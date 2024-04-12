package kr.hmit.dmjs.ui.Cusomer.Popups;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityClientNameListBinding;

import kr.hmit.dmjs.databinding.ActivityMemberListBinding;
import kr.hmit.dmjs.model.response.MEM_Model;
import kr.hmit.dmjs.model.vo.MEM_VO;
import kr.hmit.dmjs.network.Http;

import kr.hmit.dmjs.ui.Cusomer.model.MultiItemVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberListActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8011;
    public static final String CLIENT_LIST = "CLIENT_LIST";

    private ActivityMemberListBinding binding;

    private MemberListAdapter mAdapter;
    private ArrayList<MEM_VO> mListTotal;
    private ArrayList<MEM_VO> mListSearch;
    private String filterData;
    String GUBUN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMemberListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        GUBUN = (String) intent.getExtras().get("GUBUN");
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

        mAdapter = new MemberListAdapter(mContext,mListTotal);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

        requestMEM_Read();
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
            MEM_VO vo = mListTotal.get(i);

            if (vo.MEM_02.toUpperCase().contains(strSearch)) {
                mListSearch.add(vo);
            }
        }

        mAdapter.update(mListSearch);
    }

    private void requestMEM_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.mem(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).MEM_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST",
                ""

        ).enqueue(new Callback<MEM_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<MEM_Model> call, Response<MEM_Model> response) {
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

                            Response<MEM_Model> response = (Response<MEM_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    bindingData(response.body().Data);
                                }else{
                                    bindingData(new  ArrayList<MEM_VO>());
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
            public void onFailure(Call<MEM_Model> call, Throwable t) {
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
    private void bindingData(ArrayList<MEM_VO> data) {
        mListTotal = data;

        mListSearch.clear();
        for (int i = 0; i < mListTotal.size(); i++) {
            mListSearch.add(mListTotal.get(i));
        }
        mAdapter.update(mListTotal);
    }

    private void onItemClickGoInfo(View view, int position) {   // intent back!!!

        Intent intent = new Intent();
        intent.putExtra(CLIENT_LIST, mListSearch.get(position));

        if(GUBUN.equals("GCM09"))
            setResult(9, intent);

        else if(GUBUN.equals("GCM11"))
            setResult(11, intent);

        else if(GUBUN.equals("GCM15"))
            setResult(15, intent);

        finish();
    }

}
