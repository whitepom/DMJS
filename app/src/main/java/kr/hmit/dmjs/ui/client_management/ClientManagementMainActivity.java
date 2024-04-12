package kr.hmit.dmjs.ui.client_management;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityClientManagementMainBinding;

import kr.hmit.dmjs.model.response.CLT_Model;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.client_management.adapter.ClientManagementListAdapter;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientManagementMainActivity extends BaseActivity {

    private ActivityClientManagementMainBinding binding;

    private ClientManagementListAdapter mAdapter;
    private ArrayList<CLT_VO> mListTotal;
    private ArrayList<CLT_VO> mListSearch;
    private String filterData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClientManagementMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.imgWriteWork.setOnClickListener(this::onClickGoWriteWork);
        binding.imgFilter.setOnClickListener(this::onClickGoFilter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        binding.imgSearch.setOnClickListener(this::onClickSearch);

        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            switch (actionId) {
                case EditorInfo.IME_ACTION_SEARCH:
                    onClickSearch(v);
                    break;
                default:
                    return false;
            }

            return true;
        });


        if(mSettings.Value.ClientManagementBKM){
          //  binding.imgBookMark.setSelected(true);
        }else{
       //     binding.imgBookMark.setSelected(false);
        }
     //   binding.imgBookMark.setOnClickListener(this::onClickBookMark);
    }

    @Override
    protected void initialize() {
        mListTotal = new ArrayList<>();
        mListSearch = new ArrayList<>();

        filterData ="";

        mAdapter = new ClientManagementListAdapter(mContext,mListTotal);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

        requestCLT_Read();
    }
    private void onClickBookMark(View v) {
        if(mSettings.Value.ClientManagementBKM){
            requestSaveBMK("M_DELETE");
            mSettings.Value.ClientManagementBKM=false;
            mSettings.Value.menuList.remove("10");
        }else{
            requestSaveBMK("M_INSERT");
            mSettings.Value.ClientManagementBKM=true;
            mSettings.Value.menuList.add("10");
        }

       // binding.imgBookMark.setSelected(mSettings.Value.ClientManagementBKM);
    }

    private void onClickSearch(View v) {
        String strSearch = binding.etSearch.getText().toString().toUpperCase().trim();

        mListSearch.clear();

        for (int i = 0; i < mListTotal.size(); i++) {
            CLT_VO vo = mListTotal.get(i);

            if (vo.CLT_02.toUpperCase().contains(strSearch)
                    || vo.CLT_40.toUpperCase().contains(strSearch)) {
                mListSearch.add(vo);
            }
        }

        mAdapter.update(mListSearch);
    }
    public void requestSaveBMK(String GUBUN) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        Http.main(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).BMK_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                GUBUN,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                "Z02001002001",
                "",
                "Z02001",
                "002",
                "001",
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
                    }
                }.sendMessage(msg);
            }


            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                call.cancel();
            }
        });
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
                "M_NGMLIST",
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
                                    bindingData(response.body().Data);
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

    /**
     * 서버 데이터 바인딩
     *
     * @param data
     */
    private void bindingData(ArrayList<CLT_VO> data) {
        mListTotal = data;

        mAdapter.update(mListTotal);

        binding.imgSearch.performClick();

    }

    private void onItemClickGoInfo(View view, int position) {

        Intent intent = new Intent(mContext, ClientManagementDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("CLTData",mListSearch.get(position));
        mActivity.startActivityForResult(intent, ClientManagementDetailActivity.REQUEST_CODE);

    }
    private void onClickGoWriteWork(View v) {

        Intent intent = new Intent(mContext, ClientManagementAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, ClientManagementAddActivity.REQUEST_CODE);

    }
    private void onClickGoFilter(View v) {

        Intent intent = new Intent(mContext, ClientManagementFilterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("filterData", filterData);
        mActivity.startActivityForResult(intent, ClientManagementFilterActivity.REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 담당자 추가
        if ((requestCode == ClientManagementAddActivity.REQUEST_CODE||requestCode ==ClientManagementDetailActivity.REQUEST_CODE) && resultCode == RESULT_OK) {
            requestCLT_Read();
        }else if (requestCode == ClientManagementFilterActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            filterData = data.getStringExtra("filterData");
            requestCLT_Read();
        }
    }
}
