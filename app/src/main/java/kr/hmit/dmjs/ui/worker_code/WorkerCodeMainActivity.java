package kr.hmit.dmjs.ui.worker_code;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityWorkerCodeMainBinding;
import kr.hmit.dmjs.model.response.WorkerCode_Model;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.worker_code.model.FilterVO;
import kr.hmit.dmjs.ui.worker_code.adapter.WorkerCodeMainAdapter;
import kr.hmit.dmjs.ui.worker_code.model.WorkerCodeVO;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkerCodeMainActivity extends BaseActivity {
    //================================
    // region // view
    private ActivityWorkerCodeMainBinding binding;
    // endregion // view
    //================================


    //================================
    // region // variable
    private WorkerCodeMainAdapter mAdapter;
    private ArrayList<WorkerCodeVO> mListTotal;
    private ArrayList<WorkerCodeVO> mListSearch;
    private FilterVO filterVO;
    // endregion // variable
    //================================


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkerCodeMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        binding.imgGoTop.setOnClickListener(this::onClickGoTop);
        binding.imgSearch.setOnClickListener(this::onClickSearch);
        binding.imgFilter.setOnClickListener(this::onClickGoFilter);
        binding.imgWriteWork.setOnClickListener(this::onClickgoWriteWork);
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

        if(mSettings.Value.WorkerCodeBKM){
            binding.imgBookMark.setSelected(true);
        }else{
            binding.imgBookMark.setSelected(false);
        }
        binding.imgBookMark.setOnClickListener(this::onClickBookMark);
    }

    @Override
    protected void initialize() {
        mListTotal = new ArrayList<>();
        mListSearch = new ArrayList<>();

        filterVO = new FilterVO("","","");

        mAdapter = new WorkerCodeMainAdapter(mContext, mListTotal);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

        requestWOC_Read();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void requestWOC_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.woc(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).WOC_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST",
                mUser.Value.MEM_CID,
                filterVO.WOC_02,
                filterVO.WOC_09,
                filterVO.WOC_10,
                ""
        ).enqueue(new Callback<WorkerCode_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<WorkerCode_Model> call, Response<WorkerCode_Model> response) {
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

                            Response<WorkerCode_Model> response = (Response<WorkerCode_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if (response.body().Data.get(0).Validation) {
                                        bindingData(response.body().Data);
                                    } else {
                                        BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ErrorCode);
                                    }
                                }else{
                                    bindingData(new  ArrayList<WorkerCodeVO>());
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
            public void onFailure(Call<WorkerCode_Model> call, Throwable t) {
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
    private void bindingData(ArrayList<WorkerCodeVO> data) {
        mListTotal = data;

        mAdapter.update(mListTotal);

        binding.imgSearch.performClick();

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
                "Z02001002004",
                "",
                "Z02001",
                "002",
                "004",
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

    private void onClickGoTop(View view) {
        binding.recyclerView.smoothScrollToPosition(0);
    }

    private void onClickBookMark(View v) {
        if(mSettings.Value.WorkerCodeBKM){
            requestSaveBMK("M_DELETE");
            mSettings.Value.WorkerCodeBKM=false;
            mSettings.Value.menuList.remove("11");
        }else{
            requestSaveBMK("M_INSERT");
            mSettings.Value.WorkerCodeBKM=true;
            mSettings.Value.menuList.add("11");
        }

        binding.imgBookMark.setSelected(mSettings.Value.WorkerCodeBKM);
    }



    private void onClickSearch(View view) {
        String strSearch = binding.etSearch.getText().toString().toUpperCase().trim();

        mListSearch.clear();

        for (int i = 0; i < mListTotal.size(); i++) {
            WorkerCodeVO vo = mListTotal.get(i);

            if (vo.WOC_02.toUpperCase().contains(strSearch)
                    || vo.WOC_09.toUpperCase().contains(strSearch)
                    || vo.WOC_10.toUpperCase().contains(strSearch)) {
                mListSearch.add(vo);
            }
        }

        mAdapter.update(mListSearch);
    }
    private void onItemClickGoInfo(View view, int position) {
        Intent intent = new Intent(mContext, WorkerCodeDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("WOCData",mListSearch.get(position));
        mActivity.startActivityForResult(intent, WorkerCodeDetailActivity.REQUEST_CODE);

    }
    private void onClickgoWriteWork(View v) {

        Intent intent = new Intent(mContext, AddWorkerCodeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, WorkerCodeDetailActivity.REQUEST_CODE);

    }
    private void onClickGoFilter(View v) {
        Intent intent = new Intent(mContext, WorkerCodeFilterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("filterData", filterVO);
        mActivity.startActivityForResult(intent, WorkerCodeFilterActivity.REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == WorkerCodeFilterActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            filterVO = (kr.hmit.dmjs.ui.worker_code.model.FilterVO) data.getSerializableExtra(WorkerCodeFilterActivity.CATEGORY_INFO);
            requestWOC_Read();
        }else if ((requestCode == AddWorkerCodeActivity.REQUEST_CODE||requestCode == WorkerCodeDetailActivity.REQUEST_CODE) && resultCode == RESULT_OK) {
            requestWOC_Read();
        }

        // 담당자 추가
    }


}