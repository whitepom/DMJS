package kr.hmit.dmjs.ui.m017;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityZag02SubAddBinding;
import kr.hmit.dmjs.databinding.ActivityZag02SubDetailBinding;
import kr.hmit.dmjs.model.vo.GAG_VO;
import kr.hmit.dmjs.model.vo.ZAG_VO;
import kr.hmit.dmjs.network.Http;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Zag02SubDetailActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8023;
    public static final String GAG_ADD = "GAG_ADD";

    private ActivityZag02SubDetailBinding binding;
    private GAG_VO gag_vo = new GAG_VO();

    //달력선택 관련
    private DatePickerDialog datePickerDialog;
    private int DateFlag;
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());

    //시간
    private TimePickerDialog timePickerDialog;
    private int TimeFlag;

    //파라미터
    private HashMap<String, Object> paramMap = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityZag02SubDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
        initLayout();
    }

    @Override
    protected void initialize() {
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();

        binding.gag04.setOnClickListener(v -> onClicktvDate((TextView) v , 1));
        binding.gag51.setOnClickListener(v -> onClicktvTime((TextView) v , 1));
        binding.gag52.setOnClickListener(v -> onClicktvTime((TextView) v , 2));

        binding.btnUpdate.setOnClickListener(this::onClickSave);
        binding.btnDelete.setOnClickListener(this::onClickDelete);


        Intent intent = getIntent();
        gag_vo = (GAG_VO) intent.getExtras().get("GAG_VO");

        binding.gag02.setText(String.valueOf(gag_vo.GAG_02));
        binding.gag04.setText(gag_vo.GAG_04);
        binding.gag11.setText(String.valueOf(gag_vo.GAG_11));
        binding.gag51.setText(gag_vo.GAG_51);
        binding.gag52.setText(gag_vo.GAG_52);
        binding.gag53.setText(String.valueOf(gag_vo.GAG_53));

    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
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
                            binding.gag04.setText(sdfFormat.format(mCalRequest.getTime()));
                        }
                    }
                },
                mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH),
                mCalRequest.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void onClicktvTime(TextView v , int paramTimeFlog) {

        TimeFlag = paramTimeFlog;

        timePickerDialog = new TimePickerDialog(mContext,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (TimeFlag == 1) {
                            binding.gag51.setText((hourOfDay<10?("0"+hourOfDay):hourOfDay)+":"+(minute<10?("0"+minute):minute));
                            calTime();
                        }else if (TimeFlag == 2){
                            binding.gag52.setText((hourOfDay<10?("0"+hourOfDay):hourOfDay)+":"+(minute<10?("0"+minute):minute));
                            calTime();
                        }
                    }
                },
                mCalRequest.getTime().getHours(),
                mCalRequest.getTime().getMinutes(),
                true
        );

        timePickerDialog.show();
    }

    private void calTime(){
        String startTime = binding.gag51.getText().toString();
        String endTime   = binding.gag52.getText().toString();

        int startMin = 0;
        int endMin = 0;

        if(!startTime.isEmpty() && !endTime.isEmpty()){
            String[] arryStartTime = startTime.split(":");
            String[] arryEndTime = endTime.split(":");

            startMin = (Integer.parseInt(arryStartTime[0])*60)+Integer.parseInt(arryStartTime[1]);
            endMin = (Integer.parseInt(arryEndTime[0])*60)+Integer.parseInt(arryEndTime[1]);

            int result = endMin < startMin ? (endMin + 1440) - startMin : endMin - startMin;

            binding.gag53.setText(String.valueOf(result));
        }
    }

    private void onClickSave(View view) {
        if (binding.gag04.getText().toString().isEmpty()) {
            toast("생산일자를 선택해주세요.");
        } else if (Integer.parseInt(binding.gag11.getText().toString()) < 1) {
            toast("생산수량을 입력 해주세요.");
        }else {
            GAG_U("M_ZAG_UPDATE");
        }
    }

    public void GAG_U(String GUBUN) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        paramMap = setParamMap("GAG_ID", GUBUN);

        paramMap.put("GAG_01", gag_vo.GAG_01);
        paramMap.put("GAG_02", gag_vo.GAG_02);
        paramMap.put("GAG_04", binding.gag04.getText().toString());
        paramMap.put("GAG_11", binding.gag11.getText().toString());
        paramMap.put("GAG_51", binding.gag51.getText().toString());
        paramMap.put("GAG_52", binding.gag52.getText().toString());
        paramMap.put("GAG_53", binding.gag53.getText().toString());

        Http.gag(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).GAG_U(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<GAG_VO>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<GAG_VO> call, Response<GAG_VO> response) {
                Message msg = new Message();
                msg.obj = response;
                msg.what = 100;

                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        closeLoadingBar();

                        toast("활전복 생산실적을 등록하였습니다.");

                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }.sendMessage(msg);
            }
            @Override
            public void onFailure(Call<GAG_VO> call, Throwable t) {
                call.cancel();
                closeLoadingBar();
            }
        });
    }

    //삭제버튼 클릭시
    private void onClickDelete(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("삭제 알림");
        alertDialogBuilder.setMessage("활전복 생산을 삭제하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("삭제",
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                GAG_U("DELETE");
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
}
