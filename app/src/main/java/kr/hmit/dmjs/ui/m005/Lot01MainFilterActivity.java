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
import kr.hmit.dmjs.databinding.ActivityLot01MainFilterBinding;
import kr.hmit.dmjs.databinding.ActivityLot01MainFilterBinding;
import kr.hmit.dmjs.ui.m005.filter.LotFilterVO;
import kr.hmit.dmjs.ui.m005.filter.LotFilterVO;

public class Lot01MainFilterActivity extends BaseActivity{
    public static final int REQUEST_CODE = 2022;
    public static final String CATEGORY_INFO = "LOT_01";

    private ActivityLot01MainFilterBinding binding;
    private LotFilterVO filterVO = new LotFilterVO();

    //콤보박스 관련
    //완료구분
    private LinkedHashMap<String, Object> mapLot10 =  new LinkedHashMap();
    private String[] mLot18 = {"- 전체 -", "미완료", "완료"};
    private String selectLot10 ="";

    public void setLot10Map(){
        mapLot10.put("" ,mLot18[0].toString());
        mapLot10.put("N",mLot18[1].toString());
        mapLot10.put("Y",mLot18[2].toString());
    }

    //달력선택 관련
    private DatePickerDialog datePickerDialog;
    private int DateFlag;
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLot01MainFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
        initLayout();
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {
        setLot10Map();

        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();

        Intent intent = getIntent();
        filterVO = (LotFilterVO) intent.getExtras().get("LotFilterVO");

        binding.zag02St.setText(filterVO.getLOT_02_ST());
        binding.zag02Ed.setText(filterVO.getLOT_02_ED());
    }

    @Override
    protected void initLayout() {

        binding.imgBack.setOnClickListener(v -> finish());
        binding.zag02St.setOnClickListener(v -> onClicktvDate((TextView) v , 1));
        binding.zag02Ed.setOnClickListener(v -> onClicktvDate((TextView) v , 2));
        binding.lot18.setOnClickListener(v -> onClickComboLot((TextView) v , mLot18 , "1"));

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
                            binding.zag02St.setText(sdfFormat.format(mCalRequest.getTime()));
                            filterVO.setLOT_02_ST(sdfFormat.format(mCalRequest.getTime()));

                            binding.zag02Ed.performClick();
                        }else if (DateFlag == 2) {
                            binding.zag02Ed.setText(sdfFormat.format(mCalRequest.getTime()));
                            filterVO.setLOT_02_ED(sdfFormat.format(mCalRequest.getTime()));
                        }
                    }
                },
                mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH),
                mCalRequest.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void onClickComboLot(TextView v , String[] paramArry , String comboDiv) {
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
            for(Map.Entry<String, Object> elem : mapLot10.entrySet()){
                if(which == index){
                    selectLot10 = elem.getKey();
                    break;
                }
                index++;
            }

            binding.lot18.setText(mLot18[which]);
            filterVO.setLOT_18(mLot18[which]);
        }
    }

    private void onClickSearch(View view) {

        Intent intent = new Intent();
        intent.putExtra(CATEGORY_INFO, filterVO);
        setResult(RESULT_OK, intent);
        finish();
    }
}
