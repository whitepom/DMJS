package kr.hmit.dmjs.ui.worker_code;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityWorkerCodeAddBinding;
import kr.hmit.dmjs.network.Http;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddWorkerCodeActivity extends BaseActivity {
    public static final int REQUEST_CODE = 2001;
    public static final String WorkerCode_ADD_INFO = "WorkerCode_ADD_INFO";
    //================================
    // region // view
    //================================

    private ActivityWorkerCodeAddBinding binding;

    //================================
    // endregion // view
    //================================
    private Calendar mCalRequest;
    private boolean flag = true;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if (flag) binding.tvJoinDate.setText(sdfFormat.format(mCalRequest.getTime()));
            else binding.tvExpireDate.setText(sdfFormat.format(mCalRequest.getTime()));
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkerCodeAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(this::onClickSave);
        binding.tvJoinDate.setOnClickListener(this::onClickRequestDate);
        binding.tvExpireDate.setOnClickListener(this::onClickRequestDate2);

        binding.chkGenderFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.chkGenderMale.setChecked(false);
                }
            }
        });
        binding.chkGenderMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.chkGenderFemale.setChecked(false);
                }
            }
        });
        binding.chkWorkTypeFullTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.chkWorkTypePartTime.setChecked(false);
                }
            }
        });
        binding.chkWorkTypePartTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    binding.chkWorkTypeFullTime.setChecked(false);
                }
            }
        });
    }

    @Override
    protected void initialize() {
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
        binding.tvJoinDate.setText(sdfFormat.format(mCalRequest.getTime()));

    }

    private void onClickSave(View view) {

        if (binding.etName.getText().toString().isEmpty()) {
            toast("성명을 입력해주세요.");
        } else if (binding.etProcess.getText().toString().isEmpty()) {
            toast("생산공정를 입력해주세요.");
        } else if (binding.etPosition.getText().toString().isEmpty()) {
            toast("직급를 입력해주세요.");
        }else if (binding.tvJoinDate.getText().toString().isEmpty()) {
            toast("입사날짜를 입력해주세요.");
        }else {
            requestSaveWOC();
        }
    }
    //요청일자
    private void onClickRequestDate(View v) {
        flag=true;
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    //요청일자
    private void onClickRequestDate2(View v) {
        flag=false;
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void requestSaveWOC() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }
        String tvTextGender = "";
        String tvTextWorkType = "";
        if(binding.chkGenderMale.isChecked()==true){
            tvTextGender ="M";
        }else if(binding.chkGenderFemale.isChecked()==true){
            tvTextGender  ="F";
        }
        if(binding.chkWorkTypeFullTime.isChecked()==true){
            tvTextWorkType ="1";
        }else if(binding.chkWorkTypePartTime.isChecked()==true){
            tvTextWorkType ="2";
        }

        openLoadingBar();
        Http.woc(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).WOC_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_INSERT",
                mUser.Value.MEM_CID,
                "",
                binding.etName.getText().toString(),
                "",
                tvTextGender,
                tvTextWorkType,
                "N",
                binding.tvJoinDate.getText().toString().replaceAll("-",""),
                "",
                binding.etResidence.getText().toString(),
                binding.etProcess.getText().toString(),
                binding.etPosition.getText().toString(),
                binding.etContact.getText().toString(),
                binding.tvExpireDate.getText().toString().replaceAll("-",""),
                binding.etEmployeeContact.getText().toString(),
                "",
                0,
                "",
                ""
        ).enqueue(new Callback<BaseModel>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                Message msg = new Message();
                msg.obj = response;
                msg.what = 100;

                //=====================
                // response callback
                //=====================
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        closeLoadingBar();
                        toast("거래처를 추가하였습니다.");
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }.sendMessage(msg);
            }


            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                call.cancel();
                closeLoadingBar();
            }
        });
    }
}