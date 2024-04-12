
package kr.hmit.dmjs.ui.login;

import android.os.Bundle;
import android.text.TextUtils;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityLoginBinding;
import kr.hmit.dmjs.ui.main.MainDashboardActivity;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.settings.SettingsKey;
import kr.hmit.base.user_interface.UserData;
import kr.hmit.base.user_interface.UserInfo;

public class LoginActivity extends BaseActivity {
    //=========================
    // final
    //=========================


    //=========================
    // view
    //=========================
    private ActivityLoginBinding binding;

    private String check="";
    private String LoginCID = "DMJS";

    //=========================
    // variable
    //=========================


    //=========================
    // initialize
    //=========================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();

        initialize();

//        binding.etID.setText("dgoh");
//        binding.etPW.setText("!h2876633");
/*        binding.etID.setText("dgoh");
        binding.etPW.setText("!h2876633");*/

    }

    @Override
    protected void initLayout() {
        binding.btnLogin.setOnClickListener(v -> checkValidation());

    }

    @Override
    protected void initialize() {

        if (mSettings.getBooleanItem(SettingsKey.SaveId, false)) {
            binding.etID.setText(mSettings.getStringItem(SettingsKey.LoginID, ""));
            binding.chkSaveID.setChecked(true);
        }

        if (mSettings.getBooleanItem(SettingsKey.AutoLogin, false)) {
            binding.etID.setText(mSettings.getStringItem(SettingsKey.LoginID, ""));
            binding.etPW.setText(mSettings.getStringItem(SettingsKey.LoginPW, ""));
            binding.chkAutoLogin.setChecked(true);
            checkValidation();
        }
    }

    /**
     * 값 입력 여부 확인
     */
    private void checkValidation() {
        String strCode = LoginCID;
        String strID = binding.etID.getText().toString().trim();
        String strPassword = binding.etPW.getText().toString().trim();

        if (TextUtils.isEmpty(strCode)) {
            BaseAlert.show(mContext, R.string.login_0);
            return;
        }

        if (TextUtils.isEmpty(strID)) {
            BaseAlert.show(mContext, R.string.login_1);
            return;
        }

        if (TextUtils.isEmpty(strPassword)) {
            BaseAlert.show(mContext, R.string.login_2);
            return;
        }

        requestLogin(getUserData());
    }


    //=========================
    // api
    //=========================

    /**
     * 로그인을 한다.
     */
    private void requestLogin(UserData userData) {


        requestLogin(new OnRequestLogin() {
            @Override
            public void isSuccess(UserInfo userInfo) {


                if (binding.chkSaveID.isChecked()) {
                    mSettings.putBooleanItem(SettingsKey.SaveId, true);
                    mSettings.putStringItem(SettingsKey.LoginCID, LoginCID);
                    mSettings.putStringItem(SettingsKey.LoginID, binding.etID.getText().toString().trim());
                } else {
                    mSettings.putBooleanItem(SettingsKey.SaveId, false);
                }
                if (binding.chkAutoLogin.isChecked()) {
                    mSettings.putBooleanItem(SettingsKey.AutoLogin, true);
                    mSettings.putStringItem(SettingsKey.LoginCID, LoginCID);
                    mSettings.putStringItem(SettingsKey.LoginID, binding.etID.getText().toString().trim());
                    mSettings.putStringItem(SettingsKey.LoginPW, binding.etPW.getText().toString().trim());

                } else {
                    mSettings.putBooleanItem(SettingsKey.AutoLogin, false);
                }
                goMain();
            }

            @Override
            public void isFail(String errorMsg) {

            }

            @Override
            public void isNotConnectable() {
                BaseAlert.show(mContext, R.string.network_error_1);
            }
        }, userData);

    }

    /**
     * 메인으로 간다.
     */
    private void goMain() {
//        goActivity(WorkManagementListActivity.class);
//        goActivity(ScheduleMainActivity.class);

       /*   Intent intent = new Intent(getApplicationContext(), MainDashboardActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
          intent.putExtra("name","홍길동");
          startActivity(intent);*/

          goActivity(MainDashboardActivity.class);

//        goActivity(ProductionPlanMainActivity.class);

        finish();
    }

    private UserData getUserData() {
        String strCode = LoginCID;
        String strID = binding.etID.getText().toString().trim();
        String strPassword = binding.etPW.getText().toString().trim();
        return new UserData(strCode, strID, strPassword);
    }

    //=========================
    // event
    //=========================

}
