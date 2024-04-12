package kr.hmit.dmjs.ui.order_request;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityOrderRequestFilterBinding;
import kr.hmit.dmjs.model.response.CDO_Model;
import kr.hmit.dmjs.model.vo.CDO_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.order_request.model.FilterVO;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRequestFilterActivity extends BaseActivity{
    public static final int REQUEST_CODE = 2021;
    public static final String CATEGORY_INFO = "CATEGORY_INFO";

    private ActivityOrderRequestFilterBinding binding;
    private FilterVO filterVO;
    private int flag = 0;
    private String[] mCategory;
    private String[] mCategoryCode;
    private String req_09="";

    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if (flag==1) binding.tvFilter2.setText(sdfFormat.format(mCalRequest.getTime()));
            else binding.tvFilter3.setText(sdfFormat.format(mCalRequest.getTime()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderRequestFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.tvFilter4.setOnClickListener(this::onClickCategory);
        binding.tvFilter2.setOnClickListener(this::onClickRequestDate);
        binding.tvFilter3.setOnClickListener(this::onClickRequestDate2);
        binding.btnSave.setOnClickListener(this::onClickSave);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {
        Intent intent = getIntent();
        filterVO = (FilterVO) intent.getExtras().get("filterData");


        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
        
        binding.tvFilter4.setText("전체");
        binding.tvFilter2.setText(datePatternChange(filterVO.REQ_02_ST));
        binding.tvFilter3.setText(datePatternChange(filterVO.REQ_02_ED));


    }

    //요청일자
    private void onClickRequestDate(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        flag=1;
        datePickerDialog.show();
    }
    //요청일자
    private void onClickRequestDate2(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        flag=2;
        datePickerDialog.show();
    }
    private void onClickCategory(View v) {
        dropdownCategory();

    }
    private void dropdownCategory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle("판매유형")
                .setItems(mCategory, (dialog, which) -> {
                    binding.tvFilter4.setText(mCategory[which]);
                    req_09=mCategoryCode[which];
                }).setCancelable(false).create();

        builder.show();
    }

    private String datePatternChange(String date){
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

        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();

        mCalRequest.add(Calendar.DATE,-30);
        String tvFilter2Text = binding.tvFilter2.getText().toString().replaceAll("-","");

        mCalRequest.add(Calendar.DATE,60);
        String tvFilter3Text = binding.tvFilter3.getText().toString().replaceAll("-","");


        filterVO = new FilterVO( tvFilter2Text, tvFilter3Text,"","",req_09);
        Intent intent = new Intent();
        intent.putExtra(CATEGORY_INFO, filterVO);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void bindingCategory(ArrayList<CDO_VO> data) {
        mCategory = new String[data.size()+1];
        mCategoryCode=new String[data.size()+1];

        mCategory[0] = "전체";
        mCategoryCode[0] = "";

        for(int i=0;i<data.size();i++){
            mCategory[i+1] = data.get(i).CDO_03;
            mCategoryCode[i+1] = data.get(i).CDO_02;

            if(filterVO.REQ_09.equals(data.get(i).CDO_02))
                binding.tvFilter4.setText(data.get(i).CDO_03);
        }
        
        

    }


}
