package kr.hmit.dmjs.ui.m017;

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

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
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
import kr.hmit.dmjs.databinding.ActivityZag05ScanMainBinding;
import kr.hmit.dmjs.model.vo.LOTG_VO;
import kr.hmit.dmjs.model.vo.NGG_VO;
import kr.hmit.dmjs.model.vo.ODD_VO;
import kr.hmit.dmjs.model.vo.ZAG_VO;
import kr.hmit.dmjs.network.Http_Lotg;
import kr.hmit.dmjs.network.Http_Zag;
import kr.hmit.dmjs.ui.m004.OdmScanMainActivity;
import kr.hmit.dmjs.ui.m004.adapter.OdmScanMainListAdapter;
import kr.hmit.dmjs.ui.m017.adapter.Zag05ScanMainListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Zag05ScanActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8023;
    public static final String GAG_INFO = "ZAG_05_SCAN";

    private static final String TAG = "tScanner";
    private static ScanManager mScanner;
    private static DecodeResult mDecodeResult;
    private int mBackupResultType = ScanConst.ResultType.DCD_RESULT_COPYPASTE;
    private final Handler mHandler = new Handler();
    private static ScanResultReceiver mScanResultReceiver = null;
    private AlertDialog mDialog = null;
    private ProgressDialog mWaitDialog = null;

    private ActivityZag05ScanMainBinding binding;
    private ZAG_VO zag_vo = new ZAG_VO();

    private ArrayList<ZAG_VO> mList = new ArrayList<>();
    private Zag05ScanMainListAdapter mAdapter;
    private ArrayList<LOTG_VO> mList_LOTG = new ArrayList<>();

    //파라미터
    private HashMap<String, Object> paramMap = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityZag05ScanMainBinding.inflate(getLayoutInflater());
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

    @Override
    protected void initialize() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
    }

    @Override
    protected void initLayout() {
        ZAG_Read();

        mList = new ArrayList<>();
        mAdapter = new Zag05ScanMainListAdapter();
        binding.recyclerView.setAdapter(mAdapter);
    }
    private void ZAG_Read() {

        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        //기본파라미터
        paramMap = setParamMap("ZAG_ID", "M_LAST_DETAIL");
        paramMap.put("ZAG_03","");

        openLoadingBar();

        Http_Zag.zag(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).ZAG_Read(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<ArrayList<ZAG_VO>>() {
            @Override
            public void onResponse(Call<ArrayList<ZAG_VO>> call, Response<ArrayList<ZAG_VO>> response) {
                closeLoadingBar();
                ArrayList<ZAG_VO> data = response.body();

                if (response.isSuccessful()) {
                    if(data.size() > 0){
                        bindingData(data);
                    }else{
                        bindingData(new  ArrayList<ZAG_VO>());
                        BaseAlert.show(mContext,"검색결과가 없습니다." );
                    }
                } else {
                    BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ZAG_VO>> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
                t.printStackTrace();
            }
        });
    }

    private void bindingData(ArrayList<ZAG_VO> data) {
        if(data != null && data.size() > 0){
            zag_vo = (ZAG_VO) data.get(0);

            binding.zag01.setText(zag_vo.ZAG_01);
            binding.zag02.setText(zag_vo.ZAG_02);
            binding.zag03Nm.setText(zag_vo.ZAG_03_NM);
            binding.zag0405.setText(zag_vo.ZAG_0405);
            binding.zag05Rate.setText(String.valueOf(zag_vo.ZAG_05_RATE));
            binding.zag1213.setText(zag_vo.ZAG_1213);
            binding.zag11.setText(String.valueOf(zag_vo.ZAG_11));

            LOTG_Read();
        }
    }

    private void LOTG_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        //기본파라미터
        paramMap = setParamMap("LOTG_ID", "M_LOTG_SCAN_LIST");
        paramMap.put("LOTG_04",binding.zag01.getText());

        openLoadingBar();

        Http_Lotg.lotg(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).LOTG_Read(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<ArrayList<LOTG_VO>>() {
            @Override
            public void onResponse(Call<ArrayList<LOTG_VO>> call, Response<ArrayList<LOTG_VO>> response) {
                closeLoadingBar();
                ArrayList<LOTG_VO> data = response.body();

                if (response.isSuccessful()) {
                    if(data.size() > 0){
                        bindingData2(data);
                    }else{
                        bindingData2(new  ArrayList<LOTG_VO>());
                        //BaseAlert.show(mContext,"검색결과가 없습니다." );
                    }
                } else {
                    BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<LOTG_VO>> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
                t.printStackTrace();
            }
        });
    }

    private void bindingData2(ArrayList<LOTG_VO> data) {
        mList_LOTG = data;
        mAdapter.setList(mList_LOTG);
    }

    //스캐너 관련함수
    public class ScanResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mScanner != null) {
                try {
                    if (ScanConst.INTENT_USERMSG.equals(intent.getAction())) {
                        mScanner.aDecodeGetResult(mDecodeResult.recycle());

                        System.out.println("---------------------------------------------------------------");

                        if(mDecodeResult.toString() != "READ_FAIL"){
                            //스캔 데이터 저정
                            LOTG_U(mDecodeResult.toString());
                        }
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

    public void LOTG_U(String scanData) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        paramMap = setParamMap("LOTG_ID", "CONTROL");
        paramMap.put("LOTG_01", "");
        paramMap.put("LOTG_02", "");
        paramMap.put("LOTG_03", "");
        paramMap.put("LOTG_04", binding.zag01.getText());
        paramMap.put("LOTG_05", "");
        paramMap.put("LOTG_06", "");
        paramMap.put("LOTG_07", "");
        paramMap.put("LOTG_09", scanData);


        Http_Lotg.lotg(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).LOTG_U(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<LOTG_VO>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<LOTG_VO> call, Response<LOTG_VO> response) {
                Message msg = new Message();
                msg.obj = response;
                msg.what = 100;

                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        closeLoadingBar();

                        toast("활전복 생산실적을 등록하였습니다.");

                        LOTG_Read();
                    }
                }.sendMessage(msg);
            }
            @Override
            public void onFailure(Call<LOTG_VO> call, Throwable t) {
                call.cancel();
                closeLoadingBar();
            }
        });
    }
}
