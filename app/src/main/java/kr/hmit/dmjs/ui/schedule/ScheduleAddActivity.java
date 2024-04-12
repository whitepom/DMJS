package kr.hmit.dmjs.ui.schedule;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import kr.hmit.dmjs.databinding.ActivityScheduleAddBinding;
import kr.hmit.dmjs.model.response.CAL_Model;
import kr.hmit.dmjs.model.response.MEM_ReadModel;
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

public class ScheduleAddActivity extends BaseActivity {
    public static final int REQUEST_CODE = 2021;
    public static final String Schedule_INFO = "Schedule_INFO";

    private ActivityScheduleAddBinding binding;

    private String[] mCategory;
    private String[] mManager;
    private String[] mManagerNo;


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
        binding = ActivityScheduleAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        binding.btnSave.setOnClickListener(this::onClickSave);
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
    private void onClickSave(View view) {
        checkValidation();
    }

    private void checkValidation() {

        String strDate = binding.tvStartDate.getText().toString().trim();
        String tvEndDate = binding.tvEndDate.getText().toString().trim();
        String tvContent = binding.tvContent.getText().toString().trim();

        if (TextUtils.isEmpty(strDate)) {
            BaseAlert.show(mContext,"시작일자를 선택해주세요.");
            return;
        }

        if (TextUtils.isEmpty(tvEndDate)) {
            BaseAlert.show(mContext, "종료일자를 선택해주세요.");
            return;
        }

        if (TextUtils.isEmpty(tvContent)) {
            BaseAlert.show(mContext, "관련내용을 입력해주세요.");
            return;
        }

        requestSaveCal();
    }
    /**
     * api 호출
     */
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

                                if (response.body().Data.size()==0){

                                    bindingManager(new ArrayList<MEM_ReadVO>());
                                }


                                else if (response.body().Data.get(0).Validation) {
                                    bindingManager(response.body().Data);
                                }

                            }
                            else{
                                bindingManager(new ArrayList<MEM_ReadVO>() );
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
    public void requestSaveCal() {
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
                "M_INSERT",
                mUser.Value.MEM_CID,
                0,
                binding.tvContent.getText().toString(),
                "",
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
            binding.tvCategoryDDL.setText("직접입력");
        } else {
            binding.tvCategory.setEnabled(false);
            binding.tvCategoryDDL.setText("일정분류");
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
     * 담당자 바인딩
     *
     * @param data
     */
    private void bindingManager(ArrayList<MEM_ReadVO> data) {

        if (data.size()==0 ){

            mManager = new String[1];
            mManagerNo =  new String[1];
            mManager[0]=   mUser.Value.MEM_02.toString();
            mManagerNo[0] = mUser.Value.MEM_01.toString();
        }
        else {
            mManager = new String[data.size()];
            mManagerNo = new String[data.size()];
            for (int i = 0; i < data.size(); i++) {
                mManager[i] = data.get(i).MEM_02;
                mManagerNo[i] = data.get(i).MEM_01;
            }
        }
    }
}
