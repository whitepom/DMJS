package kr.hmit.dmjs.ui.m020;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.dmjs.databinding.ActivityTemp01MainBinding;
import kr.hmit.dmjs.ui.main.MainDashboardActivity;


public class Temp01MainActivity extends BaseActivity {

    private ActivityTemp01MainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTemp01MainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
        initLayout();
    }


    @Override
    protected void initialize() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.imgGoHome.setOnClickListener(this::onClickGoHome);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initLayout() {
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setDomStorageEnabled(true);
        binding.webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        binding.webView.setWebViewClient(new WebViewClient());
        binding.webView.loadUrl("http://dm.smfactory.kr/Z04012/Z04012CMobile/TEMP_L");

    }


    private void onClickGoHome(View v){
        Intent intent = new Intent(this, MainDashboardActivity.class);
        startActivity(intent);
    }
}
