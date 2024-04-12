package kr.hmit.dmjs.ui.stockage_list;

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

import kr.hmit.base.settings.SettingsKey;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityStockageListMainBinding;
import kr.hmit.dmjs.model.response.ProductionInfo_Model;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.login.LoginActivity;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import kr.hmit.dmjs.ui.stockage_list.model.FilterVO;
import kr.hmit.dmjs.ui.stockage_list.adapter.StockageListAdapter;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockageListMainActivity extends BaseActivity {

    private ActivityStockageListMainBinding binding;

    private StockageListAdapter mAdapter;
    private ArrayList<ProductionInfoVO> mListTotal;
    private ArrayList<ProductionInfoVO> mListSearch;
    private FilterVO filterVO;

    private Calendar mCalRequest1;
    private Calendar mCalRequest2;
    private SimpleDateFormat sdfFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStockageListMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.imgFilter.setOnClickListener(this::onClickGoFilter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        binding.etSearch.setOnEditorActionListener(this::onSearch);
        binding.imgSearch.setOnClickListener(this::onClickSearch);
        binding.imgGoTop.setOnClickListener(this::onClickGoTop);


    }
    private void onClickGoTop(View v) {
        binding.recyclerView.smoothScrollToPosition(0);
    }
    @Override
    protected void initialize() {

        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest1 = Calendar.getInstance();
        mCalRequest2 = Calendar.getInstance();
        mCalRequest2.add(Calendar.MONTH,-6);
        //filterVO =new FilterVO("",sdfFormat.format(mCalRequest2.getTime()).replaceAll("-",""),sdfFormat.format(mCalRequest1.getTime()).replaceAll("-",""));
        filterVO =new FilterVO("","",sdfFormat.format(mCalRequest1.getTime()).replaceAll("-",""));


        mListTotal = new ArrayList<>();
        mListSearch = new ArrayList<>();

        mAdapter = new StockageListAdapter(mContext,mListTotal);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

        requestOOK_Read();
    }

    @Override
    protected void onResume() {
        super.onResume();
     //   requestDAH_Read();
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

        mListSearch.clear();

        for (int i = 0; i < mListTotal.size(); i++) {
            ProductionInfoVO vo = mListTotal.get(i);

            if (vo.DAH_02.toUpperCase().contains(strSearch)
               ||vo.CDO_03.toUpperCase().contains(strSearch)
               ||vo.DAH_01.toUpperCase().contains(strSearch)) {
                mListSearch.add(vo);
            }
        }

        mAdapter.update(mListSearch);
    }


    private void onItemClickGoInfo(View view, int position) {
        Intent intent = new Intent(mContext, StockageListDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("ookVO",mListSearch.get(position));
        mActivity.startActivityForResult(intent, StockageListDetailActivity.REQUEST_CODE);
    }

    private void onClickGoFilter(View v) {
        Intent intent = new Intent(mContext, StockageListFilterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("filterData", filterVO);
        mActivity.startActivityForResult(intent, StockageListFilterActivity.REQUEST_CODE);
    }


    private void requestOOK_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.ook(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).OOK_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST2",
                mUser.Value.MEM_CID,
                filterVO.OOK_02_ST,
                filterVO.OOK_02_ED,
                filterVO.OOK_04 //

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
                                        toast("동일계정 접속 > 로그인 페이지로 이동합니다");  // asdfasdfasdf
                                        mSettings.putBooleanItem(SettingsKey.AutoLogin,false);
                                        Intent intent = new Intent(mContext, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("simultaneous_connect",mUser.Value.MEM_01.toString());
                                        startActivity(intent);
                                        finish();
                                     //   BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
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

        mAdapter.update(mListTotal);

        binding.imgSearch.performClick();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == StockageListFilterActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            filterVO = (FilterVO) data.getSerializableExtra(StockageListFilterActivity.CATEGORY_INFO);
            requestOOK_Read();
        }
//        else if (requestCode == StockageListDetailActivity.REQUEST_CODE && resultCode == RESULT_OK) {
//            requestDAH_Read();
//        }

    }
}
