package kr.hmit.dmjs.ui.m005;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.dmjs.databinding.ActivityLot03MainFilterBinding;
import kr.hmit.dmjs.ui.m005.filter.GagFilterVO;

public class Lot03MainFilterActivity extends BaseActivity{
    public static final int REQUEST_CODE = 2021;
    public static final String CATEGORY_INFO = "GAG_INFO";

    private ActivityLot03MainFilterBinding binding;
    private GagFilterVO filterVO = new GagFilterVO();

    //콤보박스 관련
    //완료구분
    private LinkedHashMap<String, Object> mapGag10 =  new LinkedHashMap();;
    private String[] mGag10 = {"- 전체 -", "미완료", "완료"};
    private String selectGag10 ="";

    public void setGag10Map(){
        mapGag10.put("" ,mGag10[0].toString());
        mapGag10.put("N",mGag10[1].toString());
        mapGag10.put("Y",mGag10[2].toString());
    }

    //달력선택 관련
    private DatePickerDialog datePickerDialog;
    private int DateFlag;
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLot03MainFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
        initLayout();
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {
        setGag10Map();

        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();

        Intent intent = getIntent();
        filterVO = (GagFilterVO) intent.getExtras().get("GagFilterVO");

        binding.gag04St.setText(filterVO.getGAG_04_ST());
        binding.gag04Ed.setText(filterVO.getGAG_04_ED());
    }

    @Override
    protected void initLayout() {

        binding.imgBack.setOnClickListener(v -> finish());
        binding.gag04St.setOnClickListener(v -> onClicktvDate((TextView) v , 1));
        binding.gag04Ed.setOnClickListener(v -> onClicktvDate((TextView) v , 2));
        binding.gag10.setOnClickListener(v -> onClickComboZag((TextView) v , mGag10 , "1"));

        binding.btnSearch.setOnClickListener(this::onClickSearch);
    }

    private void onClicktvDate(TextView v , int paramDateFlog) {

        DateFlag = paramDateFlog;

        datePickerDialog = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        mCalRequest.set(Calendar.YEAR, year);
                        mCalRequest.set(Calendar.MONTH, month);
                        mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        if (DateFlag == 1) {
                            binding.gag04St.setText(sdfFormat.format(mCalRequest.getTime()));
                            filterVO.setGAG_04_ST(sdfFormat.format(mCalRequest.getTime()));

                            binding.gag04Ed.performClick();
                        }else if (DateFlag == 2) {
                            binding.gag04Ed.setText(sdfFormat.format(mCalRequest.getTime()));
                            filterVO.setGAG_04_ED(sdfFormat.format(mCalRequest.getTime()));
                        }
                    }
                },
                mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH),
                mCalRequest.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void onClickComboZag(TextView v , String[] paramArry , String comboDiv) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        String title ="";

        if(comboDiv.equals("1")){
            title ="완료구분";
        }

        builder.setTitle(title).setCancelable(true)
                .setItems(paramArry, (dialog, which) -> {
                    setArry(which , comboDiv);

                }).setCancelable(false).create();

        builder.show();
    }

    private void setArry(int which , String comboDiv) {

        int index = 0 ;
        if(comboDiv.equals("1")){
            for(Map.Entry<String, Object> elem : mapGag10.entrySet()){
                if(which == index){
                    selectGag10 = elem.getKey();
                    break;
                }
                index++;
            }

            binding.gag10.setText(mGag10[which]);
            filterVO.setGAG_10(mGag10[which]);
        }
    }

    private void onClickSearch(View view) {

        Intent intent = new Intent();
        intent.putExtra(CATEGORY_INFO, filterVO);
        setResult(RESULT_OK, intent);
        finish();
    }
}
