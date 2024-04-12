package kr.hmit.dmjs.ui.m017;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityOrderRequestAddBinding;
import kr.hmit.dmjs.databinding.ActivityZag01MainAddBinding;
import kr.hmit.dmjs.model.response.REQ_Model;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.model.vo.REQ_VO;
import kr.hmit.dmjs.model.vo.ZAG_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.PurchaseAbalone.PurchaseMultiAddProductActivity;
import kr.hmit.dmjs.ui.PurchaseAbalone.PurchaseMultiAddProductUpdateActivity;
import kr.hmit.dmjs.ui.PurchaseAbalone.vo.MultiItemVO;
import kr.hmit.dmjs.ui.order_request.ClientNameListActivity;
import kr.hmit.dmjs.ui.order_request.OderRequestFindProductActivity;
import kr.hmit.dmjs.ui.popup.dah.DahFindActivity;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Zag01MainAddActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8023;
    public static final String ORDER_REQUEST_ADD = "ORDER_REQUEST_ADD";

    private ActivityZag01MainAddBinding binding;

    private ZAG_VO zag_vo = new ZAG_VO();

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
        binding = ActivityZag01MainAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
        initLayout();
    }

    @Override
    protected void initialize() {
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();


    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.addProductNum.setOnClickListener(this::onClickAddProduct);
        binding.zag02.setOnClickListener(v -> onClicktvDate((TextView) v , 1));

        binding.zag08.setOnClickListener(v -> onClicktvTime((TextView) v , 1));
        binding.zag09.setOnClickListener(v -> onClicktvTime((TextView) v , 2));

        binding.btnSave.setOnClickListener(this::onClickSave);

        binding.zag02.setText(sdfFormat.format(mCalRequest.getTime()));
        binding.zag08.setText("08:00");
        binding.zag09.setText("18:00");
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
                            binding.zag02.setText(sdfFormat.format(mCalRequest.getTime()));
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
                            binding.zag08.setText((hourOfDay<10?("0"+hourOfDay):hourOfDay)+":"+(minute<10?("0"+minute):minute));
                            calTime();
                        }else if (TimeFlag == 2){
                            binding.zag09.setText((hourOfDay<10?("0"+hourOfDay):hourOfDay)+":"+(minute<10?("0"+minute):minute));
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
        dah_vo.DAH_01 = binding.zag03Nm.getText().toString();
        dah_vo.DAH_03= "R|";
        intent.putExtra("DAH_VO", dah_vo);
        mActivity.startActivityForResult(intent, DahFindActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DahFindActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            dah_vo=(ProductionInfoVO) data.getSerializableExtra(DahFindActivity.PRODUCT_LIST);

            binding.zag03.setText(dah_vo.DAH_01);
            binding.zag03Nm.setText(dah_vo.DAH_02);
            binding.dah14.setText(dah_vo.DAH_14);
        }
    }

    private void calTime(){
        String startTime =binding.zag08.getText().toString();
        String endTime =binding.zag09.getText().toString();

        int startMin = 0;
        int endMin = 0;

        if(!startTime.isEmpty() && !endTime.isEmpty()){
            String[] arryStartTime = startTime.split(":");
            String[] arryEndTime = endTime.split(":");

            startMin = (Integer.parseInt(arryStartTime[0])*60)+Integer.parseInt(arryStartTime[1]);
            endMin = (Integer.parseInt(arryEndTime[0])*60)+Integer.parseInt(arryEndTime[1]);

            int result = endMin < startMin ? (endMin + 1440) - startMin : endMin - startMin;

            binding.zag07.setText(String.valueOf(result));
        }
    }

    private void onClickSave(View view) {
        if (binding.zag02.getText().toString().isEmpty()) {
            toast("작지일자를 선택해주세요.");
        } else if (binding.zag03.getText().toString().isEmpty()) {
            toast("품번/품번을 선택해주세요.");
        }else {
            ZAG_U("INSERT");
        }
    }

    public void ZAG_U(String GUBUN) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        paramMap = setParamMap("ZAG_ID", GUBUN);

        paramMap.put("ZAG_02", binding.zag02.getText().toString());
        paramMap.put("ZAG_03", binding.zag03.getText().toString());
        paramMap.put("ZAG_04", binding.zag04.getText().toString());
        paramMap.put("ZAG_05", 0);
        paramMap.put("ZAG_07", binding.zag07.getText().toString());
        paramMap.put("ZAG_08", binding.zag08.getText().toString());
        paramMap.put("ZAG_09", binding.zag09.getText().toString());
        paramMap.put("ZAG_97", binding.zag97.getText().toString());


        Http.zag(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).ZAG_U(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<ZAG_VO>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<ZAG_VO> call, Response<ZAG_VO> response) {
                Message msg = new Message();
                msg.obj = response;
                msg.what = 100;

                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        closeLoadingBar();

                        toast("활전복 생산관리를 등록하였습니다.");


                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }.sendMessage(msg);
            }
            @Override
            public void onFailure(Call<ZAG_VO> call, Throwable t) {
                call.cancel();
                closeLoadingBar();
            }
        });
    }
}
