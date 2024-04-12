package kr.hmit.dmjs.ui.schedule;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityScheduleDetailBinding;
import kr.hmit.dmjs.model.response.CAL_Model;
import kr.hmit.dmjs.model.response.MEM_ReadModel;
import kr.hmit.dmjs.model.vo.CALC_VO;
import kr.hmit.dmjs.model.vo.CAL_VO;
import kr.hmit.dmjs.model.vo.MEM_ReadVO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleDetailActivity extends BaseActivity {
    public static final int REQUEST_CODE = 2022;
    public static final String Schedule_INFO = "Schedule_INFO";

    private ActivityScheduleDetailBinding binding;
    private String[] mCategory;
    private String[] mManager;
    private String[] mManagerNo;

    private CALC_VO CALCVO;
    private ArrayList<CAL_VO> CALVO;

    private int flag = 0;
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if (flag == 1) binding.tvStartDate.setText(sdfFormat.format(mCalRequest.getTime()));
            else binding.tvEndDate.setText(sdfFormat.format(mCalRequest.getTime()));
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScheduleDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        CALCVO = (CALC_VO) intent.getExtras().get("CALCData");
        int CAL_01 = CALCVO.CALC_01;
        requestCAL_Read(CAL_01);

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.tvStartDate.setOnClickListener(this::onClicktvStartDate);
        binding.tvEndDate.setOnClickListener(this::onClickEndDate);
        binding.tvCategoryDDL.setOnClickListener(this::onClickCategory);
        binding.tvManagerDDL.setOnClickListener(this::onClickManager);
        binding.btnUpdate.setOnClickListener(this::onClickUpdate);
        binding.btnDelete.setOnClickListener(this::onClickDelete);
    }


    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {
        mCategory = new String[]{getString(R.string.write_work_0)};
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
        binding.tvStartDate.setText(sdfFormat.format(mCalRequest.getTime()));
        binding.tvEndDate.setText(sdfFormat.format(mCalRequest.getTime()));
        requestListCAL_Read();
        requestMEM_Read();
    }
    /**
     * 요청일자 클릭
     *
     * @param v
     */
    private void onClicktvStartDate(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        flag =1;
        datePickerDialog.show();
    }
    private void onClickEndDate(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        flag =2;
        datePickerDialog.show();
    }
    private void onClickCategory(View v) {
        dropdownCategory();
    }
    private void onClickManager(View v) {
        dropdownManager();
    }
    private void onClickUpdate(View view) {
        checkValidation();
    }
    private void checkValidation() {

        String strDate = binding.tvStartDate.getText().toString().trim();
        String tvEndDate = binding.tvEndDate.getText().toString().trim();
        String tvContent = binding.tvContent.getText().toString().trim();

        if (TextUtils.isEmpty(strDate)) {
            BaseAlert.show(mContext, R.string.addWork);
            return;
        }

        if (TextUtils.isEmpty(tvEndDate)) {
            BaseAlert.show(mContext, R.string.addWork);
            return;
        }

        if (TextUtils.isEmpty(tvContent)) {
            BaseAlert.show(mContext, R.string.addWork);
            return;
        }

        requestSaveCal("M_UPDATE");
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
                        requestSaveCal("M_DELETE");
                        toast("삭제되었습니다.");
                    }
                })
                .setCancelable(false)
                .show();
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
                                if (response.body().Total>0) {
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
    public void requestSaveCal(String GUBUN) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();
        Http.cal(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).CAL_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                GUBUN,
                mUser.Value.MEM_CID,
                CALVO.get(0).CAL_01,
                binding.tvContent.getText().toString(),
                CALVO.get(0).CAL_03,
                binding.tvStartDate.getText().toString().replaceAll("-",""),
                binding.tvEndDate.getText().toString().replaceAll("-",""),
                mUser.Value.MEM_01,
                "",
                binding.tvCategory.getText().toString(),
                null,
                binding.tvManager.getText().toString(),
                mUser.Value.MEM_02
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
                        setResult(RESULT_OK, intent);
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
     * 업무분류 리스트를 보여준다.
     */
    private void dropdownCategory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle("일정분류")
                .setItems(mCategory, (dialog, which) -> {
                    setCategory(which);

                }).setCancelable(false).create();

        builder.show();
    }
    /**
     * 담당자 리스트를 보여준다.
     */
    private void dropdownManager() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle("담당자")
                .setItems(mManager, (dialog, which) -> {
                    setManager(which);
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
            binding.tvCategory.setEnabled(true);
            binding.tvCategory.setText("");
            binding.tvCategory.requestFocus();
        } else {
            binding.tvCategory.setEnabled(false);
            binding.tvCategory.setText(mCategory[which]);
        }

    }
    /**
     * 선택된 담당자를 화면에 설정한다.
     *
     * @param which
     */
    private void setManager(int which) {
        binding.tvManager.setEnabled(false);
        binding.tvManagerDDL.setText(mManager[which]);
        int Index = 0;
        for (int i = 0; i < mManager.length; i++) {
            if(true == mManager[which].equals(mManager[i]))
            {
                Index = i;
                binding.tvManager.setText(mManagerNo[Index]);
                break;
            }
        }

    }

    public void requestCAL_Read(int CAL_01) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            return;
        }

        openLoadingBar();

        Http.cal(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).CAL_Detail(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_DETAIL",
                mUser.Value.MEM_CID,
                CAL_01
        ).enqueue(new Callback<CAL_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<CAL_Model> call, Response<CAL_Model> response) {
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

                            Response<CAL_Model> response = (Response<CAL_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if (response.body().Data.get(0).Validation) {
                                        bindingData(response.body().Data);
                                    } else {
                                        BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0));
                                    }
                                }else{
                                    bindingData(new  ArrayList<CAL_VO>());
                                    toast("검색결과가 없습니다.");
                                }
                            } else {
                                BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<CAL_Model> call, Throwable t) {
                call.cancel();
                closeLoadingBar();
            }
        });
    }
    public void requestListCAL_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            return;
        }

        openLoadingBar();

        Http.cal(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).CAL_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_CATEGORY_L",
                mUser.Value.MEM_CID
        ).enqueue(new Callback<CAL_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<CAL_Model> call, Response<CAL_Model> response) {
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
                            Response<CAL_Model> response = (Response<CAL_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if (response.body().Data.get(0).Validation) {
                                    bindingCategory(response.body().Data);
                                }
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<CAL_Model> call, Throwable t) {
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
    private void bindingCategory(ArrayList<CAL_VO> data) {
        mCategory = new String[data.size() + 1];

        mCategory[0] = getString(R.string.write_work_0);

        for (int i = 0; i < data.size(); i++) {
            mCategory[i + 1] = data.get(i).CAL_04;
        }
    }

    /**
     * 서버 데이터 바인딩
     *
     * @param data
     */
    private void bindingData(ArrayList<CAL_VO> data) {
        if (data == null)
            data = new ArrayList<>();

        CALVO = data;
        binding.tvStartDate.setText(CALVO.get(0).CAL_11);
        binding.tvEndDate.setText(CALVO.get(0).CAL_12);
        binding.tvCategory.setText(CALVO.get(0).CAL_04 );
        binding.tvContent.setText(CALVO.get(0).CAL_02);
        binding.tvManager.setText(CALVO.get(0).CAL_05);
        binding.tvManagerDDL.setText(CALVO.get(0).CAL_05_NM);
    }
    /**
     * 담당자 바인딩
     *
     * @param data
     */
    private void bindingManager(ArrayList<MEM_ReadVO> data) {
        mManager = new String[data.size()];
        mManagerNo = new String[data.size()];
        for (int i = 0; i < data.size(); i++) {
            mManager[i] = data.get(i).MEM_02;
            mManagerNo[i] = data.get(i).MEM_01;
        }
    }

}