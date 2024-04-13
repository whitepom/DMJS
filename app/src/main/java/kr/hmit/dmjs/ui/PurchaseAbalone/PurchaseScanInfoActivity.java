package kr.hmit.dmjs.ui.PurchaseAbalone;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityPurchaseScanInfoBinding;
import kr.hmit.dmjs.model.response.NGGK_Model;
import kr.hmit.dmjs.model.vo.NGGK_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.network.Http_Nggk;
import kr.hmit.dmjs.ui.m004.OdmScanMainActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.BroadcastReceiver;

import device.common.DecodeResult;
import device.common.DecodeStateCallback;
import device.common.ScanConst;
import device.sdk.ScanManager;


public class PurchaseScanInfoActivity extends BaseActivity {

    //스캐너
    private static final String TAG = "tScanner";
    private static ScanManager mScanner;
    private static DecodeResult mDecodeResult;
    private AlertDialog mDialog = null;
    private int mBackupResultType = ScanConst.ResultType.DCD_RESULT_COPYPASTE;
    private final Handler mHandler = new Handler();
    private PurchaseScanInfoActivity.ScanResultReceiver mScanResultReceiver = null;
    private ProgressDialog mWaitDialog = null;

    public static final int REQUEST_CODE = 7232;
    public static final String ORDER_REQUEST_ADD = "ORDER_REQUEST_ADD";

    private Context mContext;

    private ActivityPurchaseScanInfoBinding binding;

    String[] items = {"-선택-", "수조 1", "수조 2", "수조 3", "수조 4", "수조 5", "수조 6", "수조 7", "수조 8", "수조 9", "수조 10", "수조 11", "수조 12"};
    private HashMap<String, String> map = new HashMap<String,String>();

    private HashMap<String, Object> paramMap = new HashMap<String, Object>();

    NGGK_VO nggk_vo = new NGGK_VO();


    public String strNggk07 ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPurchaseScanInfoBinding.inflate(getLayoutInflater());
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


        setSojoMap();

        ///////
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items
        );


        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strNggk07= items[position];

                System.out.println(strNggk07);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //textView.setText("선택 : ");
            }
        });

        initLayout();
        initialize();

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

    public  class ScanResultReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mScanner != null) {
                try {
                    if (ScanConst.INTENT_USERMSG.equals(intent.getAction())) {
                        mScanner.aDecodeGetResult(mDecodeResult.recycle());

                        if(!mDecodeResult.toString().contains("READ_FAIL")){
                            //스캔 데이터 저정
                            fnSelect(mDecodeResult.toString());
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
                        //mResult.setText(decodeValue);
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


    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.btnUpdate.setOnClickListener(this::onClickSave);
    }

    @Override
    protected void initialize() {
        setSojoMap();
    }

    public void fnSelect(String scanData){
        NGGK_Read(scanData);
    }

    private void NGGK_Read(String scanData) {

        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        //기본파라미터
        paramMap = setParamMap("NGGK_ID", "SCAN_MOBILE");
        paramMap.put("NGGK_06",scanData);

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

        nggk_vo = (NGGK_VO)data.get(0);

        binding.nggk01.setText(nggk_vo.NGGK_01);
        binding.nggk02.setText(String.valueOf(nggk_vo.NGGK_02));
        binding.ngg02.setText(nggk_vo.NGG_02);
        binding.ngg03Nm.setText(nggk_vo.NGG_03_NM);
        binding.ngg04Nm.setText(nggk_vo.NGG_04_NM);
        binding.ngg03.setText(nggk_vo.NGG_03);
        binding.nggk03.setText(nggk_vo.NGGK_03);
        binding.nggk04.setText("0");
        binding.bfNggk04.setText(String.valueOf(nggk_vo.BF_NGGK_04));
        binding.bfNggk07.setText(nggk_vo.BF_NGGK_07);

        //strNggk07 = nggk_vo.NGGK_07;

        //int index = 0 ;

        // Iterator 사용 3 - entrySet() : key / value
        //for(map.Entry<String, String> elem : map.entrySet()){
        //    if(elem.getKey().equals(nggk_vo.NGGK_05)){
        //        binding.spinner.setSelection(index);
        //        break;
        //    }
        //    index++;
        //}
    }

    private void setSojoMap(){
        map.put("001", "수조 1");
        map.put("002", "수조 2");
        map.put("003", "수조 3");
        map.put("004", "수조 4");
        map.put("005", "수조 5");
        map.put("006", "수조 6");
        map.put("007", "수조 7");
        map.put("008", "수조 8");
        map.put("009", "수조 9");
        map.put("010", "수조 10");
        map.put("011", "수조 11");
        map.put("012", "수조 12");
        map.put("999", "-선택-");
    }

    public void NGGK_U() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        paramMap = setParamMap("NGGK_ID", "M_UPDATE_C");

        paramMap.put("NGGK_01", binding.nggk01.getText());
        paramMap.put("NGGK_02", binding.nggk02.getText());
        paramMap.put("NGGK_03", "");
        paramMap.put("NGGK_04", Double.parseDouble(binding.nggk04.getText().toString()));
        paramMap.put("NGGK_05", "");
        paramMap.put("NGGK_06", "");
        paramMap.put("NGGK_07", strNggk07);
        paramMap.put("NGGK_08", "");
        paramMap.put("NGGK_09", "");
        paramMap.put("NGGK_10", "");
        paramMap.put("NGGK_11", "");

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

    private void onClickSave(View view) {
        NGGK_U();
    }

}
