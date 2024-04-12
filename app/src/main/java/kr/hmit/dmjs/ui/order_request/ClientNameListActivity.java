package kr.hmit.dmjs.ui.order_request;

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
import kr.hmit.dmjs.databinding.ActivityClientNameListBinding;
import kr.hmit.dmjs.model.response.CLT_Model;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.client_management.adapter.ClientManagementListAdapter;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientNameListActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8011;
    public static final String CLIENT_LIST = "CLIENT_LIST";

    private ActivityClientNameListBinding binding;

    private ClientManagementListAdapter mAdapter;
    private ArrayList<CLT_VO> mListTotal;
    private ArrayList<CLT_VO> mListSearch;
    private String filterData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClientNameListBinding.inflate(getLayoutInflater());
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

        mAdapter = new ClientManagementListAdapter(mContext,mListTotal);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

        requestCLT_Read();
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
            CLT_VO vo = mListTotal.get(i);

            if (vo.CLT_02.toUpperCase().contains(strSearch)) {
                mListSearch.add(vo);
            }
        }

        mAdapter.update(mListSearch);
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
                "LIST_M",
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

        mListSearch.clear();
        for (int i = 0; i < mListTotal.size(); i++) {
            mListSearch.add(mListTotal.get(i));
        }
        mAdapter.update(mListTotal);
    }

    private void onItemClickGoInfo(View view, int position) {   // intent back!!!

        Intent intent = new Intent();
        intent.putExtra(CLIENT_LIST, mListSearch.get(position)); // getintent
        setResult(RESULT_OK, intent);
        finish();
    }

}
