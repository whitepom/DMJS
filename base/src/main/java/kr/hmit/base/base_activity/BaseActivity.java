package kr.hmit.base.base_activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import kr.hmit.base.BaseApplication;
import kr.hmit.base.R;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.LoginModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.Http;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.base.settings.InterfaceSettings;
import kr.hmit.base.settings.SettingsKey;
import kr.hmit.base.user_interface.InterfaceUser;
import kr.hmit.base.user_interface.UserData;
import kr.hmit.base.user_interface.UserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public abstract class BaseActivity extends AppCompatActivity {
    public static Context BaseContext;
    protected Context mContext;
    protected Activity mActivity;
    protected InterfaceSettings mSettings;
    protected InterfaceUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.overridePendingTransition(R.anim.start_enter, R.anim.start_exit);
        init();
    }

    /**
     * 초기화
     */
    private void init() {
        if (BaseContext == null)
            BaseContext = this;
        if (mContext == null)
            mContext = this;
        if (mActivity == null)
            mActivity = this;
        if (mSettings == null)
            mSettings = InterfaceSettings.getInstance(this);
        if (mUser == null)
            mUser = InterfaceUser.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();

        init();
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.end_enter, R.anim.end_exit);
    }

    /**
     * 레이아웃 초기화
     */
    protected abstract void initLayout();

    /**
     * 데이터 초기화
     */
    protected abstract void initialize();

    protected void openLoadingBar() {
        BaseApplication.getInstance().openLoading(this, null);
    }

    public void closeLoadingBar() {
        BaseApplication.getInstance().closeLoading();
    }


    @Override
    protected void onPause() {
        super.onPause();

//        if (mLoadingBar != null) {
//            mLoadingBar.dismiss();
//        }
    }

    /**
     * 액티비티 이동
     *
     * @param cls
     */
    protected void goActivity(Class<?> cls) {
        Intent intent = new Intent(mContext, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mContext.startActivity(intent);
    }

    protected void goActivity(Class<?> cls, Intent intent) {
        intent.setClass(mContext, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mContext.startActivity(intent);
    }

    protected void goActivity(Class<?> cls, int requestCode) {
        Intent intent = new Intent(mContext, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, requestCode);
    }
    /**
     * 천단위마다 콤마 추가
     *
     * @param number
     * @return comma포함한 숫자(문자열)
     */
    public String commaWithNumber(double number) {
        DecimalFormat myFormatter = new DecimalFormat("#,###.####");
        return myFormatter.format(number);
    }
    /**
     * 토스트 메시지
     *
     * @param message
     */
    public void toast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
    }

    //==============================
    // api
    //==============================
    public interface OnRequestLogin {
        void isSuccess(UserInfo userInfo);

        void isFail(String errorMsg);

        void isNotConnectable();
    }

    protected void requestLogin(final OnRequestLogin onRequestLoginListener, UserData userData) {

        if (!ClsNetworkCheck.isConnectable(mContext)) {
            onRequestLoginListener.isNotConnectable();
            return;
        }

        openLoadingBar();

        Http.member(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).login(
                BaseConst.URL_HOST,
                "LIST",
                userData.MEM_CID,
                userData.MEM_01,
                userData.MEM_03
        ).enqueue(new Callback<LoginModel>() {

            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                Message msg = new Message();
                msg.obj = response;
                msg.what = 100;

                //=====================
                // response callback
                //=====================
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 100) {

                            Response<LoginModel> response = (Response<LoginModel>) msg.obj;

                            if (response.isSuccessful()) {

                                if (response.body().Data.get(0).Validation) {

                                    InterfaceUser.instance.Value = response.body().Data.get(0);

                                    mSettings.Value.TKN_03 = mUser.Value.TKN_03;
                                    mSettings.putStringItem(SettingsKey.TKN_03, mUser.Value.TKN_03);
                                    System.out.println("토큰 >>>>>>>>>>>>>>>>>>>>>>>>" +mUser.Value.TKN_03);
                                    onRequestLoginListener.isSuccess(response.body().Data.get(0));
                                } else {
                                    onRequestLoginListener.isFail(response.body().Data.get(0).ErrorCode);
                                    if(response.body().Data.get(0).ErrorCode.equals("001")){
                                        BaseAlert.show(mContext, "비밀번호를 확인하여 주십시오.");
                                    }else if(response.body().Data.get(0).ErrorCode.equals("002")){
                                        BaseAlert.show(mContext, "아이디와 비밀번호를 확인하여 주십시오.");
                                    }else{
                                        BaseAlert.show(mContext, "ErrorCode : " + response.body().Data.get(0).ErrorCode);
                                    }
                                }
                            } else {
                                toast("동시접속이 감지되어 로그인 페이지로 이동합니다.");
                               // BaseAlert.show(mContext, R.string.network_error_2);
                            }

                        }
                    }
                }.sendMessage(msg);

                closeLoadingBar();

            }

            @Override
            public void onFailure(Call<LoginModel> call, Throwable t) {
                closeLoadingBar();
                call.cancel();
            }
        });
    }
    protected void onRequestDate(String tv, String src, DatePickerDialog.OnDateSetListener datePickerListener) {
        try {
            Date date;
            Calendar mCalRequest = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            if (tv.equals("") || src.equals("")) {
                tv = formatter.format(mCalRequest.getTime());
            }
            date = formatter.parse(tv.replaceAll("-", ""));
            mCalRequest.setTime(date);
            DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                    mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        } catch (ParseException e) {
            toast(this.getClass().getName() + "에서 문제가 생겼습니다. 관리자에게 문의해주세요.");
            e.printStackTrace();
        }
    }
    public void goLogin(final Class<?> cls) {

        BaseAlert.show(mContext, "접속오류", "토큰재발행으로 인한 접속오류입니다. \n로그인화면으로 가시겠어요?",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), cls);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //저장된 정보(SharedPreferences 전체 지우기
                        SharedPreferences pref = getSharedPreferences("smfactory", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean(SettingsKey.AutoLogin, false);
                        editor.putString(SettingsKey.LoginPW, "");
                        //editor.clear();
                        editor.apply();
                        //화면 이동
                        goActivity(cls, intent);
                        finish();
                    }
                });
    }

    public HashMap<String, Object> setParamMap(String MEM_CID_COL_NM, String GUBUN){
        HashMap<String, Object> returnMap = new HashMap<String, Object>();

        returnMap.put("GUBUN"   , GUBUN);
        returnMap.put("MEM_CID" , mUser.Value.MEM_CID);
        returnMap.put("MEM_01"  , mUser.Value.MEM_01);
        returnMap.put("TKN_03"  , mUser.Value.TKN_03);
        returnMap.put(MEM_CID_COL_NM , mUser.Value.MEM_CID);

        return returnMap;
    }

}
