package kr.hmit.dmjs.ui.m004;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import device.common.DecodeResult;
import device.common.DecodeStateCallback;
import device.common.ScanConst;
import device.sdk.ScanManager;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityOdmScanMainBinding;
import kr.hmit.dmjs.databinding.ActivityOrderManagementInfoMainBinding;
import kr.hmit.dmjs.model.response.ODD_Model;
import kr.hmit.dmjs.model.vo.ODD_VO;
import kr.hmit.dmjs.model.vo.ODM_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.network.Http_Odd;
import kr.hmit.dmjs.network.Http_Odm;
import kr.hmit.dmjs.ui.PurchaseAbalone.PurchaseScanInfoActivity;
import kr.hmit.dmjs.ui.m004.adapter.Odm02MainListAdapter;
import kr.hmit.dmjs.ui.m004.adapter.OdmScanMainListAdapter;
import kr.hmit.dmjs.ui.order_management.OrderManagementDetailActivity;
import kr.hmit.dmjs.ui.order_management.OrderManagementInfoAdd;
import kr.hmit.dmjs.ui.order_management.OrderManagementInfoDetail;
import kr.hmit.dmjs.ui.order_management.adapter.OrderManagementInfoListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OdmScanMainActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8844;
    private ActivityOdmScanMainBinding binding;
    private Context mContext;

    //스캐너
    private static final String TAG = "tScanner";
    private static ScanManager mScanner;
    private static DecodeResult mDecodeResult;
    private AlertDialog mDialog = null;
    private int mBackupResultType = ScanConst.ResultType.DCD_RESULT_COPYPASTE;
    private final Handler mHandler = new Handler();
    private ScanResultReceiver mScanResultReceiver = null;
    private ProgressDialog mWaitDialog = null;

    //발주상세 Adapter
    private OdmScanMainListAdapter mAdapter;
    private ArrayList<ODD_VO> mList;

    //발주M 정보
    private ODM_VO odmVO;

    //파라미터
    private HashMap<String, Object> paramMap = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOdmScanMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mContext = this;
        mScanner = new ScanManager();
        mDecodeResult = new DecodeResult();
        mScanResultReceiver = new ScanResultReceiver();

        //auto 스캔해제
        mScanner.aDecodeSetTriggerMode(ScanConst.TriggerMode.DCD_TRIGGER_MODE_ONESHOT);
        //mScanner.aDecodeSetTriggerMode(ScanConst.TriggerMode.DCD_TRIGGER_MODE_ONESHOT);

        //Intent Broadcast Result Type
        mScanner.aDecodeSetResultType(ScanConst.ResultType.DCD_RESULT_USERMSG);
        //mScanner.aDecodeSetResultType(ScanConst.ResultType.DCD_RESULT_EVENT);

        //Beep
        mScanner.aDecodeSetBeepEnable(0);
        //mScanner.aDecodeSetBeepEnable(1);

        //upc ??
        mScanner.aDecodeSymSetEnable(ScanConst.SymbologyID.DCD_SYM_UPCA, 1);

        initialize();
        initLayout();

        initScanner();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
    }

    @Override
    protected void initialize() {
        mList = new ArrayList<>();

        mAdapter = new OdmScanMainListAdapter();
        binding.recyclerView.setAdapter(mAdapter);

        //Intent intent = getIntent();
        //String scanData = intent.getExtras().get("SCAN_DATA").toString();
        //binding.tvReceiveNum.setText(scanData);
        //requestODD_Read();
    }


    private void ODD_Read(String ODD_01) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        paramMap = setParamMap("ODD_ID", "M_LIST2");
        paramMap.put("ODD_01",ODD_01);

        Http_Odd.odd(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).ODD_READ_API(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<ArrayList<ODD_VO>>() {
            @Override
            public void onResponse(Call<ArrayList<ODD_VO>> call, Response<ArrayList<ODD_VO>> response) {
                closeLoadingBar();
                ArrayList<ODD_VO> data = response.body();

                if (response.isSuccessful()) {
                    if(data.size() > 0){
                        bindingData(data);
                    }else{
                        bindingData(new  ArrayList<ODD_VO>());
                        BaseAlert.show(mContext,"검색결과가 없습니다." );
                    }
                } else {
                    BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ODD_VO>> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
                t.printStackTrace();
            }
        });
    }

    private void bindingData(ArrayList<ODD_VO> data) {
        mList = data;
        mAdapter.setList(mList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == OrderManagementInfoDetail.REQUEST_CODE||requestCode == OrderManagementInfoAdd.REQUEST_CODE) && resultCode == RESULT_OK) {
            ODD_Read(odmVO.ODM_01);
        }else if(requestCode == OrderManagementDetailActivity.REQUEST_CODE&&resultCode == RESULT_OK){
//            odmVO=(ODM_VO) data.getSerializableExtra("odmVO");
//            binding.tvReceiveNum.setText(odmVO.ODM_01);
//            binding.tvReceiverName.setText(odmVO.CLT_01_NM);
//            binding.tvReceiveDate.setText(datePatternChange(odmVO.ODM_02));
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();

        }
    }

    public void fnScanInfo(String QR){
        ODM_Read(QR);
    }

    public void ODM_Read(String QR){
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        paramMap = setParamMap("ODM_ID", "DETAIL");
        paramMap.put("ODM_01", QR.split(":")[0]);

        Http_Odm.odm(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).ODM_READ_API(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<ArrayList<ODM_VO>>() {
            @Override
            public void onResponse(Call<ArrayList<ODM_VO>> call, Response<ArrayList<ODM_VO>> response) {
                closeLoadingBar();
                ArrayList<ODM_VO> data = response.body();

                if (response.isSuccessful()) {
                    if(data.size() > 0){
                        setData(data);
                    }else{
                        //bindingData(new  ArrayList<ODM_VO>());
                        BaseAlert.show(mContext,"검색결과가 없습니다." );
                    }
                } else {
                    BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ODM_VO>> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
                t.printStackTrace();
            }
        });
    }

    public void setData(ArrayList<ODM_VO> data){
        odmVO = data.get(0);

        binding.tvReceiveNum.setText(odmVO.ODM_01);
        binding.tvReceiveDate.setText(odmVO.ODM_02);
        binding.tvReceiverName.setText(odmVO.CLT_02);

        ODD_Read(odmVO.ODM_01);
    }

    //스캐너 관련함수
    public class ScanResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mScanner != null) {
                try {
                    if (ScanConst.INTENT_USERMSG.equals(intent.getAction())) {
                        mScanner.aDecodeGetResult(mDecodeResult.recycle());
                        fnScanInfo(mDecodeResult.toString());
                    } else if (ScanConst.INTENT_EVENT.equals(intent.getAction())) {
                        boolean result = intent.getBooleanExtra(ScanConst.EXTRA_EVENT_DECODE_RESULT, false);
                        int decodeBytesLength = intent.getIntExtra(ScanConst.EXTRA_EVENT_DECODE_LENGTH, 0);
                        byte[] decodeBytesValue = intent.getByteArrayExtra(ScanConst.EXTRA_EVENT_DECODE_VALUE);
                        String decodeValue = new String(decodeBytesValue, 0, decodeBytesLength);
                        int decodeLength = decodeValue.length();
                        String symbolName = intent.getStringExtra(ScanConst.EXTRA_EVENT_SYMBOL_NAME);
                        byte symbolId = intent.getByteExtra(ScanConst.EXTRA_EVENT_SYMBOL_ID, (byte) 0);
                        int symbolType = intent.getIntExtra(ScanConst.EXTRA_EVENT_SYMBOL_TYPE, 0);
                        byte letter = intent.getByteExtra(ScanConst.EXTRA_EVENT_DECODE_LETTER, (byte) 0);
                        byte modifier = intent.getByteExtra(ScanConst.EXTRA_EVENT_DECODE_MODIFIER, (byte) 0);
                        int decodingTime = intent.getIntExtra(ScanConst.EXTRA_EVENT_DECODE_TIME, 0);
                        Log.d(TAG, "1. result: " + result);
                        Log.d(TAG, "2. bytes length: " + decodeBytesLength);
                        Log.d(TAG, "3. bytes value: " + decodeBytesValue);
                        Log.d(TAG, "4. decoding length: " + decodeLength);
                        Log.d(TAG, "5. decoding value: " + decodeValue);
                        Log.d(TAG, "6. symbol name: " + symbolName);
                        Log.d(TAG, "7. symbol id: " + symbolId);
                        Log.d(TAG, "8. symbol type: " + symbolType);
                        Log.d(TAG, "9. decoding letter: " + letter);
                        Log.d(TAG, "10.decoding modifier: " + modifier);
                        Log.d(TAG, "11.decoding time: " + decodingTime);
                        //mBarType.setText(symbolName);
                        System.out.println("------------------------------------------------");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private DecodeStateCallback mStateCallback = new DecodeStateCallback(mHandler) {
        public void onChangedState(int state) {
            switch (state) {
                case ScanConst.STATE_ON:
                case ScanConst.STATE_TURNING_ON:
                    if (getEnableDialog().isShowing()) {
                        getEnableDialog().dismiss();
                    }
                    break;
                case ScanConst.STATE_OFF:
                case ScanConst.STATE_TURNING_OFF:
                    if (!getEnableDialog().isShowing()) {
                        getEnableDialog().show();
                    }
                    break;
            }
        };
    };

    private void initScanner() {
        if (mScanner != null) {
            mScanner.aRegisterDecodeStateCallback(mStateCallback);
            mBackupResultType = mScanner.aDecodeGetResultType();
            mScanner.aDecodeSetResultType(ScanConst.ResultType.DCD_RESULT_USERMSG);
        }
    }

    private Runnable mStartOnResume = new Runnable() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initScanner();
                    if (mWaitDialog != null && mWaitDialog.isShowing()) {
                        mWaitDialog.dismiss();
                    }
                }
            });
        }
    };


    private AlertDialog getEnableDialog() {
        if (mDialog == null) {
            AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setTitle(R.string.app_name);
            dialog.setMessage("Your scanner is disabled. Do you want to enable it?");

            dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(android.R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.ok),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(ScanConst.LAUNCH_SCAN_SETTING_ACITON);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    });
            dialog.setCancelable(false);
            mDialog = dialog;
        }
        return mDialog;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mWaitDialog = ProgressDialog.show(mContext, "", getString(R.string.msg_wait), true);
        mHandler.postDelayed(mStartOnResume, 1000);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ScanConst.INTENT_USERMSG);
        filter.addAction(ScanConst.INTENT_EVENT);
        mContext.registerReceiver(mScanResultReceiver, filter);
    }

    @Override
    protected void onPause() {
        if (mScanner != null) {
            mScanner.aDecodeSetResultType(mBackupResultType);
            mScanner.aUnregisterDecodeStateCallback(mStateCallback);
        }
        mContext.unregisterReceiver(mScanResultReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mScanner != null) {
            mScanner.aDecodeSetResultType(mBackupResultType);
        }
        mScanner = null;
        super.onDestroy();
    }
}
