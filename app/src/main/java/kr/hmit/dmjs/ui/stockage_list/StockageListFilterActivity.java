package kr.hmit.dmjs.ui.stockage_list;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kr.hmit.dmjs.databinding.ActivityStockageListFilterBinding;
import kr.hmit.dmjs.ui.stockage_list.model.FilterVO;
import kr.hmit.base.base_activity.BaseActivity;

public class StockageListFilterActivity extends BaseActivity {
    public static final int REQUEST_CODE = 2021;
    public static final String CATEGORY_INFO = "CATEGORY_INFO";

    private ActivityStockageListFilterBinding binding;
    private String[] mCategory2;
    private FilterVO filterVO;
    private int flag = 0;

    private String[] mProductList;
    private String[] mProductCodeList;
    private String dah_03="";

    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if (flag==1){
                mCalRequest.set(Calendar.DAY_OF_MONTH,1);
                binding.tvFilter4.setText(sdfFormat.format(mCalRequest.getTime()));
            }
            else binding.tvFilter5.setText(sdfFormat.format(mCalRequest.getTime()));
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStockageListFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.tvFilter1.setOnClickListener(this::onClickCategory1);
        //binding.tvFilter2.setOnClickListener(this::onClickCategory);
        binding.tvFilter4.setOnClickListener(this::onClickRequestDate);
        binding.tvFilter5.setOnClickListener(this::onClickRequestDate2);
        binding.btnSave.setOnClickListener(this::onClickSave);
    }
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {
        mCategory2= new String[]{"전체","원재료","부재료","반제품","완제품"};

        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();

        Intent intent = getIntent();
        filterVO = (FilterVO) intent.getExtras().get("filterData");

        binding.tvFilter1.setText(getCategory(filterVO.OOK_04));

        binding.tvFilter4.setText(datePatternChange(filterVO.OOK_02_ST));
        binding.tvFilter5.setText(datePatternChange(filterVO.OOK_02_ED));

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
    private void onClickCategory1(View v) {
        dropdownCategory("자재구분",mCategory2,binding.tvFilter1);
    }
   /* private void onClickCategory(View v) {

        if (mProductList != null && mProductList.length == 1) {

        } else {
            dropdownCategory("제품분류",mProductList,binding.tvFilter2);
        }
    }*/

    private void dropdownCategory(String dialogTitle, String[] category, TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(dialogTitle)
                .setItems(category, (dialog, which) -> {
                    textView.setText(category[which]);
                    if(dialogTitle.equals("자재구분"))
                        dah_03=getCategory(category[which]);
                }).setCancelable(false).create();

        builder.show();
    }

    private String changeCDO03(String args){
       String DAH_03="";
            switch (args) {
                case "전체":
                default:
                    DAH_03 = "";
                    break;
                case "원재료":
                    DAH_03 = "R";
                    break;
                case "반제품":
                    DAH_03 = "S";
                    break;
                case "완제품":
                    DAH_03 = "P";
                    break;
                case "부재료":
                    DAH_03 = "B";
                    break;
            }
            return DAH_03;
    }
    private void onClickSave(View view) {
        String tvFilter1Text = binding.tvFilter1.getText().toString();

        String tvFilter4Text = binding.tvFilter4.getText().toString().replaceAll("-","");
        String tvFilter5Text = binding.tvFilter5.getText().toString().replaceAll("-","");


        //filterVO = new FilterVO(getCategory(tvFilter1Text), dah_07, tvFilter4Text, tvFilter5Text);
        filterVO = new FilterVO(dah_03, tvFilter4Text, tvFilter5Text); // DAH_07
        Intent intent = new Intent();
        intent.putExtra(CATEGORY_INFO, filterVO);
        setResult(RESULT_OK, intent);
        finish();
    }


   /* private void bindingCategory(ArrayList<CDO_VO> data) {
        mProductList = new String[data.size()+1];
        mProductCodeList=new String[data.size()+1];
        mProductList[0]="전체";
        mProductCodeList[0]="";
        for(int i=0;i<data.size();i++){
            mProductList[i+1] = data.get(i).CDO_03;
            mProductCodeList[i+1] = data.get(i).CDO_02;

            if(filterVO.CDO_03.equals(data.get(i).CDO_02))
                binding.tvFilter2.setText(data.get(i).CDO_03);

        }

    }*/

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



    public static String getCategory(String src){
        switch (src.trim()){
            case "원재료" : return "R";
            case "부재료" : return "B";
            case "반제품" : return "S";
            case "완제품" : return "P";
            case "전체" :return "";
            case "R" : return "원재료";
            case "B" : return "부재료";
            case "S" : return "반제품";
            case "P" : return "완제품";
            case "" :return "전체";
        }
        return "";
    }

}
