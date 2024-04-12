package kr.hmit.dmjs.ui.work_management;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import kr.hmit.base.settings.SettingsKey;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityWorkManagementFilterBinding;
import kr.hmit.dmjs.model.response.MEM_ReadModel;
import kr.hmit.dmjs.model.response.WKS_Model;
import kr.hmit.dmjs.model.vo.MEM_ReadVO;
import kr.hmit.dmjs.model.vo.WKS_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.ui.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import kr.hmit.dmjs.ui.work_management.model.FilterVO;

public class WorkManagementFilterActivity extends BaseActivity {
    public static final int REQUEST_CODE = 2023;
    public static final String CATEGORY_INFO = "CATEGORY_INFO";

    //================================
    // region // view
    private ActivityWorkManagementFilterBinding binding;
    // endregion // view
    //================================

    private String[] mCategory;
    private String[] mCategory2;

    private FilterVO filterVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkManagementFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.tvFilter1.setOnClickListener(this::onClickCategory1);
        binding.tvFilter2.setOnClickListener(this::onClickCategory2);
        binding.tvFilter3.setOnClickListener(this::onClickCategory3);
        binding.btnSave.setOnClickListener(this::onClickSave);
    }

    @Override
    protected void initialize() {
        mCategory2 = new String[]{getString(R.string.write_work_1)};

        Intent intent = getIntent();
        filterVO = (FilterVO) intent.getExtras().get("filterData");

        binding.tvFilter1.setText(textCheck(filterVO.WKS_1001));
        binding.tvFilter2.setText(textCheck(filterVO.WKS_98));
        binding.tvFilter3.setText(textCheck2(filterVO.WKS_05));

        requestMEM_Read();
        requestWKS_05();


    }


    private void onClickCategory1(View v) {
        dropdownCategory("담당자",mCategory,binding.tvFilter1);

    }
    private void onClickCategory2(View v) {
        dropdownCategory("요청자",mCategory,binding.tvFilter2);

    }
    private void onClickCategory3(View v) {
        dropdownCategory("업무분류",mCategory2,binding.tvFilter3);

    }

    private void dropdownCategory(String dialogTitle, String[] category,TextView textView) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(dialogTitle)
                .setItems(category, (dialog, which) -> {
                    textView.setText(category[which]);
                }).setCancelable(false).create();

        builder.show();

    }
    private void onClickSave(View view) {

        String tvFilter1Text = binding.tvFilter1.getText().toString();
        String tvFilter2Text = binding.tvFilter2.getText().toString();
      //  String tvFilter3Text = "";
        String tvFilter3Text = binding.tvFilter3.getText().toString();

        filterVO = new FilterVO(textCheck(tvFilter1Text), textCheck(tvFilter2Text),textCheck2(tvFilter3Text));

        Intent intent = new Intent();
        intent.putExtra(CATEGORY_INFO, filterVO);
        setResult(RESULT_OK, intent);
        finish();

    }

    private String textCheck(String str) {
        if(str.equals(""))
            str="전체";
        else if(str.equals("전체"))
            str="";
        else
            str=str.split(" ")[0];

        return str;
    }
    private String textCheck2(String str) {
        if(str.equals(""))
            str="전체";
        else if(str.equals("전체"))
            str="";

        return str;
    }
    private void requestMEM_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.wks(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).MEM_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03
        ).enqueue(new Callback<MEM_ReadModel>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<MEM_ReadModel> call, Response<MEM_ReadModel> response) {
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
                            closeLoadingBar();

                            Response<MEM_ReadModel> response = (Response<MEM_ReadModel>) msg.obj;

                            if (response.isSuccessful()) {
                                if (response.body().Data.get(0).Validation) {
                                    bindingCategory(response.body().Data);
                                } else {
                                    toast("동일계정 접속 > 로그인 페이지로 이동합니다");  // asdfasdfasdf
                                    mSettings.putBooleanItem(SettingsKey.AutoLogin,false);
                                    Intent intent = new Intent(mContext, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("simultaneous_connect",mUser.Value.MEM_01.toString());
                                    startActivity(intent);
                                    finish();
                                   // BaseAlert.show(mContext, mContext.getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ErrorCode);
                                }
                            } else {
                                BaseAlert.show(mContext, mContext.getString(R.string.network_error_2) + "-" + response.errorBody());
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<MEM_ReadModel> call, Throwable t) {
                closeLoadingBar();
                call.cancel();
                BaseAlert.show(mContext, R.string.network_error_2);
            }
        });
    }
    private void bindingCategory(ArrayList<MEM_ReadVO> data) {
        mCategory = new String[data.size() + 1];

        mCategory[0] = "전체";

        for(int i=0;i<data.size();i++){
            mCategory[i + 1] = data.get(i).MEM_02+" "+data.get(i).MEM_32_NM;
        }

    }
    public void requestWKS_05() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            return;
        }

        openLoadingBar();

        Http.wks(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).WKS_05(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                mUser.Value.MEM_CID
        ).enqueue(new Callback<WKS_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<WKS_Model> call, Response<WKS_Model> response) {
                Message msg = new Message();
                msg.obj = response;
                msg.what = 100;

                //=====================
                // response callback
                //=====================
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        closeLoadingBar();
                        if (msg.what == 100) {
                            Response<WKS_Model> response = (Response<WKS_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if (response.body().Total > 0) {
                                    bindingCategory2(response.body().Data);
                                }
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<WKS_Model> call, Throwable t) {
                call.cancel();
                closeLoadingBar();
            }
        });
    }

    private void bindingCategory2(ArrayList<WKS_VO> data) {
        mCategory2 = new String[data.size() + 1];

        mCategory2[0] = "전체";

        for (int i = 0; i < data.size(); i++) {
            mCategory2[i + 1] = data.get(i).WKS_05;

        }
    }

}