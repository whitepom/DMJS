package kr.hmit.dmjs.ui.popup.run;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.dmjs.databinding.ActivityRunFindFilterBinding;
import kr.hmit.dmjs.ui.popup.run.model.RunFilterVO;

public class RunFindFilterActivity extends BaseActivity{
    public static final int REQUEST_CODE = 2021;
    public static final String CATEGORY_INFO = "ZAG_01";

    private ActivityRunFindFilterBinding binding;
    private RunFilterVO filterVO = new RunFilterVO();

    //달력선택 관련
    private DatePickerDialog datePickerDialog;
    private int DateFlag;
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRunFindFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
        initLayout();
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {

        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();

        Intent intent = getIntent();
        filterVO = (RunFilterVO) intent.getExtras().get("RunFilterVo");

        binding.run02St.setText(filterVO.getRUN_02_ST());
        binding.run02Ed.setText(filterVO.getRUN_02_ED());
    }

    @Override
    protected void initLayout() {

        binding.imgBack.setOnClickListener(v -> finish());
        binding.run02St.setOnClickListener(v -> onClicktvDate((TextView) v , 1));
        binding.run02Ed.setOnClickListener(v -> onClicktvDate((TextView) v , 2));

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
                            binding.run02St.setText(sdfFormat.format(mCalRequest.getTime()));
                            filterVO.setRUN_02_ST(sdfFormat.format(mCalRequest.getTime()));

                            binding.run02Ed.performClick();
                        }else if (DateFlag == 2) {
                            binding.run02Ed.setText(sdfFormat.format(mCalRequest.getTime()));
                            filterVO.setRUN_02_ED(sdfFormat.format(mCalRequest.getTime()));
                        }
                    }
                },
                mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH),
                mCalRequest.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void onClickSearch(View view) {

        Intent intent = new Intent();
        intent.putExtra(CATEGORY_INFO, filterVO);
        setResult(RESULT_OK, intent);
        finish();
    }
}
