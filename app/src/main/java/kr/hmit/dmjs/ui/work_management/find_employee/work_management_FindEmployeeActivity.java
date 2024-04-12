package kr.hmit.dmjs.ui.work_management.find_employee;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityFindEmployeeBinding;
import kr.hmit.dmjs.model.response.MEM_ReadModel;
import kr.hmit.dmjs.model.vo.MEM_ReadVO;
import kr.hmit.dmjs.network.Http;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class work_management_FindEmployeeActivity extends BaseActivity {
    public static final int REQUEST_CODE = 2020;
    public static final String EMPLOYEE_INFO = "EMPLOYEE_INFO";

    //===========================
    // view
    //===========================
    private ActivityFindEmployeeBinding binding;


    //===========================
    // variable
    //===========================
    private work_management_FindEmployeeAdapter mAdapter;
    private ArrayList<MEM_ReadVO> mListTotal;
    private ArrayList<MEM_ReadVO> mListSelected;
    private ArrayList<MEM_ReadVO> employeeData;
    //===========================
    // initialize
    //===========================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindEmployeeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();

        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(this::onClickBack);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void initialize() {
        mListSelected = new ArrayList<>();

        mAdapter = new work_management_FindEmployeeAdapter(mContext, new ArrayList<>());
        mAdapter.setOnItemClickListener((view, position, isDelete) -> {
            if (isDelete)
                mListSelected.remove(mListTotal.get(position));
            else
                mListSelected.add(mListTotal.get(position));
        });

        binding.recyclerView.setAdapter(mAdapter);


        Intent intent = getIntent();
        employeeData = (ArrayList<MEM_ReadVO>)intent.getExtras().get("employeeData");

        requestMEM_Read();

    }

    /**
     * 닫기
     *
     * @param v
     */
    private void onClickBack(View v) {
        Intent intent = new Intent();
        intent.putExtra(EMPLOYEE_INFO, mListSelected);
        setResult(RESULT_OK, intent);
        finish();
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
                        if (msg.what == 100) {
                            closeLoadingBar();

                            Response<MEM_ReadModel> response = (Response<MEM_ReadModel>) msg.obj;

                            if (response.isSuccessful()) {
                                if (response.body().Data.get(0).Validation) {
                                    mListTotal = response.body().Data;

                                    for(int i =0; i<mListTotal.size();i++){
                                        for(int j=0;j<employeeData.size();j++){
                                            if(mListTotal.get(i).MEM_01.equals(employeeData.get(j).MEM_01)){
                                                mListTotal.get(i).isSelected=true;
                                                mListSelected.add(mListTotal.get(i));
                                            }
                                        }
                                    }
                                    mAdapter.update(mListTotal);

                                } else {
                                    BaseAlert.show(mContext, mContext.getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ErrorCode);
                                }
                            } else {
                                BaseAlert.show(mContext, mContext.getString(R.string.network_error_2) + "-" + response.errorBody());
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
}
