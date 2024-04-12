package kr.hmit.dmjs.ui.PurchaseAbalone;

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

import kr.hmit.dmjs.databinding.ActivityPurchaseMainBinding;
import kr.hmit.dmjs.model.response.NGG_Model;
import kr.hmit.dmjs.model.response.ODM_Model;
import kr.hmit.dmjs.model.vo.NGG_VO;

import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.PurchaseAbalone.adapter.PurchaseMainListAdapter;

import kr.hmit.dmjs.ui.PurchaseAbalone.vo.FilterVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PurchaseMainActivity extends BaseActivity {


    private ActivityPurchaseMainBinding binding;

    private PurchaseMainListAdapter mAdapter;
    private ArrayList<NGG_VO> mListTotal;
    private ArrayList<NGG_VO> mListSearch;
    private FilterVO filterVO;

    private Calendar mCalRequest1;
    private Calendar mCalRequest2;
    private SimpleDateFormat sdfFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPurchaseMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.imgWriteOrder.setOnClickListener(this::onClickGoWriteOrder);
        binding.imgFilter.setOnClickListener(this::onClickGoFilter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        binding.etSearch.setOnEditorActionListener(this::onSearch);
        binding.imgSearch.setOnClickListener(this::onClickSearch);

    }

    @Override
    protected void initialize() {
        sdfFormat = new SimpleDateFormat("yyyyMMdd");
        mCalRequest1 = Calendar.getInstance();
        mCalRequest1.add(Calendar.MONTH,1);
        mCalRequest2 = Calendar.getInstance();
        mCalRequest2.add(Calendar.MONTH,-1);
        filterVO =new FilterVO("","",sdfFormat.format(mCalRequest2.getTime()),sdfFormat.format(mCalRequest1.getTime()),"");

        mListTotal = new ArrayList<>();
        mListSearch = new ArrayList<>();

        mAdapter = new PurchaseMainListAdapter(mContext,mListTotal);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);

        binding.recyclerView.setAdapter(mAdapter);
        requestNGG_Read();
        
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 목록 조회
     */
    private void requestNGG_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.ngg(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).NGG_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST",
                mUser.Value.MEM_CID,
                "",
                filterVO.NGG_02_ST,
                filterVO.NGG_02_ED,
                filterVO.CLT_01,
                filterVO.DAH_01,
                filterVO.NGG_YN
        ).enqueue(new Callback<NGG_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<NGG_Model> call, Response<NGG_Model> response) {
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

                            Response<NGG_Model> response = (Response<NGG_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if (response.body().Data.get(0).Validation) {
                                        bindingData(response.body().Data);
                                    } else {
                                        BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                }else{
                                    bindingData(new  ArrayList<NGG_VO>());
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
            public void onFailure(Call<NGG_Model> call, Throwable t) {
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
    private void bindingData(ArrayList<NGG_VO> data) {
        mListTotal = data;

        mAdapter.update(mListTotal);

        binding.imgSearch.performClick();
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
            NGG_VO vo = mListTotal.get(i);

            if (vo.NGG_03_NM.toUpperCase().contains(strSearch)) {
                mListSearch.add(vo);
            }
        }

        mAdapter.update(mListSearch);
    }


    private void onItemClickGoInfo(View view, int position) {
        Intent intent = new Intent(mContext, PurchaseDetailActivity.class);
        intent.putExtra("nggVO", mListSearch.get(position));
        mActivity.startActivityForResult(intent, PurchaseDetailActivity.REQUEST_CODE);
    }
    private void onClickGoWriteOrder(View v) {
        Intent intent = new Intent(mContext, PurchaseMainAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, PurchaseMainAddActivity.REQUEST_CODE);

    }
    private void onClickGoFilter(View v) {
        Intent intent = new Intent(mContext, PurchaseMainFilterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("filterData", filterVO);
        mActivity.startActivityForResult(intent, PurchaseMainFilterActivity.REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PurchaseMainFilterActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            filterVO = (FilterVO) data.getSerializableExtra(PurchaseMainFilterActivity.CATEGORY_INFO);
            requestNGG_Read();
        }else if ((requestCode == PurchaseMainAddActivity.REQUEST_CODE||requestCode == PurchaseDetailActivity.REQUEST_CODE) && resultCode == RESULT_OK) {
            requestNGG_Read();
        }

    }
}
