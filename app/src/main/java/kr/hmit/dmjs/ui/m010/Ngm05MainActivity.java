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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import device.common.DecodeResult;
import device.common.DecodeStateCallback;
import device.common.ScanConst;
import device.sdk.ScanManager;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityNgm04MainBinding;
import kr.hmit.dmjs.databinding.ActivityNgm05MainBinding;
import kr.hmit.dmjs.model.vo.NGGK_VO;
import kr.hmit.dmjs.model.vo.NGG_VO;
import kr.hmit.dmjs.model.vo.NGM_VO;
import kr.hmit.dmjs.model.vo.ODD_VO;
import kr.hmit.dmjs.network.Http_Ngg;
import kr.hmit.dmjs.network.Http_Nggk;
import kr.hmit.dmjs.network.Http_Ngm;
import kr.hmit.dmjs.network.Http_Odd;
import kr.hmit.dmjs.ui.m010.adapter.Ngm04MainListAdapter;
import kr.hmit.dmjs.ui.m010.adapter.Ngm05MainListAdapter;
import kr.hmit.dmjs.ui.m010.filter.NgmFilterVO;
import kr.hmit.dmjs.ui.main.MainDashboardActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Ngm05MainActivity extends BaseActivity {

    public static final int REQUEST_CODE = 9000;
    public static final String GAG_INFO = "NGG_05";

    private static final String TAG = "tScanner";
    private static ScanManager mScanner;
    private static DecodeResult mDecodeResult;
    private int mBackupResultType = ScanConst.ResultType.DCD_RESULT_COPYPASTE;
    private final Handler mHandler = new Handler();
    private static Ngm05MainActivity.ScanResultReceiver mScanResultReceiver = null;
    private AlertDialog mDialog = null;
    private ProgressDialog mWaitDialog = null;


    private ActivityNgm05MainBinding binding;
    private Ngm05MainListAdapter mAdapter;
    private ArrayList<NGGK_VO> mList;

    private String[] items = {"수조 1", "수조 2", "수조 3", "수조 4", "수조 5", "수조 6", "수조 7", "수조 8", "수조 9", "수조 10", "수조 11", "수조 12"};

    private HashMap<String, Object> paramMap = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNgm05MainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mContext = this;
        mScanner = new ScanManager();
        mDecodeResult = new DecodeResult();
        mScanResultReceiver = new Ngm05MainActivity.ScanResultReceiver();

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
        binding.imgBack.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(this::onClickSave);


        binding.addNggk04.setOnClickListener(this::onClickBatchNggk04);
        binding.nggk04.setText("0");

        binding.addNggk07.setOnClickListener(v -> onClickCombo((View) v , items , "1"));


        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

        mAdapter = new Ngm05MainListAdapter();
        binding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initLayout() {

    }

    private void onClickCombo(View v , String[] paramArry , String comboDiv) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        String title ="";

        if(comboDiv.equals("1")){
            title ="수조선택";
        }

        builder.setTitle(title).setCancelable(true)
                .setItems(paramArry, (dialog, which) -> {
                    setArry(which , comboDiv);

                }).setCancelable(false).create();

        builder.show();
    }

    private void setArry(int which , String comboDiv) {
        if(comboDiv.equals("1")){
            binding.nggk07.setText(items[which]);
        }
    }

    private void NGG_Read(String NGG_01){

        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        //기본파라미터
        paramMap = setParamMap("NGG_ID", "M_SCAN_DETAIL");
        paramMap.put("NGG_01",NGG_01);

        openLoadingBar();

        Http_Ngg.ngg(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).NGG_Read(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<ArrayList<NGG_VO>>() {
            @Override
            public void onResponse(Call<ArrayList<NGG_VO>> call, Response<ArrayList<NGG_VO>> response) {
                closeLoadingBar();
                ArrayList<NGG_VO> data = response.body();

                if (response.isSuccessful()) {
                    if(data.size() > 0){
                         NGG_DETAIL(data.get(0));
                    }else{
                        BaseAlert.show(mContext,"검색결과가 없습니다." );
                    }
                } else {
                    BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NGG_VO>> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
                t.printStackTrace();
            }
        });
    }


    public void NGG_DETAIL(NGG_VO ngg_vo){

        binding.ngg01.setText(ngg_vo.NGG_01);
        binding.ngg02.setText(ngg_vo.NGG_02);
        binding.ngg03Nm.setText(ngg_vo.NGG_03_NM);
        binding.ngg04Nm.setText(ngg_vo.NGG_04_NM);

        NGGK_Read(ngg_vo.NGG_01);
    }

    private void NGGK_Read(String NGGK_01){

        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        //기본파라미터
        paramMap = setParamMap("NGGK_ID", "M_DETAIL_LIST");
        paramMap.put("NGGK_06",NGGK_01);

        openLoadingBar();

        Http_Nggk.nggk(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).NGGK_Read(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<ArrayList<NGGK_VO>>() {
            @Override
            public void onResponse(Call<ArrayList<NGGK_VO>> call, Response<ArrayList<NGGK_VO>> response) {
                closeLoadingBar();
                ArrayList<NGGK_VO> data = response.body();

                if (response.isSuccessful()) {
                    if(data.size() > 0){
                        bindingData(data);
                    }else{
                        BaseAlert.show(mContext,"검색결과가 없습니다." );
                    }
                } else {
                    BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NGGK_VO>> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
                t.printStackTrace();
            }
        });
    }

    private void bindingData(ArrayList<NGGK_VO> data) {
        mList = data;
        mAdapter.setNggkList(data);
    }

    private void onClickBatchNggk04(View view){

        for (int i = 0; i < mList.size(); i++) {
            mAdapter.SetAll(Double.parseDouble(binding.nggk04.getText().toString()));
        }
    }

    private void onClickSave(View view) {

        ArrayList<NGGK_VO> mListCheck = mAdapter.getCheckList();

        if(mListCheck.size()>0) {

            for (int i = 0; i < mListCheck.size(); i++) {
                NGGK_U(mListCheck.get(i), true);
            }

            toast("수매 입고처리 되었습니다.");
            NGGK_Read(binding.ngg01.getText().toString());

        }else{
            toast("입고처리할 주문이 없습니다.");
        }
    }

    public void NGGK_U(NGGK_VO vo,boolean refreshFlag) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        paramMap = setParamMap("NGGK_ID", "M_UPDATE_TEST");

        paramMap.put("NGGK_01", vo.NGGK_01);
        paramMap.put("NGGK_02", vo.NGGK_02);
        paramMap.put("NGGK_03", vo.NGGK_03);
        paramMap.put("NGGK_04", vo.NGGK_04);
        paramMap.put("NGGK_05", vo.NGGK_05);
        paramMap.put("NGGK_06", vo.NGGK_06);
        paramMap.put("NGGK_07", binding.nggk07.getText().toString());
        paramMap.put("NGGK_08", vo.NGGK_08);
        paramMap.put("NGGK_09", vo.NGGK_09);
        paramMap.put("NGGK_10", vo.NGGK_10);
        paramMap.put("NGGK_11", vo.NGGK_11);

        Http_Nggk.nggk(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).NGGK_U(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<NGGK_VO>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<NGGK_VO> call, Response<NGGK_VO> response) {
                Message msg = new Message();
                msg.obj = response;
                msg.what = 100;

                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        closeLoadingBar();
                        if(refreshFlag){

                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NGGK_VO> call, Throwable t) {
                call.cancel();
                closeLoadingBar();
            }
        });
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

                        if(!mDecodeResult.toString().contains("READ_FAIL")){
                            //스캔 데이터 저정
                            binding.nggk04.setText("0");
                            binding.nggk07.setText("");
                            NGG_Read(mDecodeResult.toString());
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


}
