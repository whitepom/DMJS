package kr.hmit.dmjs.ui.m010;

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
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityNgm04MainAddBinding;
import kr.hmit.dmjs.databinding.ActivityNgm04MainUpdateBinding;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.model.vo.NGM_VO;
import kr.hmit.dmjs.network.Http_Ngm;
import kr.hmit.dmjs.ui.order_request.ClientNameListActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ngm04MainUpdateActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8023;
    public static final String ORDER_REQUEST_ADD = "ORDER_REQUEST_ADD";

    private ActivityNgm04MainUpdateBinding binding;

    private NGM_VO ngm_vo = new NGM_VO();

    //달력선택 관련
    private DatePickerDialog datePickerDialog;
    private int DateFlag;
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());

    //팝업
    private CLT_VO clt_vo = new CLT_VO();

    //파라미터
    private HashMap<String, Object> paramMap = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNgm04MainUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
        initLayout();
    }

    @Override
    protected void initialize() {
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();

        Intent intent = getIntent();
        NGM_VO ngm_vo = (NGM_VO) intent.getExtras().get("NGM_VO");

        NGM_Read(ngm_vo.NGM_01);
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.addCltNum.setOnClickListener(this::onClickAddClt);
        binding.ngm02.setOnClickListener(v -> onClicktvDate((TextView) v , 1));

        binding.btnUpdate.setOnClickListener(this::onClickSave);
        binding.btnDelete.setOnClickListener(this::onClickDelete);

        binding.ngm02.setText(sdfFormat.format(mCalRequest.getTime()));
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
                            binding.ngm02.setText(sdfFormat.format(mCalRequest.getTime()));
                        }
                    }
                },
                mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH),
                mCalRequest.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }



    private void onClickAddClt(View v) {
        Intent intent = new Intent(mContext, ClientNameListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        CLT_VO clt_vo = new CLT_VO();
        clt_vo.CLT_01 = binding.ngm04Nm.getText().toString();
        intent.putExtra("CLT_VO", clt_vo);
        mActivity.startActivityForResult(intent, ClientNameListActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ClientNameListActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            clt_vo = (CLT_VO) data.getSerializableExtra(ClientNameListActivity.CLIENT_LIST);
            binding.ngm04Nm.setText(clt_vo.CLT_02);
            binding.ngm04.setText(clt_vo.CLT_01);
        }
    }

    private void onClickSave(View view) {
        if (binding.ngm04.getText().toString().isEmpty()) {
            toast("출고일자를 선택해주세요.");
        } else if (binding.ngm04.getText().toString().isEmpty()) {
            toast("수매처를 선택해주세요.");
        }else {
            NGM_U("UPDATE");
        }
    }

    public void NGM_U(String GUBUN) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        paramMap = setParamMap("NGM_ID", GUBUN);

        paramMap.put("NGM_01", binding.ngm01.getText().toString());
        paramMap.put("NGM_02", binding.ngm02.getText().toString());
        paramMap.put("NGM_03", "W");
        paramMap.put("NGM_04", binding.ngm04.getText().toString());
        paramMap.put("NGM_05", "");
        paramMap.put("NGM_06", "");
        paramMap.put("NGM_07", "");
        paramMap.put("NGM_08", "");
        paramMap.put("NGM_09", "");
        paramMap.put("NGM_97", "");


        Http_Ngm.ngm(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).NGM_U(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<NGM_VO>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<NGM_VO> call, Response<NGM_VO> response) {
                Message msg = new Message();
                msg.obj = response;
                msg.what = 100;

                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        closeLoadingBar();

                        toast("출고정보를 등록하였습니다.");
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NGM_VO> call, Throwable t) {
                call.cancel();
                closeLoadingBar();
            }
        });
    }

    //삭제버튼 클릭시
    private void onClickDelete(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("삭제 알림");
        alertDialogBuilder.setMessage("출고정보를 삭제하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("삭제",
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NGM_U("DELETE");
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

    private void NGM_Read(String NGM_01) {

        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }
        String strSearch = binding.ngm04.getText().toString().toUpperCase().trim();

        //기본파라미터
        paramMap = setParamMap("NGM_ID", "M_DETAIL");
        paramMap.put("NGM_01",NGM_01);

        openLoadingBar();
        Http_Ngm.ngm(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).NGM_Read(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<ArrayList<NGM_VO>>() {
            @Override
            public void onResponse(Call<ArrayList<NGM_VO>> call, Response<ArrayList<NGM_VO>> response) {
                closeLoadingBar();
                ArrayList<NGM_VO> data = response.body();

                if (response.isSuccessful()) {
                    if(data.size() > 0){
                        bindingData(data);
                    }else{
                        //bindingData(new  ArrayList<NGM_VO>());
                        BaseAlert.show(mContext,"검색결과가 없습니다." );
                    }
                } else {
                    BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NGM_VO>> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
                t.printStackTrace();
            }
        });
    }

    private void bindingData(ArrayList<NGM_VO> data) {
        NGM_VO ngm_vo = data.get(0);

        binding.ngm01.setText(ngm_vo.NGM_01);
        binding.ngm02.setText(ngm_vo.NGM_02);
        binding.ngm04Nm.setText(ngm_vo.NGM_04_NM);
        binding.ngm04.setText(ngm_vo.NGM_04);
    }
}
