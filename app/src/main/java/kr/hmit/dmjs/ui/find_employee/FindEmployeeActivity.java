package kr.hmit.dmjs.ui.find_employee;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import kr.hmit.base.settings.SettingsKey;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityFindEmployeeBinding;
import kr.hmit.dmjs.model.response.MEM_ReadModel;
import kr.hmit.dmjs.model.vo.MEM_ReadVO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.ui.login.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindEmployeeActivity extends BaseActivity {
    public static final int REQUEST_CODE = 2020;
    public static final String EMPLOYEE_INFO = "EMPLOYEE_INFO";

    //===========================
    // view
    //===========================
    private ActivityFindEmployeeBinding binding;


    //===========================
    // variable
    //===========================
    private FindEmployeeAdapter mAdapter;
    private ArrayList<MEM_ReadVO> mListTotal;
    private ArrayList<MEM_ReadVO> mListSelected;
    private ArrayList<MEM_ReadVO> employeeData;
    private String btnId;
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

        mAdapter = new FindEmployeeAdapter(mContext, new ArrayList<>());
        mAdapter.setOnItemClickListener((view, position, isDelete) -> {
            if (isDelete)
                mListSelected.remove(mListTotal.get(position));
            else
                mListSelected.add(mListTotal.get(position));
                Intent intent = new Intent();
                intent.putExtra(EMPLOYEE_INFO, mListSelected);
                intent.putExtra("btnId", btnId);
                setResult(RESULT_OK, intent);
                finish();
        });

        binding.recyclerView.setAdapter(mAdapter);


        Intent intent = getIntent();
        employeeData = (ArrayList<MEM_ReadVO>)intent.getExtras().get("employeeData");
        btnId = (String) intent.getExtras().get("btnId");
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
                                            mListSelected.remove(mListTotal.get(i));
                                        }
                                    }
                                    mAdapter.update(mListTotal);

                                } else {
                                    toast("동일계정 접속 > 로그인 페이지로 이동합니다");  // asdfasdfasdf
                                    mSettings.putBooleanItem(SettingsKey.AutoLogin,false);
                                    Intent intent = new Intent(mContext, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("simultaneous_connect",mUser.Value.MEM_01.toString());
                                    startActivity(intent);
                                    finish();
                                    //BaseAlert.show(mContext, mContext.getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ErrorCode);
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
