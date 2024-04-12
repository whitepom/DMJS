package kr.hmit.dmjs.ui.sample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebViewClient;

import com.google.gson.Gson;

import kr.hmit.dmjs.databinding.ActivityAddressBinding;
import kr.hmit.base.base_activity.BaseActivity;

public class AddressActivity extends BaseActivity {
    //=====================================
    // region // static, final
    //=====================================
    public static final String JS_BRIDGE = "HAEWON";
    public static final int REQUEST_CODE = 2111;


    public AddressModel address;
    //=====================================
    // endregion // static, final
    //=====================================


    //=====================================
    // region // view
    //=====================================

    ActivityAddressBinding binding;

    //=====================================
    // endregion // view
    //=====================================


    //=====================================
    // region // variable
    //=====================================

    //=====================================
    // endregion // variable
    //=====================================


    //=====================================
    // region // initialize
    //=====================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();

        initialize();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initLayout() {
        binding.webView.loadUrl("http://app.smfactory.kr/SearchAddress/");
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.addJavascriptInterface(new AndroidBridge(), JS_BRIDGE);
        binding.webView.setWebViewClient(new WebViewClient());
    }

    @Override
    protected void initialize() {

    }

    private void onClickSearchAddress(View v) {

    }

    //=====================================
    // endregion // initialize
    //=====================================
    private class AndroidBridge {
        @JavascriptInterface
        public void callAndroid(final String arg) { // must be final
            Log.d("Test", arg);
            address = new Gson().fromJson(arg, AddressModel.class);

            mActivity.runOnUiThread(() -> {

                Intent intent = new Intent();
                intent.putExtra("addressData", address);
                setResult(RESULT_OK, intent);
                finish();

            });
        }
    }
}