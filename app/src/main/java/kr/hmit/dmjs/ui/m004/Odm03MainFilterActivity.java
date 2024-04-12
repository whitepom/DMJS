package kr.hmit.dmjs.ui.m004;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.base.settings.SettingsKey;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityOdm03MainFilterBinding;
import kr.hmit.dmjs.databinding.ActivityReceiveFilterBinding;
import kr.hmit.dmjs.model.response.GEM_Model;
import kr.hmit.dmjs.model.vo.GEM_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.login.LoginActivity;
import kr.hmit.dmjs.ui.m004.filter.OddFilterVO;
import kr.hmit.dmjs.ui.m005.filter.LotFilterVO;
import kr.hmit.dmjs.ui.receive.model.FilterVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Odm03MainFilterActivity extends BaseActivity {
    public static final int REQUEST_CODE = 2021;
    public static final String CATEGORY_INFO = "CATEGORY_INFO";

    private ActivityOdm03MainFilterBinding binding;

    //달력선택 관련
    private DatePickerDialog datePickerDialog;
    private int DateFlag;
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());

    private OddFilterVO filterVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOdm03MainFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.gem02St.setOnClickListener(v -> onClicktvDate((TextView) v , 1));
        binding.gem02Ed.setOnClickListener(v -> onClicktvDate((TextView) v , 2));
        binding.btnSave.setOnClickListener(this::onClickSave);
    }

    @Override
    protected void initialize() {

        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();

        Intent intent = getIntent();
        filterVO = (OddFilterVO) intent.getExtras().get("OddFilterVO");

        binding.gem02St.setText(filterVO.getGEM_02_ST());
        binding.gem02Ed.setText(filterVO.getGEM_02_ED());
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
                            binding.gem02St.setText(sdfFormat.format(mCalRequest.getTime()));
                            filterVO.setGEM_02_ST(sdfFormat.format(mCalRequest.getTime()));

                            binding.gem02Ed.performClick();
                        }else if (DateFlag == 2) {
                            binding.gem02Ed.setText(sdfFormat.format(mCalRequest.getTime()));
                            filterVO.setGEM_02_ED(sdfFormat.format(mCalRequest.getTime()));
                        }
                    }
                },
                mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH),
                mCalRequest.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void onClickSave(View view) {

        Intent intent = new Intent();
        intent.putExtra(CATEGORY_INFO, filterVO);
        setResult(RESULT_OK, intent);
        finish();
    }

}
