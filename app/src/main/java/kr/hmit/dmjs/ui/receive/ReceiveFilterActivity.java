package kr.hmit.dmjs.ui.receive;

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
import kr.hmit.dmjs.databinding.ActivityReceiveFilterBinding;
import kr.hmit.dmjs.model.response.GEM_Model;
import kr.hmit.dmjs.model.response.ODD_Model;
import kr.hmit.dmjs.model.vo.GEM_VO;
import kr.hmit.dmjs.model.vo.ODD_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.login.LoginActivity;
import kr.hmit.dmjs.ui.receive.model.FilterVO;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiveFilterActivity extends BaseActivity {
    public static final int REQUEST_CODE = 2021;
    public static final String CATEGORY_INFO = "CATEGORY_INFO";

    private ActivityReceiveFilterBinding binding;


    private String[] mCategory2;
    private String[] mCategory3;
    private String[] mCategory4;
    private String[] mCategory22;
    private String[] CLT_01;
    private String CLT_01_;

    private FilterVO filterVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReceiveFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CLT_01_="";
        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.tvFilter2.setOnClickListener(this::onClickCategory2);
        binding.tvFilter3.setOnClickListener(this::onClickCategory3);
        binding.tvFilter4.setOnClickListener(this::onClickCategory4);
        binding.btnSave.setOnClickListener(this::onClickSave);
    }

    @Override
    protected void initialize() {
        Intent intent = getIntent();
        filterVO = (FilterVO) intent.getExtras().get("filterData");

        binding.tvFilter2.setText(textCheck(filterVO.CLT_02));
        binding.tvFilter3.setText(textCheck(filterVO.ODD_03)); // 품번
        binding.tvFilter4.setText(textCheck(filterVO.GEM_04_NM));
        CLT_01_=filterVO.CLT_01;

        requestODD_01();
        requestODD_0102();
    }

    private void onClickCategory2(View v) {
        if (mCategory2 != null && mCategory2.length == 1) {
            requestODD_01();

        } else {
            dropdownCategory("입고처",mCategory2,binding.tvFilter2);
        }
    }
    private void onClickCategory3(View v) {
        if (mCategory3 != null && mCategory3.length == 1) {
            requestODD_0102();

        } else {
            dropdownCategoryDAH("품번",mCategory3,mCategory4,binding.tvFilter3,binding.tvFilter4);

        }
    }
    private void onClickCategory4(View v) {
        if (mCategory4 != null && mCategory4.length == 1) {
            requestODD_0102();

        } else {
           // dropdownCategory("품번",mCategory3,binding.tvFilter3);
            dropdownCategoryDAH("품명",mCategory4,mCategory3,binding.tvFilter4,binding.tvFilter3);

        }
    }
/*    private void onClickCategory5(View v) {
        if (mCategory5 != null && mCategory5.length == 1) {
            requestODD_01();
        } else {
            dropdownCategory("규격",mCategory5,binding.tvFilter5);
        }
    }*/

    private void dropdownCategory(String dialogTitle, String[] category, TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(dialogTitle)
                .setItems(category, (dialog, which) -> {
                    textView.setText(category[which]);
                    if(dialogTitle.equals("입고처")) CLT_01_=CLT_01[which];
                }).setCancelable(false).create();

        builder.show();
    }
    private void dropdownCategoryDAH(String dialogTitle, String[] category, String[] category2,TextView textView, TextView textView2) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(dialogTitle)
                .setItems(category, (dialog, which) -> {
                    textView.setText(category[which]);
                    textView2.setText(category2[which]);
                }).setCancelable(false).create();

        builder.show();
    }


    private void onClickSave(View view) {
        String tvFilter2Text = binding.tvFilter2.getText().toString().equals("전체")?"":binding.tvFilter2.getText().toString();
        String tvFilter3Text = binding.tvFilter3.getText().toString().equals("전체")?"":binding.tvFilter3.getText().toString();
        String tvFilter4Text = binding.tvFilter4.getText().toString().equals("전체")?"":binding.tvFilter4.getText().toString();
        filterVO = new FilterVO( tvFilter3Text, //품번
                                 tvFilter2Text, //입고처
                                 tvFilter4Text, //품명
                                 CLT_01_,//입고처번호
                       "", //기간조회
                       "" //기간조회
                                 );
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

    private void requestODD_01() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

    /*    Http.odd(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).ODD_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST2",
                mUser.Value.MEM_CID,
                "",
                "",
                ""*/
        Http.gem(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).GEM_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST_C",
                "DMJS",
                filterVO.GEM_02_ST,
                filterVO.GEM_02_ED,
                filterVO.ODD_03, //품명
                filterVO.CLT_01  // 입고처
        ).enqueue(new Callback<GEM_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<GEM_Model> call, Response<GEM_Model> response) {
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

                            Response<GEM_Model> response = (Response<GEM_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
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
                                       // BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                }else{
                                    bindingCategory(new ArrayList<GEM_VO>());
                                    toast("검색결과가 없습니다.");
                                }
                            } else {
                                BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<GEM_Model> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
            }
        });
    }
    private void requestODD_0102() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

    /*    Http.odd(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).ODD_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST2",
                mUser.Value.MEM_CID,
                "",
                "",
                ""*/
        Http.gem(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).GEM_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST_D",
                "DMJS",
                filterVO.GEM_02_ST,
                filterVO.GEM_02_ED,
                filterVO.ODD_03, //품명
                filterVO.CLT_01  // 입고처
        ).enqueue(new Callback<GEM_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<GEM_Model> call, Response<GEM_Model> response) {
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

                            Response<GEM_Model> response = (Response<GEM_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if (response.body().Data.get(0).Validation) {
                                        bindingCategory2(response.body().Data);
                                    } else {
                                        toast("동일계정 접속 > 로그인 페이지로 이동합니다");  // asdfasdfasdf
                                        mSettings.putBooleanItem(SettingsKey.AutoLogin,false);
                                        Intent intent = new Intent(mContext, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("simultaneous_connect",mUser.Value.MEM_01.toString());
                                        startActivity(intent);
                                        finish();
                                        // BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                }else{
                                    bindingCategory2(new ArrayList<GEM_VO>());
                                    toast("검색결과가 없습니다.");
                                }
                            } else {
                                BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<GEM_Model> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
            }
        });
    }
   /* private void bindingCategory(ArrayList<GEM_VO> data) {
        mCategory2 = new String[data.size() + 1];
        mCategory3 = new String[data.size() + 1];
        mCategory4 = new String[data.size() + 1];
        mCategory22 = new String[data.size() + 1];
        CLT_01 = new String[data.size() + 1];

            mCategory2[0] = "전체";
            CLT_01[0] = "";
            for (int i = 0; i < data.size(); i++) {
                mCategory2[i + 1] = data.get(i).CLT_02;
                CLT_01[i + 1] = data.get(i).GEM_03;
            }


            mCategory3[0] = "전체";

            for (int i = 0; i < data.size(); i++) {
                mCategory4[i + 1] = data.get(i).GEM_04_NM;
                mCategory3[i + 1] = data.get(i).GEM_04;
            }

            mCategory4[0] = "전체";

            for (int i = 0; i < data.size(); i++) {
                mCategory4[i + 1] = data.get(i).GEM_04_NM;
                mCategory3[i + 1] = data.get(i).GEM_04;
            }
        }*/
   private void bindingCategory(ArrayList<GEM_VO> data) {
       mCategory2 = new String[data.size() + 1];
       mCategory22 = new String[data.size() + 1];
       CLT_01 = new String[data.size() + 1];

       mCategory2[0] = "전체";
       CLT_01[0] = "";
       for (int i = 0; i < data.size(); i++) {
           mCategory2[i + 1] = data.get(i).CLT_02;
           CLT_01[i + 1] = data.get(i).GEM_03;
       }
   }
    private void bindingCategory2(ArrayList<GEM_VO> data) {

        mCategory3 = new String[data.size() + 1];
        mCategory4 = new String[data.size() + 1];


        mCategory3[0] = "전체";

        for (int i = 0; i < data.size(); i++) {
            mCategory4[i + 1] = data.get(i).GEM_04_NM;
            mCategory3[i + 1] = data.get(i).GEM_04;
        }

        mCategory4[0] = "전체";

        for (int i = 0; i < data.size(); i++) {
            mCategory4[i + 1] = data.get(i).GEM_04_NM;
            mCategory3[i + 1] = data.get(i).GEM_04;
        }
    }

}
