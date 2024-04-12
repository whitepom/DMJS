package kr.hmit.dmjs.ui.m010;

import android.annotation.SuppressLint;
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
import kr.hmit.dmjs.databinding.ActivityNgm04MainDetailBinding;
import kr.hmit.dmjs.databinding.ActivityZag05ScanMainBinding;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.model.vo.GEM_VO;
import kr.hmit.dmjs.model.vo.LOTG_VO;
import kr.hmit.dmjs.model.vo.NGM_VO;
import kr.hmit.dmjs.model.vo.NGO_VO;
import kr.hmit.dmjs.model.vo.ZAG_VO;
import kr.hmit.dmjs.network.Http_Lotg;
import kr.hmit.dmjs.network.Http_Ngm;
import kr.hmit.dmjs.network.Http_Ngo;
import kr.hmit.dmjs.network.Http_Zag;
import kr.hmit.dmjs.ui.m010.adapter.Ngm04MainDetailListAdapter;
import kr.hmit.dmjs.ui.m017.adapter.Zag05ScanMainListAdapter;
import kr.hmit.dmjs.ui.order_request.ClientNameListActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ngm04MainDetailActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8023;
    public static final String GAG_INFO = "NGM_04";

    private static final String TAG = "tScanner";
    private static ScanManager mScanner;
    private static DecodeResult mDecodeResult;
    private int mBackupResultType = ScanConst.ResultType.DCD_RESULT_COPYPASTE;
    private final Handler mHandler = new Handler();
    private static ScanResultReceiver mScanResultReceiver = null;
    private AlertDialog mDialog = null;
    private ProgressDialog mWaitDialog = null;

    private ActivityNgm04MainDetailBinding binding;

    private NGM_VO ngm_vo = new NGM_VO();

    private ArrayList<NGO_VO> mList = new ArrayList<>();
    private Ngm04MainDetailListAdapter mAdapter;

    //파라미터
    private HashMap<String, Object> paramMap = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNgm04MainDetailBinding.inflate(getLayoutInflater());
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


    @Override
    protected void initialize() {
        binding.imgUpdate.setOnClickListener(this::onClickUpdate);
        binding.imgBack.setOnClickListener(v -> finish());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
    }

    @Override
    protected void initLayout() {

        Intent intent = getIntent();
        ngm_vo = (NGM_VO) intent.getExtras().get("NGM_VO");

        binding.ngm01.setText(ngm_vo.NGM_01);
        binding.ngm02.setText(ngm_vo.NGM_02);
        binding.ngm03.setText(ngm_vo.NGM_03);
        binding.ngm04Nm.setText(ngm_vo.NGM_04_NM);

        //();

        mList = new ArrayList<>();
        mAdapter = new Ngm04MainDetailListAdapter();
        binding.recyclerView.setAdapter(mAdapter);

        NGO_Read();
    }

    private void NGO_Read() {

        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        //기본파라미터
        paramMap = setParamMap("NGO_ID", "M_DETAIL_LIST");
        paramMap.put("NGO_01", binding.ngm01.getText());

        openLoadingBar();

        Http_Ngo.ngo(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).NGO_Read(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<ArrayList<NGO_VO>>() {
            @Override
            public void onResponse(Call<ArrayList<NGO_VO>> call, Response<ArrayList<NGO_VO>> response) {
                closeLoadingBar();
                ArrayList<NGO_VO> data = response.body();

                if (response.isSuccessful()) {
                    if(data.size() > 0){
                        bindingData(data);
                    }else{
                        bindingData(new  ArrayList<NGO_VO>());
                        toast("검색결과가 없습니다.");
                    }
                } else {
                    BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NGO_VO>> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
                t.printStackTrace();
            }
        });
    }

    private void bindingData(ArrayList<NGO_VO> data) {
        mList = data;
        mAdapter.setNgoList(data);
    }


    //스캐너 관련함수
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
                            NGO_U(mDecodeResult.toString());
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


    public void NGO_U(String scanData) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        paramMap = setParamMap("NGO_ID", "M_CONTROL");
        paramMap.put("NGO_01", binding.ngm01.getText());
        paramMap.put("NGO_02", "");
        paramMap.put("NGO_03", "");
        paramMap.put("NGO_04", "");
        paramMap.put("NGO_05", 0);
        paramMap.put("NGO_06", 0);
        paramMap.put("NGO_07", 0);
        paramMap.put("NGO_08", "");
        paramMap.put("NGO_09", scanData);
        paramMap.put("NGO_10", "");
        paramMap.put("NGO_80", 0);
        paramMap.put("NGO_97", "");
        paramMap.put("NGO_98", "");

        Http_Ngo.ngo(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).NGO_U(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<NGO_VO>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<NGO_VO> call, Response<NGO_VO> response) {
                Message msg = new Message();
                msg.obj = response;
                msg.what = 100;

                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        closeLoadingBar();

                        toast("활전복 생산실적을 등록하였습니다.");

                        NGO_Read();
                    }
                }.sendMessage(msg);
            }
            @Override
            public void onFailure(Call<NGO_VO> call, Throwable t) {
                call.cancel();
                closeLoadingBar();
            }
        });
    }

    private void onClickUpdate(View v) {
        Intent intent = new Intent(mContext, Ngm04MainUpdateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        NGM_VO ngm_vo = new NGM_VO();
        ngm_vo.NGM_01 = binding.ngm01.getText().toString();
        intent.putExtra("NGM_VO", ngm_vo);

        mActivity.startActivityForResult(intent, Ngm04MainUpdateActivity.REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Ngm04MainUpdateActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            finish();
        }
    }
}
