package kr.hmit.dmjs.ui.receive;
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
import kr.hmit.dmjs.databinding.ActivityConstructionsightListBinding;
import kr.hmit.dmjs.model.response.REM_Model;
import kr.hmit.dmjs.model.vo.REM_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.ui.receive.adapter.ConstructionSightListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class FindConstructionActivity extends BaseActivity {

    public static final int REQUEST_CODE = 1012;
    public static final String CONSTRUCTION_LIST = "CONSTRUCTION_LIST";

    private ActivityConstructionsightListBinding binding;
    private ConstructionSightListAdapter mAdapter;
    private ArrayList<REM_VO> mListTotal;
    private ArrayList<REM_VO> mListSearch;
    private String filterData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConstructionsightListBinding.inflate(getLayoutInflater());
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

        mAdapter = new ConstructionSightListAdapter(mContext,mListTotal);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

        requestREM_Read();
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
            REM_VO vo = mListTotal.get(i);

            if (vo.REM_03.toUpperCase().contains(strSearch)||vo.REM_04.toUpperCase().contains(strSearch)||vo.REM_16.toUpperCase().contains(strSearch)) {
                mListSearch.add(vo);
            }
        }

        mAdapter.update(mListSearch);
    }

    private void requestREM_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.rem(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).REM_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_POPLIST",
                mUser.Value.MEM_CID,
                ""

        ).enqueue(new Callback<REM_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<REM_Model> call, Response<REM_Model> response) {
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

                            Response<REM_Model> response = (Response<REM_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    bindingData(response.body().Data);
                                }else{
                                    bindingData(new  ArrayList<REM_VO>());
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
            public void onFailure(Call<REM_Model> call, Throwable t) {
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
    private void bindingData(ArrayList<REM_VO> data) {
        mListTotal = data;

        mListSearch.clear();
        for (int i = 0; i < mListTotal.size(); i++) {
            mListSearch.add(mListTotal.get(i));
        }
        mAdapter.update(mListTotal);
    }

    private void onItemClickGoInfo(View view, int position) {   // intent back!!!

        Intent intent = new Intent();
        intent.putExtra(CONSTRUCTION_LIST, mListSearch.get(position));
        setResult(RESULT_OK, intent);
        finish();
    }


}
