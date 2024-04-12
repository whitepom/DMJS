package kr.hmit.dmjs.ui.m005;

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
import java.util.LinkedHashMap;
import java.util.Map;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityLot03MainDetailBinding;
import kr.hmit.dmjs.model.vo.GAG_VO;
import kr.hmit.dmjs.network.Http;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Lot03MainDetailActivity extends BaseActivity {
    public static final int REQUEST_CODE = 17003;
    public static final String GAG_DETAIL = "GAG_DETAIL";

    private ActivityLot03MainDetailBinding binding;
    private GAG_VO gag_vo = new GAG_VO();

    //달력선택 관련
    private DatePickerDialog datePickerDialog;
    private int DateFlag;
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());

    //시간
    private TimePickerDialog timePickerDialog;
    private int TimeFlag;

    //완료구분
    private LinkedHashMap<String, Object> mapGag10 =  new LinkedHashMap();
    private String[] mGag10 = {"- 선택 -", "미완료", "완료", "진행중"};
    private String selectGag10 ="";
    public void setGag10Map(){
        mapGag10.put("" ,mGag10[0].toString());
        mapGag10.put("N",mGag10[1].toString());
        mapGag10.put("Y",mGag10[2].toString());
        mapGag10.put("Y",mGag10[3].toString());
    }

    //파라미터
    private HashMap<String, Object> paramMap = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLot03MainDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
        initLayout();
    }

    @Override
    protected void initialize() {

        setGag10Map();

        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();

        binding.imgBack.setOnClickListener(v -> finish());
        binding.gag04.setOnClickListener(v -> onClicktvDate((TextView) v , 1));
        binding.gag51.setOnClickListener(v -> onClicktvTime((TextView) v , 1));
        binding.gag52.setOnClickListener(v -> onClicktvTime((TextView) v , 2));

        binding.gag10.setOnClickListener(v -> onClickComboGag((TextView) v , mGag10 , "1"));
        binding.btnUpdate.setOnClickListener(this::onClickSave);
        binding.btnDelete.setOnClickListener(this::onClickDelete);


        Intent intent = getIntent();
        gag_vo = (GAG_VO) intent.getExtras().get("GAG_VO");

        binding.gag01.setText(String.valueOf(gag_vo.GAG_01));
        binding.gag02.setText(String.valueOf(gag_vo.GAG_02));
        binding.gag03.setText(gag_vo.GAG_03);
        binding.gag03Nm.setText(gag_vo.GAG_03_NM);
        binding.gag04.setText(gag_vo.GAG_04);
        binding.gag11.setText(String.valueOf(gag_vo.GAG_11));
        binding.gag51.setText(gag_vo.GAG_51);
        binding.gag52.setText(gag_vo.GAG_52);
        binding.gag53.setText(String.valueOf(gag_vo.GAG_53));
        binding.dah14.setText(gag_vo.DAH_14);

        int index = 0;
        //10
        for(Map.Entry<String, Object> elem : mapGag10.entrySet()){
            if(elem.getKey().equals(gag_vo.GAG_10.toString())){
                binding.gag10.setText(elem.getValue().toString());
                break;
            }
            index++;
        }
    }

    @Override
    protected void initLayout() {

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
            GAG_U("UPDATE");
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
        paramMap.put("GAG_10", gag_vo.GAG_10);
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


    private void onClickComboGag(TextView v , String[] paramArry , String comboDiv) {
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
            gag_vo.GAG_10 = mGag10[which];
        }
    }
}
