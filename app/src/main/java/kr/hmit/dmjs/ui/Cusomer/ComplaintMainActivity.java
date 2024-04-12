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

import java.util.ArrayList;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.base.settings.SettingsKey;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityComplaintMainBinding;
import kr.hmit.dmjs.databinding.ActivityNoticeMain2Binding;
import kr.hmit.dmjs.model.response.GCM_Model;
import kr.hmit.dmjs.model.vo.GCM_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.Cusomer.adapter.ComplaintMainListAdapter;
import kr.hmit.dmjs.ui.login.LoginActivity;
import kr.hmit.dmjs.ui.notice.NoticeAddActivity;
import kr.hmit.dmjs.ui.notice.NoticeDetailActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComplaintMainActivity extends BaseActivity {
    private ActivityComplaintMainBinding binding;

    //=============================
    // endregion // view
    //=============================

    //=============================
    // region // variable
    //=============================

    private ComplaintMainListAdapter mAdapter;
    private ArrayList<GCM_VO> mListTotal;
    private ArrayList<GCM_VO> mListSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComplaintMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();


    }
    @Override
    protected void initLayout() {

        binding.imgBack.setOnClickListener(v -> finish());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        binding.imgGoTop.setOnClickListener(this::onClickGoTop);
        binding.imgSearch.setOnClickListener(this::onClickSearch);
        binding.etSearch.setOnEditorActionListener(this::onSearch);
        binding.imgAdd.setOnClickListener(this::onClickAdd);
    }




    @Override
    protected void onResume() {
        super.onResume();
        requestGCM_Read();
    }

    @Override
    protected void initialize() {

        mListTotal = new ArrayList<>();
        mListSearch = new ArrayList<>();

        mAdapter = new ComplaintMainListAdapter(mContext, mListTotal);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);


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

    private void requestGCM_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {

            BaseAlert.show(mContext, R.string.network_error_1);
            return;

        }

        openLoadingBar();

        Http.gcm(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).GCM_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST",
                mUser.Value.MEM_CID,
                "",
                "","","","",""


        ).enqueue(new Callback<GCM_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<GCM_Model> call, Response<GCM_Model> response) {
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
                            Response<GCM_Model> response = (Response<GCM_Model>) msg.obj;
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
                                        //BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ErrorCode);
                                    }
                                }else{
                                    bindingData(new  ArrayList<GCM_VO>());
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
            public void onFailure(Call<GCM_Model> call, Throwable t) {
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
    private void bindingData(ArrayList<GCM_VO> data) {
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
    private void onClickAdd(View view) {
        Intent intent = new Intent(mContext, ComplaintAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, ComplaintAddActivity.REQUEST_CODE);

    }

    private void onItemClickGoInfo(View view, int position) {

        Intent intent = new Intent(mContext, ComplaintDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("GCMdata",mListSearch.get(position));
        mActivity.startActivityForResult(intent, ComplaintDetailActivity.REQUEST_CODE);

    }

    private void onClickGoTop(View v) {
        binding.recyclerView.smoothScrollToPosition(0);
    }

    /**
     * 검색 버튼 클릭 시
     *
     * @param v
     */
    private void onClickSearch(View v) {
        String strSearch = binding.etSearch.getText().toString().toUpperCase().trim();

        mListSearch.clear();

        for (int i = 0; i < mListTotal.size(); i++) {
            GCM_VO vo = mListTotal.get(i);

            if (vo.CLT_02.toUpperCase().contains(strSearch))
            {
                mListSearch.add(vo);
            }
        }

        mAdapter.update(mListSearch);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

}
