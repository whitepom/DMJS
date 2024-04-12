package kr.hmit.dmjs.ui.release;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kr.hmit.base.settings.SettingsKey;
import kr.hmit.dmjs.R;

import kr.hmit.dmjs.databinding.ActivityReleaseDetailBinding;
import kr.hmit.dmjs.model.response.CLT_Model;
import kr.hmit.dmjs.model.response.RUM_Model;
import kr.hmit.dmjs.model.response.RUN_Model;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.model.vo.ODM_VO;
import kr.hmit.dmjs.model.vo.RUM_VO;
import kr.hmit.dmjs.model.vo.RUN_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.login.LoginActivity;
import kr.hmit.dmjs.ui.order_request.ClientNameListActivity;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReleaseDetailActivity extends BaseActivity  {
    public static final int REQUEST_CODE = 8024;
    public static final String OrderManagement_detail = "OrderManagement_detail";
    private String[] mCategory1;
    private ActivityReleaseDetailBinding binding;
    private CLT_VO clt_vo;
    private RUM_VO rumVO;
    private String check;
    private Calendar mCalRequest;
    private String RUM_06_;
    private String[] RUM_06;
    private String[] CLT_01;
    private String[] mCategory2;
    private String CLT_01_;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            binding.tvDate.setText(sdfFormat.format(mCalRequest.getTime()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReleaseDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        rumVO = (RUM_VO) intent.getExtras().get("rumVO");
        CLT_01_=rumVO.RUN_02;
        RUM_06_=rumVO.RUN_02;
        if(rumVO.equals("Y"))
        {check="Y";}
        else{check="N";}


        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.tvDate.setOnClickListener(this::onClickDate);
        binding.addReceiverName.setOnClickListener(this::onClickAddClient);
        binding.btnSave.setOnClickListener(this::onClickUpdate);
        binding.btnDelete.setOnClickListener(this::onClickDelete);
        binding.radioGroup.setOnCheckedChangeListener(this::onClickChangeRadioButton);
        binding.tvAddress.setText(rumVO.RUN_02); // 주소
        binding.tvRUM01.setText(rumVO.RUN_02);
        binding.tvCLT02.setText(rumVO.CLT_02);
        binding.tvRUM06.setOnClickListener(this::onClickCategory2);
        binding.tvRUM06.setText(rumVO.RUN_02);
        binding.tvOrderName1.setOnClickListener(this::onClickCategory1);
        binding.tvOrderName1.setText(rumVO.RUN_02);
        binding.tvCLT01.setText(rumVO.RUN_02);
        binding.tvDate.setText(datePatternChange(rumVO.RUN_02)); // 출고일
        binding.tvCLT20.setText(rumVO.RUN_02); //

    }
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
        requestCD();
        switch(rumVO.RUN_02){
            case "Y" :
                binding.radioCLT.setChecked(true); //거래처주문
                binding.tvOrderName1.setEnabled(false);
                binding.tvTextFilter5.setTextColor(Color.parseColor("#BDBDBD"));
                binding.tvOrderName1.setTextColor(Color.parseColor("#BDBDBD"));
                binding.tvOrderName1.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_round5_border2));

                break;
            case "N":
                binding.radioCLT2.setChecked(true); // 개인주문자
                binding.tvOrderName1.setEnabled(true);
                binding.tvTextFilter5.setTextColor(Color.parseColor("#616161"));
                binding.tvOrderName1.setTextColor(Color.parseColor("#616161"));
                binding.tvOrderName1.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_round5_border1));
                requestCLTA();
                break;
        }

    }


    private void onClickAddClient(View v) {
        Intent intent = new Intent(mContext, ClientNameListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, ClientNameListActivity.REQUEST_CODE);
    }

    private void onClickUpdate(View view) {
        if (binding.tvDate.getText().toString().isEmpty()) {
            toast("출고일을 선택해주세요.");
        } else if (binding.tvCLT01.getText().toString().isEmpty()) {
            toast("주문처를 선택해주세요.");
        }else {
            rumVO.RUN_02=binding.tvDate.getText().toString().replaceAll("-","");
            rumVO.RUN_02=binding.tvCLT01.getText().toString();
            rumVO.CLT_02=binding.tvCLT02.getText().toString();

            requestSaveRUM("M_UPDATE");
            //    requestSaveRUM("ASDF");
        }
    }
    private void onClickDelete(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("발주상세정보 삭제");
        alertDialogBuilder.setMessage("삭제하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("삭제",
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestSaveRUM("M_DELETE");
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
    private void onClickCategory1(View v) {
        if (mCategory1 != null && mCategory1.length == 1) {
            requestCLTA();
        }

        dropdownCategory("주 문 자",mCategory1,binding.tvOrderName1);
    }

    private void requestCD() { // 계좌코드 = 관리구분
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.run(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).RUM_CD(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_RUM_CODE"
        ).enqueue(new Callback<RUM_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<RUM_Model> call, Response<RUM_Model> response) {
                Message msg = new Message();
                msg.obj = response;
                msg.what = 100;

                //=====================
                // response callback
                //=====================
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 100) {
                            closeLoadingBar();

                            Response<RUM_Model> response = (Response<RUM_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if (response.body().Data.get(0).Validation) {
                                        bindingCategory2(response.body().Data);
                                    } else {
                                        toast("동일계정 접속 > 로그인 페이지로 이동합니다");  // asdfasdfasdf
                                        mSettings.putBooleanItem(SettingsKey.AutoLogin,false);
                                        Intent intent = new Intent(mContext, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("simultaneous_connect",mUser.Value.MEM_01.toString());
                                        startActivity(intent);
                                        finish();
                                        //BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                }else{
                                    bindingCategory2(new ArrayList<RUM_VO>());
                                }
                            } else {
                                BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<RUM_Model> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
            }
        });
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
    private void bindingCategory2(ArrayList<RUM_VO> data) { //CD = RUM_06, CD_NM  RUM_06_NM
        mCategory2 = new String[data.size() + 1];
        RUM_06=new String[data.size()+1];
        RUM_06[0]="";
        mCategory2[0] = "전체";
        for (int i = 0; i < data.size(); i++) {
            mCategory2[i + 1] = data.get(i).RUN_02;
            RUM_06[i+1]=data.get(i).RUN_02;
        }

    }
    public void requestSaveRUM(String GUBUN) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();
        Http.run(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).RUM_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                GUBUN,
                mUser.Value.MEM_CID,
                rumVO.RUN_03,
                binding.tvCLT01.getText().toString(),
                binding.tvDate.getText().toString().replaceAll("-",""),
                CLT_01_,
                binding.tvAddress.getText().toString(),
                RUM_06_,
                0, //이 부분들은 업데이트 안되도록
                0,
                0,
                check,
                binding.tvCLT20.getText().toString(),
                mUser.Value.MEM_01
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
                        if(GUBUN.equals("M_UPDATE")){
                            toast("출고상세정보를 수정하였습니다.");
                        }else{
                            toast("출고상세정보를 삭제하였습니다.");
                        }
                        Intent intent = new Intent();
                        intent.putExtra("rumVO", rumVO);
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
    /**
     * 요청일자 클릭
     *
     * @param v
     */
    private void onClickDate(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ClientNameListActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            binding.tvOrderName1.setText("선택");
            CLT_01_="";
            clt_vo = (CLT_VO) data.getSerializableExtra(ClientNameListActivity.CLIENT_LIST);
            binding.tvCLT02.setText(clt_vo.CLT_02);
            binding.tvCLT01.setText(clt_vo.CLT_01);
            binding.tvCLT20.setText(clt_vo.CLT_20);
            binding.tvAddress.setText("("+clt_vo.CLT_1001+")"+" "+clt_vo.CLT_1002+clt_vo.CLT_1003);
            if(check.equals("N")){
                requestCLTA();
            }

        }
    }

    private void onClickChangeRadioButton(RadioGroup radioGroup, int i){

        // 여기에 binding색깔 바꿔주고 clickable 세팅해주는 코드
        if(i==binding.radioCLT2.getId()){
            requestCLTA(); // 주문자
            check="N";
            binding.tvOrderName1.setEnabled(true);
            binding.tvTextFilter5.setTextColor(Color.parseColor("#616161"));
            binding.tvOrderName1.setTextColor(Color.parseColor("#616161"));
            binding.tvOrderName1.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_round5_border1));
        }
        else {
            binding.tvOrderName1.setText("선택");
            CLT_01_="";
            check="Y";
            binding.tvOrderName1.setEnabled(false);
            binding.tvTextFilter5.setTextColor(Color.parseColor("#BDBDBD"));
            binding.tvOrderName1.setTextColor(Color.parseColor("#BDBDBD"));
            binding.tvOrderName1.setBackground(ContextCompat.getDrawable(this,R.drawable.shape_round5_border2));

        }

    }
    private void onClickCategory2(View v) {
        if (mCategory2 != null && mCategory2.length == 1) {
            requestCD();
        }
        dropdownCategory2("관리구분",mCategory2,binding.tvRUM06);
    }

    private void dropdownCategory2(String dialogTitle, String[] category, TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(dialogTitle)
                .setItems(category, (dialog, which) -> {
                    textView.setText(category[which]);
                    RUM_06_=RUM_06[which];
                }).setCancelable(false).create();

        builder.show();
    }
    private void requestCLTA() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.clt(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).CLTA_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_COMBOLIST",
                "BADAMS",
                binding.tvCLT01.getText().toString()
        ).enqueue(new Callback<CLT_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<CLT_Model> call, Response<CLT_Model> response) {
                Message msg = new Message();
                msg.obj = response;
                msg.what = 100;

                //=====================
                // response callback
                //=====================
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 100) {
                            closeLoadingBar();

                            Response<CLT_Model> response = (Response<CLT_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if (response.body().Data.get(0).Validation) {
                                        bindingCategory(response.body().Data);
                                    } else {
                                        toast("동일계정 접속 > 로그인 페이지로 이동합니다");  // asdfasdfasdf
                                        mSettings.putBooleanItem(SettingsKey.AutoLogin,false);
                                        Intent intent = new Intent(mContext, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("simultaneous_connect",mUser.Value.MEM_01.toString());
                                        startActivity(intent);
                                        finish();
                                        //BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                }else{
                                    bindingCategory(new ArrayList<CLT_VO>());
                                }
                            } else {
                                BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<CLT_Model> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
            }
        });
    }


    private void dropdownCategory(String dialogTitle, String[] category, TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(dialogTitle)
                .setItems(category, (dialog, which) -> {
                    textView.setText(category[which]);
                    CLT_01_=CLT_01[which];
                }).setCancelable(false).create();

        builder.show();
    }
    private void bindingCategory(ArrayList<CLT_VO> data) {
        mCategory1 = new String[data.size() + 1];
        CLT_01=new String[data.size()+1];
        CLT_01[0]="";
        mCategory1[0] = "전체";
        for (int i = 0; i < data.size(); i++) {
            mCategory1[i + 1] = data.get(i).CLT_01;
            CLT_01[i+1]=data.get(i).CLT_02;
        }

    }
}
