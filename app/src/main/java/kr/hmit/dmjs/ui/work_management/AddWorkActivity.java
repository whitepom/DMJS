package kr.hmit.dmjs.ui.work_management;

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

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityWriteWorkBinding;
import kr.hmit.dmjs.model.response.WKS_Model;
import kr.hmit.dmjs.model.vo.MEM_ReadVO;
import kr.hmit.dmjs.model.vo.WKS_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.find_employee.FindEmployeeActivity;
import kr.hmit.dmjs.ui.work_management.adapter.AddWorkEmployeeAdapter;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.ui.work_management.find_employee.work_management_FindEmployeeActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddWorkActivity extends BaseActivity {
    public static final int REQUEST_CODE = 2021;
    public static final String WORK_INFO = "WORK_INFO";

    //===============================
    // region // view
    //===============================
    private ActivityWriteWorkBinding binding;
    private String[] mCategory;
    private ArrayList<MEM_ReadVO> mListEmployee;
    private AddWorkEmployeeAdapter mAdapter;

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
        binding = ActivityWriteWorkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());

        initEmployeeList();

        binding.tvRequestDate.setOnClickListener(this::onClickRequestDate);
        binding.tvCategory.setOnClickListener(this::onClickCategory);
        binding.addEmployee.setOnClickListener(this::onClickAddEmployee);
        binding.btnSave.setOnClickListener(this::onClickSave);

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

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {
        requestWKS_05();
        mCategory = new String[]{getString(R.string.write_work_0)};
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
        binding.tvRequestDate.setText(sdfFormat.format(mCalRequest.getTime()));



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

    private void onClickCategory(View v) {
        dropdownCategory();

    }
    private void onClickSave(View view) {
        checkValidation();
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

        requestSaveWks();
    }
    public void requestSaveWks() {
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
                "M_INSERT",
                mUser.Value.MEM_CID,
                "",
                binding.tvRequestDate.getText().toString().replaceAll("-",""),
                "1",
                binding.etContents.getText().toString(),
                binding.etCategory.getText().toString(),
                "",
                binding.etDetail.getText().toString(),
                "",
                mListEmployee.size()>0?mListEmployee.get(0).MEM_01:mUser.Value.MEM_01,
                "1",
                mListEmployee.size()>1?mListEmployee.get(1).MEM_01:"",
                "1",
                mListEmployee.size()>2?mListEmployee.get(2).MEM_01:"",
                "1",
                mListEmployee.size()>3?mListEmployee.get(3).MEM_01:"",
                "1",
                mListEmployee.size()>4?mListEmployee.get(4).MEM_01:"",
                "1",
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
                        toast("업무를 추가하였습니다.");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 담당자 추가

        if (requestCode == FindEmployeeActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            mListEmployee = (ArrayList<MEM_ReadVO>) data.getSerializableExtra(FindEmployeeActivity.EMPLOYEE_INFO);
            mAdapter.update(mListEmployee);
        }
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
                                if (response.body().Total > 0) {
                                    bindingCategory(response.body().Data);
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


    //=============================
    // endregion // api
    //===============================
}