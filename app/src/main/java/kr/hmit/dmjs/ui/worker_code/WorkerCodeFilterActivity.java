package kr.hmit.dmjs.ui.worker_code;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityWorkerCodeFilterBinding;
import kr.hmit.dmjs.model.response.WorkerCode_Model;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.worker_code.model.FilterVO;
import kr.hmit.dmjs.ui.worker_code.model.WorkerCodeVO;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WorkerCodeFilterActivity extends BaseActivity {
    public static final int REQUEST_CODE = 2021;
    public static final String CATEGORY_INFO = "CATEGORY_INFO";
    //================================
    // region // view
    private ActivityWorkerCodeFilterBinding binding;
    // endregion // view
    //================================

    private String[] mCategory1;
    private String[] mCategory2;
    private String[] mCategory3;

    private FilterVO filterVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkerCodeFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.tvFilter1.setOnClickListener(this::onClickCategory1);
        binding.tvFilter2.setOnClickListener(this::onClickCategory2);
        binding.tvFilter3.setOnClickListener(this::onClickCategory3);
        binding.btnSave.setOnClickListener(this::onClickSave);
    }

    @Override
    protected void initialize() {
        Intent intent = getIntent();
        filterVO = (FilterVO) intent.getExtras().get("filterData");

        binding.tvFilter1.setText(textCheck(filterVO.WOC_02));
        binding.tvFilter2.setText(textCheck(filterVO.WOC_09));
        binding.tvFilter3.setText(textCheck(filterVO.WOC_10));

        requestWOC_01();
    }

    private void onClickCategory1(View v) {
        if (mCategory1 != null && mCategory1.length == 1) {
            requestWOC_01();
        } else {
            dropdownCategory("작업자명",mCategory1,binding.tvFilter1);
        }
    }
    private void onClickCategory2(View v) {
        if (mCategory2 != null && mCategory2.length == 1) {
            requestWOC_01();
        } else {
            dropdownCategory("생산공정",mCategory2,binding.tvFilter2);
        }
    }
    private void onClickCategory3(View v) {
        if (mCategory3 != null && mCategory3.length == 1) {
            requestWOC_01();
        } else {
            dropdownCategory("직급",mCategory3,binding.tvFilter3);
        }
    }

    private void dropdownCategory(String dialogTitle, String[] category, TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(dialogTitle)
                .setItems(category, (dialog, which) -> {
                    textView.setText(category[which]);
                }).setCancelable(false).create();

        builder.show();
    }

    private void onClickSave(View view) {
        String tvFilter1Text = binding.tvFilter1.getText().toString().equals("전체")?"":binding.tvFilter1.getText().toString();
        String tvFilter2Text = binding.tvFilter2.getText().toString().equals("전체")?"":binding.tvFilter2.getText().toString();
        String tvFilter3Text = binding.tvFilter3.getText().toString().equals("전체")?"":binding.tvFilter3.getText().toString();

        filterVO = new FilterVO(tvFilter1Text, tvFilter2Text, tvFilter3Text);
        Intent intent = new Intent();
        intent.putExtra(CATEGORY_INFO, filterVO);
        setResult(RESULT_OK, intent);
        finish();
    }

    private String textCheck(String str) {
        if(str.equals(""))
            str="전체";
        else if(str.equals("전체"))
            str="";
        else
            str=str.split(" ")[0];

        return str;
    }

    private void requestWOC_01() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.woc(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).WOC_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST",
                mUser.Value.MEM_CID,
                "",
                "",
                "",
                ""
        ).enqueue(new Callback<WorkerCode_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<WorkerCode_Model> call, Response<WorkerCode_Model> response) {
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

                            Response<WorkerCode_Model> response = (Response<WorkerCode_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if (response.body().Data.get(0).Validation) {
                                        bindingCategory(response.body().Data);
                                    } else {
                                        BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ErrorCode);
                                    }
                                }else{
                                    bindingCategory(new  ArrayList<WorkerCodeVO>());
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
            public void onFailure(Call<WorkerCode_Model> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
            }
        });
    }

    private void bindingCategory(ArrayList<WorkerCodeVO> data) {

        String[] tempList1= new String[data.size()];
        String[] tempList2= new String[data.size()];
        String[] tempList3= new String[data.size()];

        for (int i = 0; i < data.size(); i++) {
            tempList1[i] = data.get(i).WOC_02;
            tempList2[i] = data.get(i).WOC_09;
            tempList3[i] = data.get(i).WOC_10;
        }

        tempList1 = new HashSet<String>(Arrays.asList(tempList1)).toArray(new String[0]);
        tempList2 = new HashSet<String>(Arrays.asList(tempList2)).toArray(new String[0]);
        tempList3 = new HashSet<String>(Arrays.asList(tempList3)).toArray(new String[0]);

        mCategory1 = new String[tempList1.length + 1];
        mCategory2 = new String[tempList2.length + 1];
        mCategory3 = new String[tempList3.length + 1];

        mCategory1[0] = "전체";
        mCategory2[0] = "전체";
        mCategory3[0] = "전체";

        for (int i = 0; i < tempList1.length; i++) {
            mCategory1[i + 1] = tempList1[i];
        }
        for (int i = 0; i < tempList2.length; i++) {
            mCategory2[i + 1] = tempList2[i];
        }
        for (int i = 0; i < tempList3.length; i++) {
            mCategory3[i + 1] = tempList3[i];
        }
    }


}