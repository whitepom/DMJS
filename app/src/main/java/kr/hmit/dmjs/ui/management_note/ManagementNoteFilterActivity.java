package kr.hmit.dmjs.ui.management_note;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.dmjs.databinding.ActivityManagementNoteFilterBinding;
import kr.hmit.dmjs.ui.management_note.model.FilterVO;
import kr.hmit.dmjs.model.vo.LED_VO;

public class ManagementNoteFilterActivity extends BaseActivity {
    public static final int REQUEST_CODE = 2021;
    public static final String CATEGORY_INFO = "CATEGORY_INFO";
    private FilterVO filterVO;
    private LED_VO LED_VO;

    private ActivityManagementNoteFilterBinding binding;
    private int flag = 0;
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if (flag==1) binding.tvFilter.setText(sdfFormat.format(mCalRequest.getTime()));
            else binding.tvFilter2.setText(sdfFormat.format(mCalRequest.getTime()));
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManagementNoteFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();

    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.tvFilter.setOnClickListener(this::onClickRequestDate);
        binding.tvFilter2.setOnClickListener(this::onClickRequestDate2);
        binding.btnSave.setOnClickListener(this::onClickSave);
    }


    @Override
    protected void initialize() {

        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();

        Intent intent = getIntent();
        filterVO = (FilterVO) intent.getExtras().get("filterData");

        binding.tvFilter.setText(filterVO.LED_1301);
        binding.tvFilter2.setText(filterVO.LED_1302);




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





    private void onClickSave(View view) {

        String LED_1301 = binding.tvFilter.getText().toString();
        String LED_1302 = binding.tvFilter2.getText().toString();
        String LED_11 = "";

        if(binding.BtnComplte.isChecked()==true){
            LED_11 = "1";
        }else if(binding.BtnunComplte.isChecked()==true){
            LED_11 = "0";
        }

        filterVO = new FilterVO("",LED_11, LED_1301,LED_1302);
        Intent intent = new Intent();
        intent.putExtra(CATEGORY_INFO, filterVO);
        setResult(RESULT_OK, intent);
        finish();

    }

}
