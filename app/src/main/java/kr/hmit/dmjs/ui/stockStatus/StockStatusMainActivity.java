package kr.hmit.dmjs.ui.stockStatus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.EditorInfo;

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
import kr.hmit.base.settings.SettingsKey;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityStockstatusMainBinding;
import kr.hmit.dmjs.model.response.OOK_Model;
import kr.hmit.dmjs.model.vo.OOK_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.login.LoginActivity;
import kr.hmit.dmjs.ui.stockStatus.adapter.StockStatusAdapter;
import kr.hmit.dmjs.ui.stockStatus.model.FilterVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StockStatusMainActivity extends BaseActivity {

    private ActivityStockstatusMainBinding binding;

    private StockStatusAdapter mAdapter;
    private ArrayList<OOK_VO> mListTotal;
    private ArrayList<OOK_VO> mListSearch;
    private FilterVO filterVO;

    private Calendar mCalRequest1;
    private Calendar mCalRequest2;
    private SimpleDateFormat sdfFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStockstatusMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
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
    }

    @Override
    protected void initialize() {

        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest1 = Calendar.getInstance();
        mCalRequest1.add(Calendar.DATE,1);
        mCalRequest2 = Calendar.getInstance();
        mCalRequest2.add(Calendar.DATE,-3);
        filterVO =new FilterVO(sdfFormat.format(mCalRequest2.getTime()),sdfFormat.format(mCalRequest1.getTime()),"");

        mListTotal = new ArrayList<>();
        mListSearch = new ArrayList<>();

        mAdapter = new StockStatusAdapter(mContext, mListTotal);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

        requestOOK_Read();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void onClickSearch(View view) {
        String strSearch = binding.etSearch.getText().toString().toUpperCase().trim();

        mListSearch.clear();

        for (int i = 0; i < mListTotal.size(); i++) {
            OOK_VO vo = mListTotal.get(i);

            if (vo.OOK_04.toUpperCase().contains(strSearch) || vo.OOK_04_NM.toUpperCase().contains(strSearch)) {
                mListSearch.add(vo);
            }
        }
        mAdapter.update(mListSearch);
    }

    private void onItemClickGoInfo(View view, int position) {

    }

    private void onClickGoFilter(View v) {
        Intent intent = new Intent(mContext, StockStatusFilterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("filterData", filterVO);
        mActivity.startActivityForResult(intent, StockStatusFilterActivity.REQUEST_CODE);
    }

    private void requestOOK_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.ook(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).OOK_Read2(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "LIST",
                mUser.Value.MEM_CID,
                filterVO.OOK_02_ST,
                filterVO.OOK_02_ED,
                filterVO.OOK_04

        ).enqueue(new Callback<OOK_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<OOK_Model> call, Response<OOK_Model> response) {
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
                            Response<OOK_Model> response = (Response<OOK_Model>) msg.obj;
                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if (response.body().Data.get(0).Validation) {
                                        bindingData(response.body().Data);
                                    } else {
                                        toast("동일계정 접속 > 로그인 페이지로 이동합니다");  // asdfasdfasdf
                                        mSettings.putBooleanItem(SettingsKey.AutoLogin,false);
                                        Intent intent = new Intent(mContext, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("simultaneous_connect",mUser.Value.MEM_01.toString());
                                        startActivity(intent);
                                        finish();
                                      //  BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                }else{
                                    bindingData(new  ArrayList<OOK_VO>());
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
            public void onFailure(Call<OOK_Model> call, Throwable t) {
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
    private void bindingData(ArrayList<OOK_VO> data) {
        mListTotal = data;

        mAdapter.update(mListTotal);

        binding.imgSearch.performClick();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == StockStatusFilterActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            filterVO = (FilterVO) data.getSerializableExtra(StockStatusFilterActivity.CATEGORY_INFO);
            requestOOK_Read();
        }
//        else if (requestCode == StockageListDetailActivity.REQUEST_CODE && resultCode == RESULT_OK) {
//            requestDAH_Read();
//        }

    }
}
