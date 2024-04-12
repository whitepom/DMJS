package kr.hmit.dmjs.ui.m005;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;

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
import kr.hmit.dmjs.databinding.ActivityLot01MainAddBinding;
import kr.hmit.dmjs.model.vo.LOT_VO;
import kr.hmit.dmjs.model.vo.ZAG_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.popup.dah.DahFindActivity;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Lot01MainAddActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8023;
    public static final String ORDER_REQUEST_ADD = "ORDER_REQUEST_ADD";

    private ActivityLot01MainAddBinding binding;

    private LOT_VO zag_vo = new LOT_VO();

    //라인
    private LinkedHashMap<String, Object> mapLot10 =  new LinkedHashMap();
    private String[] mLot11 = {"- 선택 -", "냉동전복라인", "가공전복라인", "기타생산라인"};
    private String selectLot11 ="";

    public void setLot11Map(){
        mapLot10.put("" ,mLot11[0].toString());
        mapLot10.put("1",mLot11[1].toString());
        mapLot10.put("2",mLot11[2].toString());
        mapLot10.put("3",mLot11[3].toString());
    }

    //달력선택 관련
    private DatePickerDialog datePickerDialog;
    private int DateFlag;
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());

    
    //시간
    private TimePickerDialog timePickerDialog;
    
    //제품팝업
    private ProductionInfoVO dah_vo;
    private int TimeFlag;

    //파라미터
    private HashMap<String, Object> paramMap = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLot01MainAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
        initLayout();
    }

    @Override
    protected void initialize() {
        setLot11Map();

        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.addProductNum.setOnClickListener(this::onClickAddProduct);
        binding.lot02.setOnClickListener(v -> onClicktvDate((TextView) v , 1));

        binding.lot1501.setOnClickListener(v -> onClicktvTime((TextView) v , 1));
        binding.lot1502.setOnClickListener(v -> onClicktvTime((TextView) v , 2));

        binding.lot11.setOnClickListener(v -> onClickComboLot((TextView) v , mLot11 , "1"));

        binding.btnUpdate.setOnClickListener(this::onClickSave);

        binding.lot1501.setText("08:00");
        binding.lot1502.setText("18:00");
        calTime();
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
                            binding.lot02.setText(sdfFormat.format(mCalRequest.getTime()));
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
                            binding.lot1501.setText((hourOfDay<10?("0"+hourOfDay):hourOfDay)+":"+(minute<10?("0"+minute):minute));
                            calTime();
                        }else if (TimeFlag == 2){
                            binding.lot1502.setText((hourOfDay<10?("0"+hourOfDay):hourOfDay)+":"+(minute<10?("0"+minute):minute));
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

    private void onClickAddProduct(View v) {
        Intent intent = new Intent(mContext, DahFindActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        ProductionInfoVO dah_vo = new ProductionInfoVO();
        dah_vo.DAH_01 = binding.lot04Nm.getText().toString();
        dah_vo.DAH_03= "P|";
        intent.putExtra("DAH_VO", dah_vo);
        mActivity.startActivityForResult(intent, DahFindActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DahFindActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            dah_vo=(ProductionInfoVO) data.getSerializableExtra(DahFindActivity.PRODUCT_LIST);

            binding.lot04.setText(dah_vo.DAH_01);
            binding.lot04Nm.setText(dah_vo.DAH_02);
            binding.dah14.setText(dah_vo.DAH_14);
        }
    }

    private void calTime(){
        String startTime =binding.lot1501.getText().toString();
        String endTime =binding.lot1502.getText().toString();

        int startMin = 0;
        int endMin = 0;

        if(!startTime.isEmpty() && !endTime.isEmpty()){
            String[] arryStartTime = startTime.split(":");
            String[] arryEndTime = endTime.split(":");

            startMin = (Integer.parseInt(arryStartTime[0])*60)+Integer.parseInt(arryStartTime[1]);
            endMin = (Integer.parseInt(arryEndTime[0])*60)+Integer.parseInt(arryEndTime[1]);

            int result = endMin < startMin ? (endMin + 1440) - startMin : endMin - startMin;

            binding.lot1503.setText(String.valueOf(result));
        }
    }

    private void onClickSave(View view) {
        if (binding.lot02.getText().toString().isEmpty()) {
            toast("작지일자를 선택해주세요.");
        } else if (binding.lot04.getText().toString().isEmpty()) {
            toast("품번/품번을 선택해주세요.");
        }else {
            LOT_U("INSERT");
        }
    }

    public void LOT_U(String GUBUN) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        paramMap = setParamMap("LOT_ID", GUBUN);

        paramMap.put("LOT_02", binding.lot02.getText().toString());
        paramMap.put("LOT_04", binding.lot04.getText().toString());
        paramMap.put("LOT_06", binding.lot06.getText().toString());
        paramMap.put("LOT_07", 0);
        paramMap.put("LOT_11", selectLot11);
        paramMap.put("LOT_1503", binding.lot1503.getText().toString());
        paramMap.put("LOT_1501", binding.lot1501.getText().toString());
        paramMap.put("LOT_1502", binding.lot1502.getText().toString());
        paramMap.put("LOT_97", binding.lot97.getText().toString());


        Http.lot(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).LOT_U(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<LOT_VO>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<LOT_VO> call, Response<LOT_VO> response) {
                Message msg = new Message();
                msg.obj = response;
                msg.what = 100;

                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        closeLoadingBar();
                        toast("가공생산관리를 등록하였습니다.");
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }.sendMessage(msg);
            }
            @Override
            public void onFailure(Call<LOT_VO> call, Throwable t) {
                call.cancel();
                closeLoadingBar();
            }
        });
    }

    private void onClickComboLot(TextView v , String[] paramArry , String comboDiv) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        String title ="";

        if(comboDiv.equals("1")){
            title ="생산라인";
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
                    selectLot11 = elem.getKey();
                    break;
                }
                index++;
            }
            binding.lot11.setText(mLot11[which]);
        }
    }
}
