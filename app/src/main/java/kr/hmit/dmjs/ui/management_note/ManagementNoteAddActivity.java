
package kr.hmit.dmjs.ui.management_note;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityManagementNoteAddBinding;

import kr.hmit.dmjs.model.response.MEM_ReadModel;
import kr.hmit.dmjs.model.vo.LED_VO;

import kr.hmit.dmjs.model.vo.MEM_ReadVO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.work_management.adapter.AddWorkEmployeeAdapter;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagementNoteAddActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8023;
    public static final String CATEGORY_INFO = "CATEGORY_INFO";
    public static final String EMPLOYEE_INFO = "EMPLOYEE_INFO";
    //public static final String OrderManagement_ADD = "OrderManagement_ADD";

    private ActivityManagementNoteAddBinding binding;
    private ArrayList<MEM_ReadVO> mListEmployee;
    private LED_VO ledVO;
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat;
    private String[] mCategory;
    private AddWorkEmployeeAdapter mAdapter;
    private DatePickerDialog datePickerDialog;
    private String[] mManager;
    private String[] mManagerNo;
    private String[] mManagerposition;
    private String[] mManagerplus;
    private String ManagerNo;
    private int Index;



    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManagementNoteAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {

        binding.imgBack.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(this::onClickSave);
        binding.addReceiver.setOnClickListener(this::onClickAddEmployee);
        binding.tvSendName.setText(mUser.Value.MEM_02);

    }
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {


        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();

        requestMEM_Read();

    }

    private void onClickAddEmployee(View v) {
          dropdownManager();
    }
    /**
     * 담당자 리스트를 보여준다.
     */
    private void dropdownManager() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("수신자")
                .setItems(mManagerplus,(dialog, which) -> {
                    setManager(which);
                }).setCancelable(false).create();
        builder.show();
    }
    /**
     * 선택된 담당자를 화면에 설정한다.
     *
     * @param which
     */
    private void setManager(int which) {

        binding.tvReceiverName.setEnabled(false);

        for (int i = 0; i < mManager.length; i++) {
            if(true == mManager[which].equals(mManager[i]))
            {
                Index = i;
                binding.tvReceiverName.setText(mManager[Index] +" "+ mManagerposition[Index]);
                ManagerNo = mManagerNo[Index];
                break;
            }
        }

    }

    /**
     * 담당자 바인딩
     *
     * @param data
     */
    private void bindingManager(ArrayList<MEM_ReadVO> data) {

        mManager = new String[data.size()];
        mManagerNo = new String[data.size()];
        mManagerposition = new String[data.size()];
        mManagerplus = new String[data.size()];


        for (int i = 0; i < data.size(); i++) {

            mManagerplus[i] = (data.get(i).MEM_02 + " "+ data.get(i).MEM_32_NM);
            mManagerposition[i] = data.get(i).MEM_32_NM;
            mManager[i] = data.get(i).MEM_02;
            mManagerNo[i] = data.get(i).MEM_01;

        }
    }


    private void onClickSave(View view) {

            requestSaveLED("INSERT");
            System.out.println("ManagerNo >>>>>>>>>"+ManagerNo);

        }


    public void requestSaveLED(String GUBUN) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();
        Http.led(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).MMO_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                GUBUN,
                mUser.Value.MEM_CID,
                "MMO",
                "",
                "0",
                binding.tvClientName2.getText().toString(),
                "0",
                mUser.Value.MEM_01,
                ManagerNo,
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
                        Intent intent = new Intent();
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

    private void requestMEM_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.wks(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).MEM_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03
        ).enqueue(new Callback<MEM_ReadModel>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<MEM_ReadModel> call, Response<MEM_ReadModel> response) {
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
                            Response<MEM_ReadModel> response = (Response<MEM_ReadModel>) msg.obj;

                            if (response.isSuccessful()) {
                                if (response.body().Data.get(0).Validation) {
                                    bindingManager(response.body().Data);
                                }
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<MEM_ReadModel> call, Throwable t) {
                closeLoadingBar();
                call.cancel();
                BaseAlert.show(mContext, R.string.network_error_2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 담당자 추가

        }


    }



