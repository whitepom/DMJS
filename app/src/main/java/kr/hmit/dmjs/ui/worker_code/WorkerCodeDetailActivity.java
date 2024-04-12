package kr.hmit.dmjs.ui.worker_code;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityWorkerCodeDetailBinding;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.UtilBox;
import kr.hmit.dmjs.ui.worker_code.model.WorkerCodeVO;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkerCodeDetailActivity extends BaseActivity {
    public static final int REQUEST_CODE = 2002;
    public static final String WORKER_CODE_DETAIL_INFO = "WORKER_CODE_DETAIL_INFO";
    //================================
    // region // view
    //================================

    private ActivityWorkerCodeDetailBinding binding;
    private WorkerCodeVO vo;
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
        binding = ActivityWorkerCodeDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.tvJoinDate.setOnClickListener(this::onClickRequestDate);
        binding.tvExpireDate.setOnClickListener(this::onClickRequestDate2);
        binding.imgBack.setOnClickListener(v -> finish());
        binding.btnDelete.setOnClickListener(this::onClickDelete);
        binding.btnUpdate.setOnClickListener(this::onClickUpdate);
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

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {

        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
        binding.tvJoinDate.setText(sdfFormat.format(mCalRequest.getTime()));

        Intent intent = getIntent();
        vo = (WorkerCodeVO)intent.getExtras().get("WOCData");
        binding.etName.setText(vo.WOC_02);
        binding.etProcess.setText(vo.WOC_09);
        binding.etPosition.setText(vo.WOC_10);
        if(vo.WOC_04.equals("M")){
            binding.chkGenderMale.setChecked(true);
            binding.chkGenderFemale.setChecked(false);
        }else if(vo.WOC_04.equals("F")){
            binding.chkGenderMale.setChecked(false);
            binding.chkGenderFemale.setChecked(true);
        }
        if(vo.WOC_05.equals("1")){
            binding.chkWorkTypeFullTime.setChecked(true);
            binding.chkWorkTypePartTime.setChecked(false);
        }else if(vo.WOC_05.equals("2")){
            binding.chkWorkTypeFullTime.setChecked(false);
            binding.chkWorkTypePartTime.setChecked(true);
        }
        binding.tvJoinDate.setText(datePatternChange(vo.WOC_0701));
        if(!vo.WOC_12.isEmpty()){
            binding.tvExpireDate.setText(datePatternChange(vo.WOC_12));
        }
        binding.etContact.setText(UtilBox.phone(vo.WOC_11));
        binding.etEmployeeContact.setText(UtilBox.phone(vo.WOC_13));
        binding.etResidence.setText(vo.WOC_08);


    }
    //요청일자
    private void onClickRequestDate(View v) {
        flag=true;
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    private void onClickRequestDate2(View v) {
        flag=false;
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }


    private void onClickDelete(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("작업자 코드 삭제");
        alertDialogBuilder.setMessage("삭제하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("삭제",
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestSaveWOC("M_DELETE");
                            }
                        }).setNegativeButton("취소",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void onClickUpdate(View view) {

        if (binding.etName.getText().toString().isEmpty()) {
            toast("성명을 입력해주세요.");
        } else if (binding.etProcess.getText().toString().isEmpty()) {
            toast("생산공정를 입력해주세요.");
        } else if (binding.etPosition.getText().toString().isEmpty()) {
            toast("직급를 입력해주세요.");
        }else if (binding.tvJoinDate.getText().toString().isEmpty()) {
            toast("입사날짜를 입력해주세요.");
        }else {
            requestSaveWOC("M_UPDATE");
        }

    }
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


    public void requestSaveWOC(String GUBUN) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }
       if(binding.chkGenderMale.isChecked()==true){
            vo.WOC_04 ="M";
        }else if(binding.chkGenderFemale.isChecked()==true){
           vo.WOC_04 ="F";
        }
        if(binding.chkWorkTypeFullTime.isChecked()==true){
            vo.WOC_05 ="1";
        }else if(binding.chkWorkTypePartTime.isChecked()==true){
            vo.WOC_05 ="2";
        }

        openLoadingBar();
        Http.woc(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).WOC_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                GUBUN,
                mUser.Value.MEM_CID,
                vo.WOC_01,
                binding.etName.getText().toString(),
                vo.WOC_03,
                vo.WOC_04,
                vo.WOC_05,
                vo.WOC_06?"Y":"N",
                binding.tvJoinDate.getText().toString().replaceAll("-",""),
                vo.WOC_0702,
                binding.etResidence.getText().toString(),
                binding.etProcess.getText().toString(),
                binding.etPosition.getText().toString(),
                binding.etContact.getText().toString().replaceAll("-",""),
                binding.tvExpireDate.getText().toString().replaceAll("-",""),
                binding.etEmployeeContact.getText().toString().replaceAll("-",""),
                vo.WOC_14,
                vo.WOC_80,
                vo.WOC_98,
                vo.WOC_99

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
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                        if(GUBUN=="M_UPDATE"){
                            toast("작업자 코드를 수정하였습니다.");
                        }else{
                            toast("작업자 코드를 삭제하였습니다.");
                        }
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