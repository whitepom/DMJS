package kr.hmit.dmjs.ui.work_management;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kr.hmit.base.settings.SettingsKey;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityWorkManagemenDetailBinding;
import kr.hmit.dmjs.model.response.WKSM_Model;
import kr.hmit.dmjs.model.response.WKS_Model;
import kr.hmit.dmjs.model.vo.MEM_ReadVO;
import kr.hmit.dmjs.model.vo.WKSM_VO;
import kr.hmit.dmjs.model.vo.WKS_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.find_employee.FindEmployeeActivity;
import kr.hmit.dmjs.ui.login.LoginActivity;
import kr.hmit.dmjs.ui.work_management.adapter.AddWorkEmployeeAdapter;
import kr.hmit.dmjs.ui.work_management.find_employee.work_management_FindEmployeeActivity;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkManagementDetailActivity extends BaseActivity {
    public static final int REQUEST_CODE = 2022;
    public static final String WORK_INFO = "WORK_INFO";

    //===============================onClickSave
    // region // view
    //===============================
    private ActivityWorkManagemenDetailBinding binding;
    private String[] mCategory;
    private ArrayList<MEM_ReadVO> mListEmployee;
    private AddWorkEmployeeAdapter mAdapter;
    private String check="N";

    private WKS_VO WKSData;

    //===============================
    // endregion // view
    //===============================


    //===============================
    // region // variable
    //===============================
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            binding.tvRequestDate.setText(sdfFormat.format(mCalRequest.getTime()));
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
        binding = ActivityWorkManagemenDetailBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        initLayout();
        initialize();



    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        initEmployeeList();
        binding.tvRequestDate.setOnClickListener(this::onClickRequestDate);
        binding.tvWorkType.setOnClickListener(this::onClickCategory);
        binding.addEmployee.setOnClickListener(this::onClickAddEmployee);
        binding.btnUpdate.setOnClickListener(this::onClickUpdate);
        binding.btnDelete.setOnClickListener(this::onClickDelete);
        binding.btnResultSave.setOnClickListener(this::onClickResultSave);
        binding.radioGroup2.setOnCheckedChangeListener(this::onClickChangeRadioButton);
        binding.adminMSG.setEnabled(false);


    }
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {



        mCategory = new String[]{getString(R.string.write_work_0)};
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
        binding.tvRequestDate.setText(sdfFormat.format(mCalRequest.getTime()));


        Intent intent = getIntent();
        WKSData = (WKS_VO)intent.getExtras().get("WKSData");
        //mListWKSM=new ArrayList<>();
        //ArrayList<EmployeeVO> employees = getEmployee(WKSData);

        String[] splitText =WKSData.WKSM_LIST.split("-");  // 담당자여부
        for(int list=0;list<splitText.length;list++){
            if(splitText[list].equals(mUser.Value.MEM_01))
            {check="Y";}
        }

        binding.etResult.setText(WKSData.WKS_08);
        binding.adminMSG.setText(WKSData.WKS_08);
        binding.tvNumber.setText(WKSData.WKS_01);
        binding.tvRequestDate.setText(WKSData.WKS_02);
        //binding.etSortation.setText(getState(employees));
        binding.etCategory.setText(WKSData.WKS_05);
        binding.etContents.setText(WKSData.WKS_04);
        binding.etDetail.setText(WKSData.WKS_07);
        binding.btnResultSave.setVisibility(View.INVISIBLE);
        if(binding.etResult.getText().length()!=0){
            binding.btnResultSave.setText("삭제");
        }
        else if(binding.etResult.getText().length()==0){
                  binding.btnResultSave.setText("등록");
              }
        binding.etResult.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN ) {
                    //&& i == KeyEvent.KEYCODE_ENTER
                    binding.btnResultSave.setText("등록");
                    return false;
                }
                return false;
            }
        });
      //  else if(binding.etResult.getText().length()==0){
      //      binding.btnResultSave.setText("저장");
      //  }


        switch(WKSData.WKS_0302){
            case "1" :
                binding.radioBtnWait.setChecked(true);
                break;
            case "2":
                binding.radioBtnProcessing.setChecked(true);
                break;
            case "3" :
                binding.radioBtnComplete.setChecked(true);
                break;
        }

        mListEmployee = getEmployee(WKSData.WKSM_LIST.split("-"));
        for(int i = 0 ; i < mListEmployee.size(); i++){
            if(i==0){
                WKSData.WKS_1001 = mListEmployee.get(i).MEM_01;
            }else if(i==1){
                WKSData.WKS_1101 = mListEmployee.get(i).MEM_01;
            }else if(i==2){
                WKSData.WKS_1201 = mListEmployee.get(i).MEM_01;
            }else if(i==3){
                WKSData.WKS_1301 = mListEmployee.get(i).MEM_01;
            }else if(i==4){
                WKSData.WKS_1401 = mListEmployee.get(i).MEM_01;
            }
        }

        if(mUser.Value.MEM_01.equals(WKSData.WKS_98)&&check.equals("Y")){ // 요청자 o, 담당자 o

            if(WKSData.MEM_11.contains(mUser.Value.MEM_01)){ // 관리자일 경우
                binding.etResult.setVisibility(View.VISIBLE);
                binding.adminMSG.setVisibility(View.GONE);
                binding.btnResultSave.setVisibility(View.VISIBLE);
            }
            binding.btnUpdate.setVisibility(View.VISIBLE);
            binding.btnDelete.setVisibility(View.VISIBLE);
            binding.addEmployee.setVisibility(View.VISIBLE);
            binding.tvTextEmployee.setVisibility(View.VISIBLE);

            for(MEM_ReadVO tempVO : mListEmployee){
                if(mUser.Value.MEM_01.equals(tempVO.MEM_01)&&tempVO.state.equals("대기")){
                    requestSaveWksm("M_UPDATE_R","","0");
                    tempVO.state="처리중";
                    ((WorkManagementMainActivity)WorkManagementMainActivity.context).requestWKS_Read();
                }
            }
            mAdapter.update(mListEmployee);

        }
        else if(mUser.Value.MEM_01.equals(WKSData.WKS_98)&&check.equals("N")){  // 요청자 o 담당자 x

            if(WKSData.MEM_11.contains(mUser.Value.MEM_01)){
                binding.etResult.setVisibility(View.VISIBLE);
                binding.adminMSG.setVisibility(View.GONE);
                binding.btnResultSave.setVisibility(View.VISIBLE);
            }
            binding.btnUpdate.setVisibility(View.VISIBLE);
            binding.btnDelete.setVisibility(View.VISIBLE);
            binding.addEmployee.setVisibility(View.VISIBLE);
            binding.tvTextEmployee.setVisibility(View.VISIBLE);

        }else if(check.equals("Y")&&!mUser.Value.MEM_01.equals(WKSData.WKS_98)){ // 요청자 x 담당자 o

           // if(mUser.Value.MEM_01.equals("admin")){
            if(WKSData.MEM_11.contains(mUser.Value.MEM_01)){
                binding.adminMSG.setVisibility(View.GONE);
                binding.btnResultSave.setVisibility(View.VISIBLE);
                binding.etResult.setVisibility(View.VISIBLE);
            }

            binding.btnUpdate.setVisibility(View.GONE);
            binding.btnDelete.setVisibility(View.GONE);
            binding.addEmployee.setVisibility(View.GONE);

            binding.tvResult.setVisibility(View.VISIBLE);
            binding.tvTextEmployee.setVisibility(View.GONE);


            for(MEM_ReadVO tempVO : mListEmployee){
                if(mUser.Value.MEM_01.equals(tempVO.MEM_01)&&tempVO.state.equals("대기")){
                    requestSaveWksm("M_UPDATE_R","","0");
                    tempVO.state="처리중";
                    ((WorkManagementMainActivity)WorkManagementMainActivity.context).requestWKS_Read();
                }
            }
            mAdapter.update(mListEmployee);
        }
        else{                                                                   // 요청자 x  담당자 x    - 관리자 only
              binding.btnUpdate.setVisibility(View.GONE);
              binding.btnDelete.setVisibility(View.GONE);
              binding.addEmployee.setVisibility(View.GONE);
              binding.adminMSG.setVisibility(View.GONE);
              binding.tvTextEmployee.setVisibility(View.GONE);

              binding.etResult.setVisibility(View.VISIBLE);
              binding.tvResult.setVisibility(View.VISIBLE);
              binding.btnResultSave.setVisibility(View.VISIBLE);
        }



        requestWKS_05();
        requestWKSM_Read();

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

    /**
     * 담당자 recyclerView 초기화
     */
    private void initEmployeeList() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.setLayoutManager(layoutManager);

        mListEmployee = new ArrayList<>();
        mAdapter = new AddWorkEmployeeAdapter(mContext, mListEmployee);
        binding.recyclerView.setAdapter(mAdapter);
    }

    private ArrayList<MEM_ReadVO> getEmployee(String[] strings) {
        ArrayList<MEM_ReadVO> list = new ArrayList<>();

        if(strings.length>2){
            list.add(new MEM_ReadVO(strings[1], strings[2], strings[0]));
        }
        if(strings.length>5){
            list.add(new MEM_ReadVO(strings[4], strings[5], strings[3]));

        }
        if(strings.length>8){
            list.add(new MEM_ReadVO(strings[7], strings[8], strings[6]));

        }
        if(strings.length>11){
            list.add(new MEM_ReadVO(strings[10], strings[11], strings[9]));

        }
        if(strings.length>14){
            list.add(new MEM_ReadVO(strings[13], strings[14], strings[12]));
        }

        return list;
    }


    //===============================
    // endregion // initialize
    //===============================


    //===============================
    // region // event
    //===============================

    /**
     * 담당자 추가 클릭 시
     *
     * @param v
     */
    private void onClickAddEmployee(View v) {
        Intent intent = new Intent(mContext, work_management_FindEmployeeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("employeeData",mListEmployee);
        mActivity.startActivityForResult(intent, work_management_FindEmployeeActivity.REQUEST_CODE);
    }


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
                        requestSaveWks("M_DELETE","");
                    }
                })

                .setCancelable(false)
                .show();
    }

    private void onClickCategory(View v) {
        dropdownCategory();

    }
    private void onClickUpdate(View view) {
        checkValidation();
    }
    private void onClickChangeRadioButton(RadioGroup radioGroup, int i){

     // if(WKSData.WKSM_LIST.contains(mUser.Value.MEM_01)){ //********************************************************* 여기
        if(check.equals("Y")){
            if(i==binding.radioBtnWait.getId()) {
             requestSaveWksm("M_UPDATE_P","대기","");
             ((WorkManagementMainActivity)WorkManagementMainActivity.context).requestWKS_Read();
             requestWKSM_Read();

             }
         else if(i==binding.radioBtnProcessing.getId()) {
             requestSaveWksm("M_UPDATE_P","처리중","");
             ((WorkManagementMainActivity)WorkManagementMainActivity.context).requestWKS_Read();
             requestWKSM_Read();
         }
         else{
             requestSaveWksm("M_UPDATE_P","완료","");
             ((WorkManagementMainActivity)WorkManagementMainActivity.context).requestWKS_Read();
             requestWKSM_Read();
            }
      }
        else {
            binding.radioBtnWait.setEnabled(false);
            binding.radioBtnProcessing.setEnabled(false);
            binding.radioBtnComplete.setEnabled(false);
        }

    }
    private void onClickResultSave(View view) {
        String message = binding.etResult.getText().toString();
        if(message.isEmpty()&& binding.btnResultSave.getText()=="등록"){
            BaseAlert.show(mContext, "내용을 입력해주세요.");
        }
        else if (binding.btnResultSave.getText()=="삭제"){
            requestSaveWks("M_UPDATE_ADMIN","");
        }
        else{
            requestSaveWks("M_UPDATE_ADMIN",message);
        }
    }

    private void checkValidation() {
        String strDate = binding.tvRequestDate.getText().toString().trim();
        String strCategory = binding.etCategory.getText().toString().trim();
        String strContents = binding.etContents.getText().toString().trim();

        if (TextUtils.isEmpty(strDate)) {
            toast("요청일자를 선택해주세요");
            return;
        }

        if (TextUtils.isEmpty(strCategory)) {
            toast("분류를 선택해주세요");
            return;
        }

        if (TextUtils.isEmpty(strContents)) {
            toast("업무내용을 입력해주세요");
            return;
        }

        requestSaveWks("M_UPDATE","");

    }
    public void requestSaveWks(String GUBUN,String message) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();
        Http.wks(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).WKS_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                GUBUN,
                mUser.Value.MEM_CID,
                WKSData.WKS_01,
                binding.tvRequestDate.getText().toString().replaceAll("-",""),
                "1",
                binding.etContents.getText().toString(),
                binding.etCategory.getText().toString(),
                "",
                binding.etDetail.getText().toString(),
                message,
                mListEmployee.size()>0?mListEmployee.get(0).MEM_01:mUser.Value.MEM_01,
                WKSData.WKS_1002,
                mListEmployee.size()>1?mListEmployee.get(1).MEM_01:"",
                WKSData.WKS_1102,
                mListEmployee.size()>2?mListEmployee.get(2).MEM_01:"",
                WKSData.WKS_1202,
                mListEmployee.size()>3?mListEmployee.get(3).MEM_01:"",
                WKSData.WKS_1302,
                mListEmployee.size()>4?mListEmployee.get(4).MEM_01:"",
                WKSData.WKS_1402,
                "",
                0,
                "",
                "",
                mUser.Value.MEM_01,
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
                        if (GUBUN.equals("M_UPDATE")) {
                            toast("수정하였습니다.");
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        } else {
                            toast("변경되었습니다");
                            if (message.isEmpty()) {
                                binding.btnResultSave.setText("등록");
                            }
                            binding.btnResultSave.setText("삭제");
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

    //===============================
    // endregion // event
    //===============================

    //===============================
    // region // method
    //===============================

    /**
     * 업무분류 리스트를 보여준다.
     */
    private void dropdownCategory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(R.string.write_work_1)
                .setItems(mCategory, (dialog, which) -> {
                    setCategory(which);

                }).setCancelable(false).create();

        builder.show();
    }

    /**
     * 선택된 업무분류를 화면에 설정한다.
     *
     * @param which
     */
    private void setCategory(int which) {
        // 0번일 때만 직접 입력
        if (which == 0) {
            binding.etCategory.setEnabled(true);
            binding.etCategory.setText("");
            binding.etCategory.requestFocus();
        } else {
            binding.etCategory.setEnabled(false);
            binding.etCategory.setText(mCategory[which]);
        }

    }

    //===============================
    // endregion // method
    //===============================

    //===============================
    // region // api
    //=============================


    public void requestWKS_05() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            return;
        }

        openLoadingBar();

        Http.wks(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).WKS_05(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                mUser.Value.MEM_CID
        ).enqueue(new Callback<WKS_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<WKS_Model> call, Response<WKS_Model> response) {
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
                        if (msg.what == 100) {
                            Response<WKS_Model> response = (Response<WKS_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if (response.body().Data.get(0).Validation) {
                                    bindingCategory(response.body().Data);
                                }else{
                                    toast("동일계정 접속 > 로그인 페이지로 이동합니다");  // asdfasdfasdf
                                    mSettings.putBooleanItem(SettingsKey.AutoLogin,false);
                                    Intent intent = new Intent(mContext, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("simultaneous_connect",mUser.Value.MEM_01.toString());
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<WKS_Model> call, Throwable t) {
                call.cancel();
                closeLoadingBar();
            }
        });
    }

    /**
     * 업무분류 바인딩
     *
     * @param data
     */
    private void bindingCategory(ArrayList<WKS_VO> data) {
        mCategory = new String[data.size() + 1];

        mCategory[0] = getString(R.string.write_work_0);

        for (int i = 0; i < data.size(); i++) {
            mCategory[i + 1] = data.get(i).WKS_05;
        }
    }

    private void requestWKSM_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.wks(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).WKSM_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST",
                mUser.Value.MEM_CID,
                Integer.parseInt(String.valueOf(WKSData.WKS_01))
        ).enqueue(new Callback<WKSM_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<WKSM_Model> call, Response<WKSM_Model> response) {
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

                            Response<WKSM_Model> response = (Response<WKSM_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if (response.body().Data.get(0).Validation) {
                                        ArrayList<WKSM_VO> mListWKSM = response.body().Data;
                                        for(WKSM_VO wvo : mListWKSM){
                                            for(MEM_ReadVO mvo : mListEmployee){
                                                if(mvo.MEM_01.equals(wvo.WKSM_02))
                                                    mvo.content=wvo.WKSM_04;
                                            }
                                        }
                                        mAdapter.update(mListEmployee);
                                    } else {
                                        BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ErrorCode);
                                    }
                                }else{
                                }
                            } else {
                                BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<WKSM_Model> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
            }
        });
    }
    public void requestSaveWksm(String GUBUN, String message, String state) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();
        Http.wks(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).WKSM_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                GUBUN,
                mUser.Value.MEM_CID,
                WKSData.WKS_01,
                mUser.Value.MEM_01,
                "2020-01-01",
                message,
                state
        ).enqueue(new Callback<WKSM_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<WKSM_Model> call, Response<WKSM_Model> response) {
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

                        if(GUBUN.equals("M_UPDATE_P")){
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                          //  finish();
                        }
                    }
                }.sendMessage(msg);
            }


            @Override
            public void onFailure(Call<WKSM_Model> call, Throwable t) {
                call.cancel();
                closeLoadingBar();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 담당자 추가

        if (requestCode == FindEmployeeActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            mListEmployee = (ArrayList<MEM_ReadVO>) data.getSerializableExtra(FindEmployeeActivity.EMPLOYEE_INFO);
            mAdapter.update(mListEmployee);
        }
    }

    //=============================
    // endregion // api
    //===============================
}