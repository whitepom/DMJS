package kr.hmit.dmjs.ui.Cusomer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityCustomerSaleMainFilterBinding;
import kr.hmit.dmjs.model.response.CLT_Model;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.Cusomer.model.CustomerSaleFilterVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerSaleMainFilterActivity extends BaseActivity{
    public static final int REQUEST_CODE = 2021;
    public static final String REQUEST_INFO = "REQUEST_INFO";


    private ActivityCustomerSaleMainFilterBinding binding;
    private String[] mCategory, mCategory2;
    private CustomerSaleFilterVO filterVo = new CustomerSaleFilterVO();
    private HashMap<String, Object> paramMap = new HashMap<String, Object>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerSaleMainFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.clt06.setOnClickListener(this::onClickCategory);
        binding.clt07.setOnClickListener(this::onClickCategory2);
        binding.btnSave.setOnClickListener(this::onClickSave);
    }

    @Override
    protected void initialize() {
        Intent intent = getIntent();

        //메인화면에서 받은 정보
        filterVo = (CustomerSaleFilterVO) intent.getExtras().get("filterData");

        comboData("CLT_06");
        comboData("CLT_07");
    }

    private void comboData(String Param){

        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }
        openLoadingBar();

        //기본파라미터
        paramMap = setParamMap("CLT_ID", Param+"_L");

        //유저파리미터
        paramMap.put("CLT_29" ,"G");

        Http.clt(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).CLT_Read_Test(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<CLT_Model>() {
            @Override
            public void onResponse(Call<CLT_Model> call, Response<CLT_Model> response) {

                if (response.isSuccessful()) {
                    ArrayList<CLT_VO> data = response.body().Data;

                    if(Param.contains("CLT_06"))
                    {
                        bindingCategory(data);
                    }
                    else{
                        bindingCategory2(data);
                    }
                } else {
                    BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<CLT_Model> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
            }
        });

        closeLoadingBar();
    }

    private void onClickSave(View view) {

        CustomerSaleFilterVO sendFilterVo = new CustomerSaleFilterVO();

        //메인화면으로 보낼 정보
        String CLT_06 = binding.clt06.getText().toString().equals("- 전체 -")?"":binding.clt06.getText().toString();
        String CLT_07 = binding.clt07.getText().toString().equals("- 전체 -")?"":binding.clt07.getText().toString();

        sendFilterVo.setCLT_06(CLT_06);
        sendFilterVo.setCLT_07(CLT_07);

        Intent intent = new Intent();
        intent.putExtra(REQUEST_INFO, sendFilterVo);
        
        setResult(RESULT_OK, intent);
        finish();
    }

    private void bindingCategory(@NotNull ArrayList<CLT_VO> data) {

        if(data.size() < 1)
        {
            mCategory = new String[1];
            mCategory[0] = "- 전체 -";
        }
        else{
            String[] tempList1 = new String[data.size()];

            for (int i = 0; i < data.size(); i++) {
                tempList1[i] = data.get(i).CLT_06;
            }

            tempList1 = new HashSet<String>(Arrays.asList(tempList1)).toArray(new String[0]);

            mCategory = new String[tempList1.length + 1];

            mCategory[0] = "- 전체 -";

            for (int i = 0; i < tempList1.length; i++) {
                mCategory[i + 1] = tempList1[i];
            }
        }
    }

    private void bindingCategory2(@NotNull ArrayList<CLT_VO> data) {

        if(data.size() < 1)
        {
            mCategory2 = new String[1];
            mCategory2[0] = "- 전체 -";
        }
        else{
            String[] tempList1 = new String[data.size()];

            for (int i = 0; i < data.size(); i++) {
                tempList1[i] = data.get(i).CLT_07;
            }

            tempList1 = new HashSet<String>(Arrays.asList(tempList1)).toArray(new String[0]);

            mCategory2 = new String[tempList1.length + 1];

            mCategory2[0] = "- 전체 -";

            for (int i = 0; i < tempList1.length; i++) {
                mCategory2[i + 1] = tempList1[i];
            }
        }
    }


    private void onClickCategory(View v) {
        dropdownCategory("주문경로",mCategory,binding.clt06);
    }

    private void onClickCategory2(View v) {
        dropdownCategory("고객구분",mCategory2,binding.clt07);
    }


    private void dropdownCategory(String dialogTitle, String[] category, TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(dialogTitle)
                .setItems(category, (dialog, which) -> {
                    textView.setText(category[which]);
                }).setCancelable(false).create();

        builder.show();
    }
}
