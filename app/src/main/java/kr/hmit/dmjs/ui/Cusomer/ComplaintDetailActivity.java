package kr.hmit.dmjs.ui.Cusomer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityComplaintDetailBinding;
import kr.hmit.dmjs.databinding.ActivityNoticeDetailBinding;
import kr.hmit.dmjs.model.response.GCM_Model;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.model.vo.GCM_VO;
import kr.hmit.dmjs.model.vo.MEM_VO;
import kr.hmit.dmjs.model.vo.REM_VO;
import kr.hmit.dmjs.model.vo.RUN2_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.Cusomer.Popups.MemberListActivity;
import kr.hmit.dmjs.ui.order_request.ClientNameListActivity;
import kr.hmit.dmjs.ui.popup.run.RunFindActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComplaintDetailActivity extends BaseActivity {
    public static final int REQUEST_CODE = 2021;
    public String DecodeContent = "";
    //===============================
    // region // view
    //===============================
    private ActivityComplaintDetailBinding binding;
    private String[] mCategory;

    private CLT_VO cltVO;
    private REM_VO remVO;
    private RUN2_VO run2Vo;
    public MEM_VO memVO;

    private String[] GCM04_List;
    private String[] GCM04NM_List;
    private String[] GCM05_List;
    private String[] GCM05NM_List;
    private String[] GCM06_List;
    private String[] GCM06NM_List;
    private String GCM04;
    private String GCM05;
    private String GCM06;

    private GCM_VO GCMdata = new GCM_VO();

    //===============================
    // endregion // view
    //===============================


    //===============================
    // region // variable
    //===============================
    private Calendar mCalRequest;
    private Calendar mCalRequest2;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog datePickerDialog2;

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            binding.tvGCM02.setText(sdfFormat.format(mCalRequest.getTime()));
        }
    };
    private DatePickerDialog.OnDateSetListener datePickerListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest2.set(Calendar.YEAR, year);
            mCalRequest2.set(Calendar.MONTH, month);
            mCalRequest2.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            binding.tvGCM12.setText(sdfFormat.format(mCalRequest2.getTime()));
        }
    };

    //===============================
    // endregion // variable
    //===============================

    //===============================
    // region // initialize
    //===============================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComplaintDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        GCMdata = (GCM_VO) intent.getExtras().get("GCMdata");

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.btnUpdate.setOnClickListener(this::onClickUpdate);
        binding.btnDelete.setOnClickListener(this::onClickDelete);

        binding.addGCM09.setOnClickListener(this::onClickAddGCM09);
        binding.addGCM11.setOnClickListener(this::onClickAddGCM11);
        binding.addGCM15.setOnClickListener(this::onClickAddGCM15);

        binding.addRUN.setOnClickListener(this::onClickaddRun);

        binding.ddGCM04.setOnClickListener(this::onClickCategory1);
        binding.ddGCM05.setOnClickListener(this::onClickCategory2);
        binding.ddGCM06.setOnClickListener(this::onClickCategory3);

    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {

        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
        mCalRequest2=Calendar.getInstance();

        binding.tvGCM02.setText(sdfFormat.format(mCalRequest.getTime()));
        binding.tvGCM12.setText(sdfFormat.format(mCalRequest2.getTime()));
        binding.tvGCM03.setText(GCMdata.GCM_03);
        binding.tvGCM03NM.setText(GCMdata.GCM_03_NM);
        binding.ddGCM04.setText((GCMdata.GCM_04_NM));
        binding.ddGCM05.setText((GCMdata.GCM_05_NM));
        binding.ddGCM06.setText((GCMdata.GCM_06_NM));

        binding.tvGCM14.setText((GCMdata.GCM_14));

        requestCombo_Read("GCM04");
        requestCombo_Read("GCM05");
        requestCombo_Read("GCM06");

    }

    public String removeComma(String data){

        return data.replaceAll("\\,","");
    }

    private void onClickDate(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    private void onClickDate2(View v) {

        datePickerDialog2 = new DatePickerDialog(mContext, datePickerListener2, mCalRequest2.get(Calendar.YEAR),
                mCalRequest2.get(Calendar.MONTH), mCalRequest2.get(Calendar.DAY_OF_MONTH));
        datePickerDialog2.show();
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


    //===============================
    // endregion // initialize
    //===============================


    //===============================
    // region // event
    //===============================


    /**
     * 요청일자 클릭
     *
     * @param v
     */
    private void onClickRequestDate(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    private void onClickDelete(View view) {
        AlertDialog.Builder oDialog = new AlertDialog.Builder(this,
                android.R.style.Theme_DeviceDefault_Light_Dialog);

        oDialog.setTitle("삭제하시겠습니까?")
                .setPositiveButton("아니오", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }

                })
                .setNeutralButton("예", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        requestSaveNGM("DELETE");
                    }
                })

                .setCancelable(false)
                .show();
    }


    private void onClickUpdate(View view) {
        checkValidation();
    }

    private void checkValidation() {
        if (binding.tvGCM03.getText().toString().isEmpty()) {
            toast("거래처를 입력해주세요.");
        } else if (binding.textGCM07.getText().toString().isEmpty()) {
            toast("출고번호를 입력해주세요.");
        } else if (binding.tvGCM02.getText().toString().isEmpty()) {
            toast("접수일자를 입력해주세요.");
        } // 현장명 잠시 제외
        else {
            requestSaveNGM("M_UPDATE");
        }
    }

    public void requestSaveNGM(String GUBUN) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();
        Http.gcm(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).GCM_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,

                GUBUN,
                mUser.Value.MEM_CID,
                "",                                     // 자동생성
                binding.tvGCM02.getText().toString().replaceAll("-", ""),  // 일자
                binding.tvGCM03.getText().toString(), //거래처
                binding.ddGCM04.getText().toString(),//처리상태
                binding.ddGCM05.getText().toString(),//발생유형
                binding.ddGCM06.getText().toString(),//처리유형
                binding.tvGCM07.getText().toString(), //거래처
                GCMdata.GCM_08,
                binding.tvGCM09.getText().toString(), //
                "",
                binding.tvGCM11.getText().toString(), //
                binding.tvGCM12.getText().toString().replaceAll("-", ""),  // 제조일자
                binding.tvGCM13.getText().toString(), //
                Double.parseDouble(binding.tvGCM14.getText().toString()), // 고객만족도
                binding.tvGCM15.getText().toString(), //
                binding.tvGCM16.getText().toString(),
                binding.tvGCM16.getText().toString(),
                binding.tvNote.getText().toString(), // 비고
                mUser.Value.MEM_01 // 최종수정자
        ).enqueue(new Callback<GCM_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<GCM_Model> call, Response<GCM_Model> response) {
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
                        toast("고객불만정보를 수정 하였습니다.");
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }.sendMessage(msg);
            }


            @Override
            public void onFailure(Call<GCM_Model> call, Throwable t) {
                call.cancel();
                closeLoadingBar();
            }
        });
    }

    //===============================
    // endregion // method
    //===============================

    //===============================
    // region // api
    //=============================

    private void onClickAddClient(View v) {   // 거래처찾기
        Intent intent = new Intent(mContext, ClientNameListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, ClientNameListActivity.REQUEST_CODE);
    }

    private void onClickAddGCM09(View v) {   // 담당자
        Intent intent = new Intent(mContext, ClientNameListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, ClientNameListActivity.REQUEST_CODE);
    }
    private void onClickAddGCM11(View v) {   // 처리자
        Intent intent = new Intent(mContext, ClientNameListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, ClientNameListActivity.REQUEST_CODE);
    }
    private void onClickAddGCM15(View v) {   // 확인자
        Intent intent = new Intent(mContext, ClientNameListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, ClientNameListActivity.REQUEST_CODE);
    }

    private void onClickaddRun(View v) {    // 제품찾기
        Intent intent = new Intent(mContext, RunFindActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        intent.putExtra("GCM_03", binding.tvGCM03.getText());

        mActivity.startActivityForResult(intent, RunFindActivity.REQUEST_CODE);
    }

    private void dropdownCategory1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle("처리상태")
                .setItems(GCM04NM_List, (dialog, which) -> {
                    binding.ddGCM04.setText(GCM04NM_List[which]);
                    GCM04=GCM04_List[which];
                }).setCancelable(false).create();

        builder.show();
    }

    private void dropdownCategory2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle("고객불만유형")
                .setItems(GCM05NM_List, (dialog, which) -> {
                    binding.ddGCM05.setText(GCM05NM_List[which]);
                    GCM05=GCM05_List[which];
                }).setCancelable(false).create();

        builder.show();
    }

    private void dropdownCategory3() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle("처리상태")
                .setItems(GCM06NM_List, (dialog, which) -> {
                    binding.ddGCM06.setText(GCM06NM_List[which]);
                    GCM06=GCM06_List[which];
                }).setCancelable(false).create();

        builder.show();
    }

    private void onClickCategory1(View v) {
        dropdownCategory1();
    }
    private void onClickCategory2(View v) {
        dropdownCategory2();
    }
    private void onClickCategory3(View v) {
        dropdownCategory3();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ClientNameListActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            cltVO = (CLT_VO) data.getSerializableExtra(ClientNameListActivity.CLIENT_LIST);
            binding.tvGCM03NM.setText(cltVO.CLT_02);
            binding.tvGCM03.setText(cltVO.CLT_01);

        }else  if (requestCode == MemberListActivity.REQUEST_CODE) {

            memVO = (MEM_VO) data.getSerializableExtra(MemberListActivity.CLIENT_LIST);

            if (resultCode == 9) {
                binding.tvGCM09.setText(memVO.MEM_01);
                binding.tvGCM09NM.setText(memVO.MEM_02);
            }else if (resultCode == 11) {
                binding.tvGCM11.setText(memVO.MEM_01);
                binding.tvGCM11NM.setText(memVO.MEM_02);
            }else if (resultCode == 15) {
                binding.tvGCM15.setText(memVO.MEM_01);
                binding.tvGCM15NM.setText(memVO.MEM_02);
            }
        } if (requestCode == RunFindActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            run2Vo = (RUN2_VO) data.getSerializableExtra(RunFindActivity.RUN_LIST);
            binding.tvGCM07.setText(run2Vo.RUN_01);
            binding.tvDAH02.setText(run2Vo.RUN_09);
            binding.tvRUN06.setText(run2Vo.RUN_06);
        }
    }

    private void bindingCategory_GCM04(ArrayList<GCM_VO> data) {


        if (data.size()==0 ){

            GCM04NM_List = new String[1];
            GCM04_List =  new String[1];
            GCM04NM_List[0]=  "";
            GCM04_List[0] ="";
        }
        else {

            GCM04NM_List = new String[data.size()];
            GCM04_List = new String[data.size()];
            for (int i = 0; i < data.size(); i++) {
                GCM04NM_List[i] = data.get(i).CD_NM;
                GCM04_List[i] = data.get(i).CD;
            }
        }

    }
    private void bindingCategory_GCM05(ArrayList<GCM_VO> data) {


        if (data.size()==0 ){

            GCM05NM_List = new String[1];
            GCM05_List =  new String[1];
            GCM05NM_List[0]=  "";
            GCM05_List[0] ="";
        }
        else {

            GCM05NM_List = new String[data.size()];
            GCM05_List = new String[data.size()];
            for (int i = 0; i < data.size(); i++) {
                GCM05NM_List[i] = data.get(i).CD_NM;
                GCM05_List[i] = data.get(i).CD;
            }
        }

    }
    private void bindingCategory_GCM06(ArrayList<GCM_VO> data) {


        if (data.size()==0 ){

            GCM06NM_List = new String[1];
            GCM06_List =  new String[1];
            GCM06NM_List[0]=  "";
            GCM06_List[0] ="";
        }
        else {

            GCM06NM_List = new String[data.size()];
            GCM06_List = new String[data.size()];
            for (int i = 0; i < data.size(); i++) {
                GCM06NM_List[i] = data.get(i).CD_NM;
                GCM06_List[i] = data.get(i).CD;
            }
        }

    }

    private void requestCombo_Read( String GUBUN ) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.gcm(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).GCM_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_COMBO",
                mUser.Value.MEM_CID,
                GUBUN,"","","","",""
        ).enqueue(new Callback<GCM_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<GCM_Model> call, Response<GCM_Model> response) {
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

                            Response<GCM_Model> response = (Response<GCM_Model>) msg.obj;

                            if (response.isSuccessful()) {

                                if (response.body().Data.size()==0){

                                    if(GUBUN=="GCM04") bindingCategory_GCM04(new ArrayList<GCM_VO>());
                                    else if(GUBUN=="GCM05") bindingCategory_GCM05(new ArrayList<GCM_VO>());
                                    else if(GUBUN=="GCM06") bindingCategory_GCM06(new ArrayList<GCM_VO>());
                                }
                                else {

                                    if (GUBUN =="GCM04") bindingCategory_GCM04(response.body().Data);
                                    else if (GUBUN =="GCM05") bindingCategory_GCM05(response.body().Data);
                                    else if (GUBUN =="GCM06") bindingCategory_GCM06(response.body().Data);
                                }
                            }
                            else {
                                if(GUBUN=="GCM04") bindingCategory_GCM04(new ArrayList<GCM_VO>());
                                else if(GUBUN=="GCM05") bindingCategory_GCM05(new ArrayList<GCM_VO>());
                                else if(GUBUN=="GCM06") bindingCategory_GCM06(new ArrayList<GCM_VO>());
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<GCM_Model> call, Throwable t) {
                closeLoadingBar();
                call.cancel();
                BaseAlert.show(mContext, R.string.network_error_2);
            }
        });
    }
}