package kr.hmit.dmjs.ui.PurchaseAbalone;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;

import kr.hmit.dmjs.databinding.ActivityPurchaseMainFilterBinding;

import kr.hmit.dmjs.model.response.NGG_Model;
import kr.hmit.dmjs.model.response.ODM_Model;
import kr.hmit.dmjs.model.vo.NGG_VO;
import kr.hmit.dmjs.network.Http;

import kr.hmit.dmjs.ui.PurchaseAbalone.vo.FilterVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseMainFilterActivity extends BaseActivity{
    public static final int REQUEST_CODE = 2021;
    public static final String CATEGORY_INFO = "CATEGORY_INFO";

    private ActivityPurchaseMainFilterBinding binding;
    private String[] mCategory;  // 입고처 드롭리스트용
    private String[] mCategory2; // 현장명 드롭리스트용
    private FilterVO filterVO;
    private boolean flag = true;

    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if (flag) binding.dtNGG02ST.setText(sdfFormat.format(mCalRequest.getTime()));
            else binding.dtNGG02ED.setText(sdfFormat.format(mCalRequest.getTime()));

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPurchaseMainFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {

        binding.imgBack.setOnClickListener(v -> finish());
        binding.cbCLT01.setOnClickListener(this::onClickCategory);
        binding.dtNGG02ST.setOnClickListener(this::onClickRequestDate);
        binding.dtNGG02ED.setOnClickListener(this::onClickRequestDate2);
        binding.cbDAH01.setOnClickListener(this::onClickCategory2);
        binding.btnSave.setOnClickListener(this::onClickSave);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {
        Intent intent = getIntent();
        filterVO = (FilterVO) intent.getExtras().get("filterData");

        binding.cbCLT01.setText(textCheck(filterVO.CLT_01));

        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();

        binding.dtNGG02ST.setText(datePatternChange(filterVO.NGG_02_ST));
        binding.dtNGG02ED.setText(datePatternChange(filterVO.NGG_02_ED));

        requestNGG_01();
    }
    private void onClickCategory(View v) { //입고처 카테고리
        if (mCategory != null && mCategory.length == 1) {
            requestNGG_01();
        } else {
            dropdownCategory("수매처",mCategory,binding.cbCLT01);
        }

    }

    private void onClickCategory2(View v) { // 현장명 카테고리
        if (mCategory2 != null && mCategory2.length == 1) {
            requestNGG_01();
        } else {
            dropdownCategory("품목",mCategory2,binding.cbDAH01);
        }

    }


    //요청일자
    private void onClickRequestDate(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        flag=true;
        datePickerDialog.show();
    }
    //요청일자
    private void onClickRequestDate2(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        flag=false;
        datePickerDialog.show();
    }

    private void dropdownCategory(String dialogTitle, String[] category, TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(dialogTitle)
                .setItems(category, (dialog, which) -> {
                    textView.setText(category[which]);
                }).setCancelable(false).create();

        builder.show();
    }
    private String datePatternChange(String date){
        if(date.isEmpty())
            return date;
        SimpleDateFormat beforeFormat = new SimpleDateFormat("yyyymmdd");
        SimpleDateFormat afterFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date tempDate = null;

        try {
            tempDate = beforeFormat.parse(date);
        } catch (ParseException e) {
        }

        return  afterFormat.format(tempDate);
    }
    private void onClickSave(View view) {
        String all = "전체";
        String CLT_01 = binding.cbCLT01.getText().toString().equals("전체")?"":binding.cbCLT01.getText().toString();

        String NGG_02_ST = binding.dtNGG02ST.getText().toString().replaceAll("-","");

        String NGG_02_ED = binding.dtNGG02ED.getText().toString().replaceAll("-","");

        String DAH_01 = binding.cbDAH01.getText().toString().equals(all)?"":binding.cbDAH01.getText().toString();
             //   binding.tvFilter1.getText().toString().equals("전체")?"":binding.tvFilter4.getText().toString();


        filterVO = new FilterVO(CLT_01, DAH_01, NGG_02_ST,NGG_02_ED,"");
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

    private void requestNGG_01() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.ngg(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).NGG_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST",
                mUser.Value.MEM_CID,
                "",
                filterVO.NGG_02_ST,
                filterVO.NGG_02_ED,
                filterVO.CLT_01,
                filterVO.DAH_01,
               ""
        ).enqueue(new Callback<NGG_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<NGG_Model> call, Response<NGG_Model> response) {
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

                            Response<NGG_Model> response = (Response<NGG_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if (response.body().Data.get(0).Validation) {
                                        bindingCategory(response.body().Data);
                                    } else {
                                        BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                }else{
                                    bindingCategory(new ArrayList<NGG_VO>());
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
            public void onFailure(Call<NGG_Model> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
            }
        });
    }

    private void bindingCategory(@NotNull ArrayList<NGG_VO> data) {
        String[] tempList1= new String[data.size()];
        String[] tempList2= new String[data.size()];

        for (int i = 0; i < data.size(); i++) {
            tempList1[i] = data.get(i).NGG_03_NM; //CLT?
         //   tempList1[i] = data.get(i).ODM_03_NM;
        }

        for (int i = 0; i < data.size(); i++) {
            tempList2[i] = data.get(i).NGG_04_NM;
        }

        tempList1 = new HashSet<String>(Arrays.asList(tempList1)).toArray(new String[0]);
        tempList2 = new HashSet<String>(Arrays.asList(tempList2)).toArray(new String[0]);

        mCategory = new String[tempList1.length + 1];
        mCategory2 = new String[tempList2.length+1];

        mCategory[0] = "전체";
        for (int i = 0; i < tempList1.length; i++) {
            mCategory[i + 1] = tempList1[i];
        }

        mCategory2[0] ="전체";
        for (int i = 0; i < tempList2.length; i++) {
            mCategory[i + 1] = tempList2[i];
        }
    }
}
