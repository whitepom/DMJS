package kr.hmit.dmjs.ui.m010;

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
import kr.hmit.dmjs.databinding.ActivityNgm04MainFilterBinding;
import kr.hmit.dmjs.databinding.ActivityZag01MainFilterBinding;
import kr.hmit.dmjs.ui.m010.filter.NgmFilterVO;
import kr.hmit.dmjs.ui.m017.filter.ZagFilterVO;

public class Ngm04MainFilterActivity extends BaseActivity{
    public static final int REQUEST_CODE = 2022;
    public static final String CATEGORY_INFO = "NGM_04";

    private ActivityNgm04MainFilterBinding binding;
    private NgmFilterVO filterVO = new NgmFilterVO();

    //콤보박스 관련
    //완료구분
    private LinkedHashMap<String, Object> mapNgm07 =  new LinkedHashMap();
    private String[] mNgm07 = {"- 전체 -", "미출고", "출고"};
    private String selectNgm07 ="";

    public void setNgm07Map(){
        mapNgm07.put("" ,mNgm07[0].toString());
        mapNgm07.put("N",mNgm07[1].toString());
        mapNgm07.put("Y",mNgm07[2].toString());
    }

    //달력선택 관련
    private DatePickerDialog datePickerDialog;
    private int DateFlag;
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNgm04MainFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
        initLayout();
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {
        setNgm07Map();

        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();

        Intent intent = getIntent();
        filterVO = (NgmFilterVO) intent.getExtras().get("NgmFilterVO");

        binding.ngm02St.setText(filterVO.getNGM_02_ST());
        binding.ngm02Ed.setText(filterVO.getNGM_02_ED());
    }

    @Override
    protected void initLayout() {

        binding.imgBack.setOnClickListener(v -> finish());
        binding.ngm02St.setOnClickListener(v -> onClicktvDate((TextView) v , 1));
        binding.ngm02Ed.setOnClickListener(v -> onClicktvDate((TextView) v , 2));
        binding.ngm07.setOnClickListener(v -> onClickComboZag((TextView) v , mNgm07 , "1"));

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
                            binding.ngm02St.setText(sdfFormat.format(mCalRequest.getTime()));
                            filterVO.setNGM_02_ST(sdfFormat.format(mCalRequest.getTime()));

                            binding.ngm02Ed.performClick();
                        }else if (DateFlag == 2) {
                            binding.ngm02Ed.setText(sdfFormat.format(mCalRequest.getTime()));
                            filterVO.setNGM_02_ED(sdfFormat.format(mCalRequest.getTime()));
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
            title ="출고구분";
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
            for(Map.Entry<String, Object> elem : mapNgm07.entrySet()){
                if(which == index){
                    selectNgm07 = elem.getKey();
                    break;
                }
                index++;
            }

            binding.ngm07.setText(mNgm07[which]);
            filterVO.setNGM_07(mNgm07[which]);
        }
    }

    private void onClickSearch(View view) {

        Intent intent = new Intent();
        intent.putExtra(CATEGORY_INFO, filterVO);
        setResult(RESULT_OK, intent);
        finish();
    }
}
