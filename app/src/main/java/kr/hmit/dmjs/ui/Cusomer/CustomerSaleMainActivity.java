package kr.hmit.dmjs.ui.Cusomer;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityCustomerSaleMainBinding;
import kr.hmit.dmjs.model.response.CLT_Model;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.Cusomer.adapter.CustomerSaleMainAdapter;
import kr.hmit.dmjs.ui.Cusomer.adapter.CustomerSaleMainListAdapter;
import kr.hmit.dmjs.ui.Cusomer.model.CustomerSaleFilterVO;
import kr.hmit.dmjs.ui.Cusomer.model.FilterVO;
import kr.hmit.dmjs.ui.order_management.OrderManagementAddActivity;
import kr.hmit.dmjs.ui.order_management.OrderManagementFilterActivity;
import kr.hmit.dmjs.ui.order_management.OrderManagementInfoMain;
import kr.hmit.dmjs.ui.release.ReleaseAddActivity;
import kr.hmit.dmjs.ui.release.ReleaseFilterActivity;
import kr.hmit.dmjs.ui.release.ReleaseInfoMain;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerSaleMainActivity extends BaseActivity {
    private ActivityCustomerSaleMainBinding binding;
    private CustomerSaleMainAdapter mAdapter;

    private ArrayList<CLT_VO> mList = new ArrayList<>();
    private CustomerSaleFilterVO filterVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerSaleMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.imgWriteNew.setOnClickListener(this::onClickNew);
        binding.imgFilter.setOnClickListener(this::onClickGoFilter);

        binding.etSearch.setOnEditorActionListener(this::onSearch);
        binding.imgGoTop.setOnClickListener(this::onClickGoTop);
        binding.imgSearch.setOnClickListener(this::onClickSearch);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
    }

    @Override
    protected void initialize() {

        mAdapter = new CustomerSaleMainAdapter();
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

    }
    private boolean onSearch(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            onClickSearch(v);
        } else {
            return false;
        }

        return true;
    }
    private void onClickSearch(View view) {
        requestCLT_Read();
    }
    private void onClickGoTop(View v) {
        binding.recyclerView.smoothScrollToPosition(0);
    }

    private void onItemClickGoInfo(View view, int position) {
        Intent intent = new Intent(mContext, CustomerSaleDetailActivity.class);
        intent.putExtra("cltVO", mList.get(position));
        intent.putExtra("filterVO",filterVO);
        mActivity.startActivityForResult(intent, CustomerSaleDetailActivity.REQUEST_CODE);
    }
    private void onClickNew(View v) {
        Intent intent = new Intent(mContext, CustomerSaleMasterAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        mActivity.startActivityForResult(intent, CustomerSaleMasterAddActivity.REQUEST_CODE);

    }
    private void onClickGoFilter(View v) {
        Intent intent = new Intent(mContext, CustomerSaleMainFilterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("filterData", filterVO);
        mActivity.startActivityForResult(intent, CustomerSaleMainFilterActivity.REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CustomerSaleMainFilterActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            filterVO = (CustomerSaleFilterVO) data.getSerializableExtra(CustomerSaleMainFilterActivity.REQUEST_INFO);
            requestCLT_Read();
        } else if ((requestCode == CustomerSaleMasterAddActivity.REQUEST_CODE || requestCode == CustomerSaleDetailActivity.REQUEST_CODE) && resultCode == RESULT_OK) {
            requestCLT_Read();
        }
    }


    private void requestCLT_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.clt(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).CLT_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST_CLT2",
                mUser.Value.MEM_CID,
                ""
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
                                        bindingData(response.body().Data);
                                    } else {
                                        BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                }else{
                                    bindingData(new  ArrayList<CLT_VO>());
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
            public void onFailure(Call<CLT_Model> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
            }
        });
    }

    private void bindingData(ArrayList<CLT_VO> data) {
        mList = data;
        mAdapter.setList(data);
    }
}
