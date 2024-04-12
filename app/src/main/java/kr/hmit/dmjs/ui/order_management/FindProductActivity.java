package kr.hmit.dmjs.ui.order_management;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityFindProductBinding;
import kr.hmit.dmjs.model.response.ProductionInfo_Model;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.product_info.adapter.ProductionInfoListAdapter;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindProductActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8001;
    public static final String PRODUCT_LIST = "PRODUCT_LIST";

    private ActivityFindProductBinding binding;

    private ProductionInfoListAdapter mAdapter;
    private ArrayList<ProductionInfoVO> mListTotal;
    private ArrayList<ProductionInfoVO> mListSearch;
    private String filterData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
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

    }

    @Override
    protected void initialize() {
        mListTotal = new ArrayList<>();
        mListSearch = new ArrayList<>();

        filterData ="";

        mAdapter = new ProductionInfoListAdapter(mContext,mListTotal);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

        requestDAH_Read();
        binding.etSearch.performClick();
    }

    private void onClickSearch(View v) {
        String strSearch = binding.etSearch.getText().toString().toUpperCase().trim();

        if (strSearch.isEmpty()) {
            mAdapter.update(mListTotal);
            return;
        }

        mListSearch.clear();

        for (int i = 0; i < mListTotal.size(); i++) {
            ProductionInfoVO vo = mListTotal.get(i);

            if (vo.DAH_02.toUpperCase().contains(strSearch)) {
                mListSearch.add(vo);
            }
        }

        mAdapter.update(mListSearch);
    }

    private void requestDAH_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.dah(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).DAH_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_POPLIST",
                mUser.Value.MEM_CID,
                "",
                "R",
                "B",
                "S"
        ).enqueue(new Callback<ProductionInfo_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<ProductionInfo_Model> call, Response<ProductionInfo_Model> response) {
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
                            Response<ProductionInfo_Model> response = (Response<ProductionInfo_Model>) msg.obj;
                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if (response.body().Data.get(0).Validation) {
                                        bindingData(response.body().Data);
                                    } else {
                                        BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                }else{
                                    bindingData(new  ArrayList<ProductionInfoVO>());
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
            public void onFailure(Call<ProductionInfo_Model> call, Throwable t) {
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
    private void bindingData(ArrayList<ProductionInfoVO> data) {
        mListTotal = data;

        mListSearch.clear();
        for (int i = 0; i < mListTotal.size(); i++) {
            mListSearch.add(mListTotal.get(i));
        }
        mAdapter.update(mListTotal);
    }

    private void onItemClickGoInfo(View view, int position) {

        Intent intent = new Intent();
        intent.putExtra(PRODUCT_LIST, mListSearch.get(position));
        setResult(RESULT_OK, intent);
        finish();
    }

}

