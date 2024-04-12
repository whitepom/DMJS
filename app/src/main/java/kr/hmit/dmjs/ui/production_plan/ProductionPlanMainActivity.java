package kr.hmit.dmjs.ui.production_plan;

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

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityProductionPlanMainBinding;
import kr.hmit.dmjs.model.response.MSM_Model;
import kr.hmit.dmjs.model.vo.MSM_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.production_plan.adapter.ProductionPlanListAdapter;
import kr.hmit.dmjs.ui.work_management.model.FilterVO;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductionPlanMainActivity extends BaseActivity {

    private ActivityProductionPlanMainBinding binding;

    private ProductionPlanListAdapter mAdapter;
    private ArrayList<MSM_VO> mListTotal;
    private ArrayList<MSM_VO> mListSearch;
    private FilterVO filterVO;
    private Calendar mCalRequest1;
    private SimpleDateFormat sdfFormat;
    private SimpleDateFormat sdfFormat2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductionPlanMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {

        binding.imgBack.setOnClickListener(v -> finish());
        binding.imgWriteWork.setOnClickListener(this::onClickGoWriteWork);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        binding.imgPre.setOnClickListener(this::onClickPre);
        binding.imgNext.setOnClickListener(this::onClickNext);
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
        if(mSettings.Value.ProductionPlanBKM){
         //   binding.imgBookMark.setSelected(true);
        }else{
      //      binding.imgBookMark.setSelected(false);
        }
      //  binding.imgBookMark.setOnClickListener(this::onClickBookMark);

    }

    private void onClickNext(View view) {
        mCalRequest1.add(Calendar.MONTH,1);
        binding.tvDate.setText(sdfFormat.format(mCalRequest1.getTime()));
        requestMSM_Read(sdfFormat2.format(mCalRequest1.getTime()));
    }
    private void onClickSearch(View v) {
        String strSearch = binding.etSearch.getText().toString().toUpperCase().trim();

        mListSearch.clear();

        for (int i = 0; i < mListTotal.size(); i++) {
            MSM_VO vo = mListTotal.get(i);

            if (vo.MSMS_03.toUpperCase().contains(strSearch)) {
                mListSearch.add(vo);
            }
        }

        mAdapter.update(mListSearch);
    }

    private void onClickPre(View view) {
        mCalRequest1.add(Calendar.MONTH,-1);
        binding.tvDate.setText(sdfFormat.format(mCalRequest1.getTime()));
        requestMSM_Read(sdfFormat2.format(mCalRequest1.getTime()));
    }

    @Override
    protected void initialize() {
        mListTotal = new ArrayList<>();
        mListSearch = new ArrayList<>();

        mAdapter = new ProductionPlanListAdapter(mContext,mListTotal);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

        //첫 시작 날짜
        sdfFormat = new SimpleDateFormat("yyyy년MM월");
        sdfFormat2 = new SimpleDateFormat("yyyyMM");
        mCalRequest1 = Calendar.getInstance();
        binding.tvDate.setText(sdfFormat.format(mCalRequest1.getTime()));

        requestMSM_Read(sdfFormat2.format(mCalRequest1.getTime()));

    }
    public void requestSaveBMK(String GUBUN) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        Http.main(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).BMK_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                GUBUN,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                "Z02001007001",
                "",
                "Z02001",
                "007",
                "001",
                mUser.Value.MEM_01
        ).enqueue(new Callback<BaseModel>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                Message msg = new Message();
                msg.obj = response;
                msg.what = 100;

                //=====================
                // response callback
                //=====================
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                    }
                }.sendMessage(msg);
            }


            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void requestMSM_Read(String tvDate) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.msm(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).MSM_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST",
                mUser.Value.MEM_CID,
                tvDate,
                ""
        ).enqueue(new Callback<MSM_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<MSM_Model> call, Response<MSM_Model> response) {
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

                            Response<MSM_Model> response = (Response<MSM_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if (response.body().Data.get(0).Validation) {
                                        binding.message.setVisibility(View.INVISIBLE);
                                        bindingData(response.body().Data);
                                    } else {
                                        BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                }else{
                                    bindingData(new  ArrayList<MSM_VO>());
                                    binding.message.setVisibility(View.VISIBLE);
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
            public void onFailure(Call<MSM_Model> call, Throwable t) {
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
    private void bindingData(ArrayList<MSM_VO> data) {
        mListTotal = data;

        mAdapter.update(mListTotal);

        binding.imgSearch.performClick();

    }
    private void onItemClickGoInfo(View view, int position) {
        Intent intent = new Intent(mContext, ProductionDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("msmVO", mListSearch.get(position));
        mActivity.startActivityForResult(intent, ProductionDetailActivity.REQUEST_CODE);
    }
    private void onClickGoWriteWork(View v){
        Intent intent = new Intent(mContext, ProductionAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, ProductionAddActivity.REQUEST_CODE);
    }
    private void onClickBookMark(View v) {
        if(mSettings.Value.ProductionPlanBKM){
            requestSaveBMK("M_DELETE");
            mSettings.Value.ProductionPlanBKM=false;
            mSettings.Value.menuList.remove("50");
        }else{
            requestSaveBMK("M_INSERT");
            mSettings.Value.ProductionPlanBKM=true;
            mSettings.Value.menuList.add("50");
        }

     //   binding.imgBookMark.setSelected(mSettings.Value.ProductionPlanBKM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == ProductionAddActivity.REQUEST_CODE||requestCode == ProductionDetailActivity.REQUEST_CODE) && resultCode == RESULT_OK) {
            requestMSM_Read(sdfFormat2.format(mCalRequest1.getTime()));
        }
    }
}
