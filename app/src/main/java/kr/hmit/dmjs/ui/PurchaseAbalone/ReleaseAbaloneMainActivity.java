package kr.hmit.dmjs.ui.PurchaseAbalone;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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
import kr.hmit.dmjs.R;

import kr.hmit.dmjs.databinding.ActivityAbaloneReleaseMainBinding;

import kr.hmit.dmjs.model.response.NGO_Model;
import kr.hmit.dmjs.model.vo.NGO_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.PurchaseAbalone.adapter.AbaloneReleaseListAdapter;


import kr.hmit.dmjs.ui.PurchaseAbalone.vo.FilterVO2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReleaseAbaloneMainActivity extends BaseActivity {

    private ActivityAbaloneReleaseMainBinding binding;

    private AbaloneReleaseListAdapter mAdapter;
    private ArrayList<NGO_VO> mListTotal;
    private ArrayList<NGO_VO> mListSearch;

    private FilterVO2 filterVO;

    private Calendar mCalRequest1;
    private Calendar mCalRequest2;
    private SimpleDateFormat sdfFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAbaloneReleaseMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.imgAdd.setOnClickListener(this::onClickGoWriteWork);
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
        sdfFormat = new SimpleDateFormat("yyyyMMdd");
        mCalRequest1 = Calendar.getInstance();
        mCalRequest1.add(Calendar.MONTH,1);
        mCalRequest2 = Calendar.getInstance();
        mCalRequest2.add(Calendar.MONTH,-1);
        filterVO =new FilterVO2("","",sdfFormat.format(mCalRequest2.getTime()),sdfFormat.format(mCalRequest1.getTime()),"");

        mListTotal = new ArrayList<>();
        mListSearch = new ArrayList<>();

        mAdapter = new AbaloneReleaseListAdapter(mContext,mListTotal);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

        requestNGO_Read();
    }
    private void requestNGO_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {

            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.ngo(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).NGO_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST",
                mUser.Value.MEM_CID,
                "",
                filterVO.NGO_02_ST,
                filterVO.NGO_02_ED,
                "",
                "",
                ""
        ).enqueue(new Callback<NGO_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<NGO_Model> call, Response<NGO_Model> response) {
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

                            Response<NGO_Model> response = (Response<NGO_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){

                                    System.out.println(response.body().Data.get(0).Validation);

                                    if (response.body().Data.get(0).Validation) {
                                        bindingData(response.body().Data);
                                    } else {
                                        BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                }else{
                                    bindingData(new  ArrayList<NGO_VO>());
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
            public void onFailure(Call<NGO_Model> call, Throwable t) {
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
    private void bindingData(ArrayList<NGO_VO> data) {
        mListTotal = data;
        mAdapter.update(mListTotal);
        binding.imgSearch.performClick();

    }

    private void onItemClickGoInfo(View view, int position) {
        Intent intent = new Intent(mContext, AbaloneReleaseDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("NGOData",mListSearch.get(position));
        mActivity.startActivityForResult(intent, AbaloneReleaseDetailActivity.REQUEST_CODE);

    }
    private void onClickGoWriteWork(View v) {
        String[] tempList = {"일반출고","다량출고"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("출고유형")
                .setItems(tempList, (dialog, which) -> {
                    if(which==0){
                        Intent intent = new Intent(mContext, AbaloneReleaseAddActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        mActivity.startActivityForResult(intent, AbaloneReleaseAddActivity.REQUEST_CODE);
                    }else{
                        Intent intent = new Intent(mContext, AbaloneReleaseMultiAddActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        mActivity.startActivityForResult(intent, AbaloneReleaseMultiAddActivity.REQUEST_CODE);
                    }
                }).setCancelable(false).create();

        builder.show();
    }
    private void onClickGoFilter(View v){
        Intent intent = new Intent(mContext, AbaloneReleaseFilterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("filterData", filterVO);
        mActivity.startActivityForResult(intent, AbaloneReleaseFilterActivity.REQUEST_CODE);
    }
    private void onClickSearch(View v) {
        String strSearch = binding.etSearch.getText().toString().toUpperCase().trim();

        mListSearch.clear();

        for (int i = 0; i < mListTotal.size(); i++) {
            NGO_VO vo = mListTotal.get(i);

            if(vo.NGO_03_NM.toUpperCase().contains(strSearch)
            ||(vo.NGO_03==null?"":vo.NGO_03).toUpperCase().contains(strSearch)){
                mListSearch.add(vo);
            }
        }

        mAdapter.update(mListSearch);

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AbaloneReleaseFilterActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            filterVO = (FilterVO2) data.getSerializableExtra(AbaloneReleaseFilterActivity.CATEGORY_INFO);
            requestNGO_Read();
        }else if ((requestCode == AbaloneReleaseAddActivity.REQUEST_CODE||requestCode == AbaloneReleaseDetailActivity.REQUEST_CODE
                ||requestCode == AbaloneReleaseMultiAddActivity.REQUEST_CODE
        ) && resultCode == RESULT_OK) {
            requestNGO_Read();
        }

        // 담당자 추가
    }
}
