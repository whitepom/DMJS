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

import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.webkit.JavascriptInterface;

import java.util.ArrayList;
import java.util.HashMap;

import device.common.DecodeResult;
import device.common.DecodeStateCallback;
import device.common.ScanConst;
import device.sdk.ScanManager;
import kr.hmit.base.base_activity.BaseActivity;

import kr.hmit.dmjs.R;

import kr.hmit.dmjs.databinding.ActivityNgm05MainBinding;
import kr.hmit.dmjs.model.vo.NGGK_VO;

import kr.hmit.dmjs.ui.m010.adapter.Ngm05MainListAdapter;


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
        binding.webView.loadUrl("http://dm.smfactory.kr/Z04012/Z04012CMobile/TEST_L");

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setWebViewClient(new WebViewClient());
        binding.webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        binding.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        binding.webView.getSettings().setLoadsImagesAutomatically(true);
        binding.webView.getSettings().setUseWideViewPort(true);
        binding.webView.getSettings().setSupportZoom(false);
        binding.webView.getSettings().setSupportMultipleWindows(true);
        binding.webView.getSettings().setAppCacheEnabled(true);
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.getSettings().setAllowFileAccess(true);
        binding.webView.getSettings().setGeolocationEnabled(true);
        binding.webView.getSettings().setDefaultTextEncodingName("UTF-8");

    }

    @Override
    protected void initLayout() {

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
                            binding.webView.loadUrl("javascript:fnTest('"+mDecodeResult.toString()+"')");

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
