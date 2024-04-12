package kr.hmit.dmjs.ui.release;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import kr.hmit.dmjs.databinding.ActivityReleasecurrentMainBinding;
import kr.hmit.dmjs.model.response.RUN_Model;
import kr.hmit.dmjs.model.vo.RUN_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.release.adapter.ReleaseCurrentListAdapter;
import kr.hmit.dmjs.ui.release.model.FilterVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReleaseCurrentActivity extends BaseActivity {
    private ActivityReleasecurrentMainBinding binding;
    private ReleaseCurrentListAdapter mAdapter;
    private ArrayList<RUN_VO> mListTotal;
    private ArrayList<RUN_VO> mListSearch;
    private FilterVO filterVO;

    private Calendar mCalRequest1;
    private Calendar mCalRequest2;
    private SimpleDateFormat sdfFormat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReleasecurrentMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.imgWriteNew.setOnClickListener(this::onClickNew);
        binding.imgFilter.setOnClickListener(this::onClickGoFilter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        binding.etSearch.setOnEditorActionListener(this::onSearch);
        binding.imgGoTop.setOnClickListener(this::onClickGoTop);
        binding.imgSearch.setOnClickListener(this::onClickSearch);
    }

    @Override
    protected void initialize() {
        sdfFormat = new SimpleDateFormat("yyyyMMdd");
        mCalRequest1 = Calendar.getInstance();
        mCalRequest1.set(Calendar.DAY_OF_MONTH,1);
        mCalRequest2 = Calendar.getInstance();
        filterVO =new FilterVO("",sdfFormat.format(mCalRequest1.getTime()),sdfFormat.format(mCalRequest2.getTime()));

        mListTotal = new ArrayList<>();
        mListSearch = new ArrayList<>();

        mAdapter = new ReleaseCurrentListAdapter(mContext,mListTotal);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);

        binding.recyclerView.setAdapter(mAdapter);
        requestRUN_Read();
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
            RUN_VO vo = mListTotal.get(i);

            if (
                    vo.CLT_02.toUpperCase().contains(strSearch)
            ) {
                mListSearch.add(vo);
            }
        }

        mAdapter.update(mListSearch);
    }
    private void onClickGoTop(View v) {
        binding.recyclerView.smoothScrollToPosition(0);
    }

    private void onItemClickGoInfo(View view, int position) {
        Intent intent = new Intent(mContext, ReleaseInfoMain.class);
        intent.putExtra("runVO", mListSearch.get(position));
        intent.putExtra("filterVO",filterVO);
        mActivity.startActivityForResult(intent, ReleaseInfoMain.REQUEST_CODE);
    }
    private void onClickNew(View v) {
        Intent intent = new Intent(mContext, ReleaseAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        mActivity.startActivityForResult(intent, ReleaseAddActivity.REQUEST_CODE);

    }
    private void onClickGoFilter(View v) {
        Intent intent = new Intent(mContext, ReleaseFilterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("filterData", filterVO);
        mActivity.startActivityForResult(intent, ReleaseFilterActivity.REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ReleaseFilterActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            filterVO = (FilterVO) data.getSerializableExtra(ReleaseFilterActivity.CATEGORY_INFO);
            requestRUN_Read();
        } else if ((requestCode == ReleaseAddActivity.REQUEST_CODE || requestCode == ReleaseInfoMain.REQUEST_CODE) && resultCode == RESULT_OK) {
            requestRUN_Read();
        }

    }


    private void requestRUN_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.run(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).RUN_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST_L",
                mUser.Value.MEM_CID,
                filterVO.CLT_01,
                filterVO.RUN_02_ST,
                filterVO.RUN_02_ED

        ).enqueue(new Callback<RUN_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<RUN_Model> call, Response<RUN_Model> response) {
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

                            Response<RUN_Model> response = (Response<RUN_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if (response.body().Data.get(0).Validation) {
                                        bindingData(response.body().Data);
                                    } else {
                                        BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                }else{
                                    bindingData(new  ArrayList<RUN_VO>());
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
            public void onFailure(Call<RUN_Model> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
            }
        });
    }

    private void bindingData(ArrayList<RUN_VO> data) {
        mListTotal = data;

        mAdapter.update(mListTotal);

        binding.imgSearch.performClick();
    }
}
