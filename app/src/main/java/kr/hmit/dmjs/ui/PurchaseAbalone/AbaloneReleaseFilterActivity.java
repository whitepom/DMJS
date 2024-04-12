package kr.hmit.dmjs.ui.PurchaseAbalone;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.dmjs.databinding.ActivityAbaloneReleaseFilterBinding;
import kr.hmit.dmjs.databinding.ActivityOrderRequestFilterBinding;
import kr.hmit.dmjs.model.vo.CDO_VO;
import kr.hmit.dmjs.model.vo.NGO_VO;
import kr.hmit.dmjs.ui.PurchaseAbalone.vo.FilterVO2;


public class AbaloneReleaseFilterActivity extends BaseActivity{
    public static final int REQUEST_CODE = 2021;
    public static final String CATEGORY_INFO = "CATEGORY_INFO";

    private ActivityAbaloneReleaseFilterBinding binding;
    private FilterVO2 filterVO;
    private int flag = 0;
    private String[] mCategory;
    private String[] mCategoryCode;
    private String NGO_08="";

    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if (flag==1) binding.tvNGO02ST.setText(sdfFormat.format(mCalRequest.getTime()));
            else binding.tvNGO02ED.setText(sdfFormat.format(mCalRequest.getTime()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAbaloneReleaseFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.tvNGO08.setOnClickListener(this::onClickCategory);
        binding.tvNGO02ST.setOnClickListener(this::onClickRequestDate);
        binding.tvNGO02ED.setOnClickListener(this::onClickRequestDate2);
        binding.btnSave.setOnClickListener(this::onClickSave);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {
        Intent intent = getIntent();
        filterVO = (FilterVO2) intent.getExtras().get("filterData");


        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
        
        binding.tvNGO08.setText("전체");
        binding.tvNGO02ST.setText(datePatternChange(filterVO.NGO_02_ST));
        binding.tvNGO02ED.setText(datePatternChange(filterVO.NGO_02_ED));


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

        builder.setTitle("출고구분")
                .setItems(mCategory, (dialog, which) -> {
                    binding.tvNGO08.setText(mCategory[which]);
                    NGO_08=mCategoryCode[which];
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
        String NGO_02_ST = binding.tvNGO02ST.getText().toString().replaceAll("-","");

        mCalRequest.add(Calendar.DATE,60);
        String NGO_02_ED = binding.tvNGO02ED.getText().toString().replaceAll("-","");


        filterVO = new FilterVO2( NGO_02_ST, NGO_02_ED,"","",NGO_08);
        Intent intent = new Intent();
        intent.putExtra(CATEGORY_INFO, filterVO);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void bindingCategory(ArrayList<NGO_VO> data) {
        mCategory = new String[data.size()+1];
        mCategoryCode=new String[data.size()+1];

        mCategory[0] = "전체";
        mCategoryCode[0] = "";

        for(int i=0;i<data.size();i++){
            mCategory[i+1] = data.get(i).NGO_08;
            mCategoryCode[i+1] = data.get(i).NGO_08_NM;

            if(filterVO.NGO_08.equals(data.get(i).NGO_08))
                binding.tvNGO08.setText(data.get(i).NGO_08_NM);
        }
        
        

    }


}
