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
import kr.hmit.dmjs.databinding.ActivityLot01MainDetailBinding;
import kr.hmit.dmjs.model.vo.LOT_VO;
import kr.hmit.dmjs.model.vo.ZAG_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.popup.dah.DahFindActivity;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Lot01MainDetailActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8023;
    public static final String ORDER_REQUEST_ADD = "ORDER_REQUEST_ADD";

    private ActivityLot01MainDetailBinding binding;

    private LOT_VO lot_vo = new LOT_VO();

    //라인
    private LinkedHashMap<String, Object> mapLot11 =  new LinkedHashMap();
    private LinkedHashMap<String, Object> mapLot18 =  new LinkedHashMap();
    private String[] mLot11 = {"- 선택 -", "냉동전복라인", "가공전복라인", "기타생산라인"};
    private String[] mLot18 = {"- 선택 -", "완료", "미완료", "진행중"};
    private String selectLot11 ="";
    private String selectLot18 ="";
    public void setLot11Map(){
        mapLot11.put("" ,mLot11[0].toString());
        mapLot11.put("1",mLot11[1].toString());
        mapLot11.put("2",mLot11[2].toString());
        mapLot11.put("3",mLot11[3].toString());
    }

    public void setLot18Map(){
        mapLot18.put("" ,mLot18[0].toString());
        mapLot18.put("Y",mLot18[1].toString());
        mapLot18.put("N",mLot18[2].toString());
        mapLot18.put("S",mLot18[3].toString());
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
        binding = ActivityLot01MainDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
        initLayout();
    }

    @Override
    protected void initialize() {

        setLot11Map();
        setLot18Map();

        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();

        Intent intent = getIntent();
        lot_vo = (LOT_VO) intent.getExtras().get("LOT_VO");

        binding.lot01.setText(lot_vo.LOT_01);
        binding.lot02.setText(lot_vo.LOT_02);
        binding.lot04.setText(lot_vo.LOT_04);
        binding.lot04Nm.setText(lot_vo.LOT_04_NM);
        binding.lot06.setText(String.valueOf(lot_vo.LOT_06));
        binding.lot1503.setText(String.valueOf(lot_vo.LOT_1503));
        binding.dah14.setText(String.valueOf(lot_vo.DAH_14));
        binding.lot1501.setText(lot_vo.LOT_1501);
        binding.lot1502.setText(lot_vo.LOT_1502);
        binding.lot97.setText(lot_vo.LOT_97);


        if(lot_vo.LOT_11 != null) {
            for (Map.Entry<String, Object> elem : mapLot11.entrySet()) {
                if (lot_vo.LOT_11.equals(elem.getKey())) {
                    binding.lot11.setText(elem.getValue().toString());
                    break;
                }
            }
        }
        if(lot_vo.LOT_18 != null) {
            for(Map.Entry<String, Object> elem : mapLot18.entrySet()){
                if(lot_vo.LOT_18.equals(elem.getKey())){
                    binding.lot18.setText(elem.getValue().toString());
                    break;
               }
            }
        }
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.addProductNum.setOnClickListener(this::onClickAddProduct);
        binding.lot02.setOnClickListener(v -> onClicktvDate((TextView) v , 1));

        binding.lot1501.setOnClickListener(v -> onClicktvTime((TextView) v , 1));
        binding.lot1502.setOnClickListener(v -> onClicktvTime((TextView) v , 2));

        binding.lot11.setOnClickListener(v -> onClickComboLot((TextView) v , mLot11 , "1"));
        binding.lot18.setOnClickListener(v -> onClickComboLot((TextView) v , mLot18 , "2"));

        binding.btnUpdate.setOnClickListener(this::onClickSave);
        binding.btnDelete.setOnClickListener(this::onClickDelete);

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
        dah_vo.DAH_03= "R|";
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
            LOT_U("UPDATE");
        }
    }

    public void LOT_U(String GUBUN) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        paramMap = setParamMap("LOT_ID", GUBUN);

        paramMap.put("LOT_01", lot_vo.LOT_01);
        paramMap.put("LOT_02", binding.lot02.getText().toString());
        paramMap.put("LOT_04", binding.lot04.getText().toString());
        paramMap.put("LOT_06", binding.lot06.getText().toString());
        paramMap.put("LOT_07", 0);
        paramMap.put("LOT_11", selectLot11);
        paramMap.put("LOT_1503", binding.lot1503.getText().toString());
        paramMap.put("LOT_1501", binding.lot1501.getText().toString());
        paramMap.put("LOT_1502", binding.lot1502.getText().toString());
        paramMap.put("LOT_18", selectLot18);
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

                        if(GUBUN.equals("UPDATE")) {
                            toast("가공생산관리를 수정하였습니다.");
                        }else if(GUBUN.equals("DELETE")) {
                            toast("가공생산관리를 삭제하였습니다.");
                        }

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

    //삭제버튼 클릭시
    private void onClickDelete(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("삭제 알림");
        alertDialogBuilder.setMessage("작업지시를 삭제하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("삭제",
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LOT_U("DELETE");
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

    private void onClickComboLot(TextView v , String[] paramArry , String comboDiv) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        String title ="";

        if(comboDiv.equals("1")){
            title ="생산라인";
        }else if(comboDiv.equals("1")){
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
            for(Map.Entry<String, Object> elem : mapLot11.entrySet()){
                if(which == index){
                    selectLot11 = elem.getKey();
                    break;
                }
                index++;
            }
            binding.lot11.setText(mLot11[which]);
        }
        if(comboDiv.equals("2")){
            for(Map.Entry<String, Object> elem : mapLot18.entrySet()){
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
