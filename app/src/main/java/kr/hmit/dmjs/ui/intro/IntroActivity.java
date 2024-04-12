package kr.hmit.dmjs.ui.intro;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityIntroBinding;
import kr.hmit.dmjs.ui.login.LoginActivity;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.util.PermissionUtils;

public class IntroActivity extends BaseActivity {
    //=========================
    // final
    //=========================


    //=========================
    // view
    //=========================
    private ActivityIntroBinding binding;


    //=========================
    // variable
    //=========================


    //=========================
    // initialize
    //=========================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();

        initialize();
    }

    @Override
    protected void initLayout() {

    }

    @Override
    protected void initialize() {
        new Handler().postDelayed(() -> {
     //       binding.imgIntroLogo.setVisibility(View.VISIBLE);
            binding.textTitle2.setVisibility(View.VISIBLE);


            if (PermissionUtils.checkPermissionAll(mContext))
                goLogin();
            else
                PermissionUtils.requestPermissionAll(mActivity);

        }, 500);
    }

    /**
     * 로그인 화면으로 이동한다.
     */
    private void goLogin() {
        new Handler().postDelayed(() -> {
            finish();
            goActivity(LoginActivity.class);
        }, 1000);

    }

    //=========================
    // api
    //=========================

    //=========================
    // event
    //=========================
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionUtils.REQUEST_CODE) {
            PermissionUtils.checkPermissionResult(mActivity, permissions, grantResults, new PermissionUtils.OnPermissionListener() {
                @Override
                public void permissionGranted() {
                    goLogin();
                }

                @Override
                public void permissionDenied() {
                    BaseAlert.show(mContext, R.string.intro_0, R.string.intro_1, (dialog, which) -> {
                        PermissionUtils.goAppSettings(mContext);
                        finish();
                    });
                }
            });
        }
    }
}
