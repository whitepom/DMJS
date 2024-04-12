package kr.hmit.dmjs.ui.popup.dah;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.base.settings.SettingsKey;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityDahFindBinding;
import kr.hmit.dmjs.databinding.ActivityFindProductBinding;
import kr.hmit.dmjs.model.response.ProductionInfo_Model;
import kr.hmit.dmjs.model.vo.ZAG_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.login.LoginActivity;
import kr.hmit.dmjs.ui.m017.filter.ZagFilterVO;
import kr.hmit.dmjs.ui.popup.dah.adapter.DahFindListAdapter;
import kr.hmit.dmjs.ui.product_info.adapter.ProductionInfoListAdapter;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DahFindActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8001;
    public static final String PRODUCT_LIST = "PRODUCT_LIST";

    private ActivityDahFindBinding binding;
    private DahFindListAdapter mAdapter;

    private ArrayList<ProductionInfoVO> mList;

    private ProductionInfoVO paramDahVo = new ProductionInfoVO();

    private HashMap<String, Object> paramMap = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDahFindBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
        initLayout();
    }
    @Override
    protected void initialize() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        binding.imgSearch.setOnClickListener(this::onClickSearch);
        binding.etSearch.setOnEditorActionListener(this::onSearch);

        mAdapter = new DahFindListAdapter();
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

        Intent intent = getIntent();
        paramDahVo = (ProductionInfoVO) intent.getExtras().get("DAH_VO");

        binding.etSearch.setText(paramDahVo.DAH_01);
    }
    @Override
    protected void initLayout() {

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
        String strSearch = binding.etSearch.getText().toString().toUpperCase().trim();
        DAH_Read(strSearch);
    }

    private void DAH_Read(String DAH_01) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        //기본파라미터
        paramMap = setParamMap("ZAG_ID", "M_POP_LIST");
        paramMap.put("DAH_01", DAH_01 );
        paramMap.put("DAH_03", paramDahVo.DAH_03);

        openLoadingBar();

        Http.dah(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).DAH_Read2(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<ArrayList<ProductionInfoVO>>() {
            @Override
            public void onResponse(Call<ArrayList<ProductionInfoVO>> call, Response<ArrayList<ProductionInfoVO>> response) {
                closeLoadingBar();
                ArrayList<ProductionInfoVO> data = response.body();

                if (response.isSuccessful()) {
                    if(data.size() > 0){
                        bindingData(data);
                    }else{
                        bindingData(new  ArrayList<ProductionInfoVO>());
                        BaseAlert.show(mContext,"검색결과가 없습니다." );
                    }
                } else {
                    BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ProductionInfoVO>> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
                t.printStackTrace();
            }
        });
    }

    private void bindingData(ArrayList<ProductionInfoVO> data) {
        mList = data;
        mAdapter.setList(data);
    }

    private void onItemClickGoInfo(View view, int position) {

        Intent intent = new Intent();
        intent.putExtra(PRODUCT_LIST, mList.get(position));
        setResult(RESULT_OK, intent);
        finish();
    }

}

