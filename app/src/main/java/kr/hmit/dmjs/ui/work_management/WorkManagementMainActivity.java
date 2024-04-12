package kr.hmit.dmjs.ui.work_management;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import kr.hmit.base.settings.SettingsKey;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityWorkManagementMainBinding;
import kr.hmit.dmjs.model.response.WKS_Model;
import kr.hmit.dmjs.model.vo.WKS_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.login.LoginActivity;
import kr.hmit.dmjs.ui.work_management.adapter.WorkManagementListAdapter;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import kr.hmit.dmjs.ui.work_management.model.FilterVO;


public class WorkManagementMainActivity extends BaseActivity {
    //=============================
    // region // view
    //=============================

    private ActivityWorkManagementMainBinding binding;

    //=============================
    // endregion
    //=============================

    //=============================
    // region // variable
    //=============================

    private WorkManagementListAdapter mAdapter;
    private ArrayList<WKS_VO> mListTotal;
    private ArrayList<WKS_VO> mListSearch;
    private ArrayList<WKS_VO> mListDetail;
    private WKS_VO WKSData;
    private FilterVO filterVO;
    public static Context context;
    //============================
    // endregion // variable
    //=============================

    //=============================
    // region // initialize
    //=============================




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWorkManagementMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context=this;
        initLayout();
        initialize();




    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        binding.imgGoTop.setOnClickListener(this::onClickGoTop);
        binding.imgSearch.setOnClickListener(this::onClickSearch);
        binding.imgWriteWork.setOnClickListener(this::onClickGoWriteWork);
        binding.imgFilter.setOnClickListener(this::onclickGoFilter);

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
        filterVO =new FilterVO("","","");

        mAdapter = new WorkManagementListAdapter(mContext, mListTotal);
        mAdapter.setOnItemClickListener(this::onItemClickGoDetail);
        binding.recyclerView.setAdapter(mAdapter);

        requestWKS_Read();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //=============================
    // endregion // initialize
    //=============================

    //=============================
    // region // api
    //=============================

    /**
     * 업무관리 목록 조회
     */
    public void requestWKS_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.wks(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).WKS_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST",
                mUser.Value.MEM_CID,
                "",
                filterVO.WKS_05,
                filterVO.WKS_1001, //요청자
                filterVO.WKS_98    // 담당자
        ).enqueue(new Callback<WKS_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<WKS_Model> call, Response<WKS_Model> response) {
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

                            Response<WKS_Model> response = (Response<WKS_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0) {

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
                                    bindingData(new  ArrayList<WKS_VO>());
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
            public void onFailure(Call<WKS_Model> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
            }
        });
    }

    /**업무관리 detail**/

    public void requestWKS_Detail(int WKS_01, String wksmList, String MEM_11) {
          String WKS_98="admin";
     /*   if(mUser.Value.MEM_01.equals("admin")){
            if(wksmList.contains("관리자")){
                WKS_98="admin";
            }else WKS_98="";
        }*/
        if(MEM_11.contains(mUser.Value.MEM_01)){  // 관리자일 때
            if(wksmList.contains(mUser.Value.MEM_02)){
                WKS_98=mUser.Value.MEM_01;
            }else WKS_98="";
        }
        else{
            WKS_98=mUser.Value.MEM_01;
        }


        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.wks(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).WKS_DETAIL(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                mUser.Value.MEM_CID,
                String.valueOf(WKS_01),
                WKS_98
        ).enqueue(new Callback<WKS_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<WKS_Model> call, Response<WKS_Model> response) {
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

                            Response<WKS_Model> response = (Response<WKS_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                    if (response.body().Data.get(0).Validation) {
                                        mListDetail = response.body().Data;
                                        Intent intent = new Intent(mContext, WorkManagementDetailActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        WKSData = mListDetail.get(0);
                                   //   String MEM_01=mUser.Value.MEM_01;
                                        intent.putExtra("WKSData",WKSData);
                                   //   intent.putExtra("MEM_01",MEM_01);
                                         mActivity.startActivityForResult(intent, WorkManagementDetailActivity.REQUEST_CODE);
                                        Toast.makeText(getApplicationContext(),"detail",Toast.LENGTH_LONG);
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
                            } else {
                                BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<WKS_Model> call, Throwable t) {
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
    private void bindingData(ArrayList<WKS_VO> data) {
        mListTotal = data;

        mAdapter.update(mListTotal);

        binding.imgSearch.performClick();

    }


    //=============================
    // endregion // api
    //=============================

    //==============================
    // region // event
    //==============================

    /**
     * 상단으로 올린다.
     *
     * @param v
     */
    private void onClickGoTop(View v) {
        binding.recyclerView.smoothScrollToPosition(0);
    }


    /**
     * 상세화면으로 이동
     *
     * @param view
     * @param position
     */
    private void onItemClickGoDetail(View view, int position) {
        requestWKS_Detail(Integer.parseInt(mListSearch.get(position).WKS_01),mListSearch.get(position).WKSM_LIST,mListSearch.get(position).MEM_11);
     //   Intent intent = new Intent(mContext, WorkManagementDetailActivity.class);
      //  intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
      //  WKSData = mListDetail.get(0);
      //  intent.putExtra("WKSData",mListDetail);
      //  mActivity.startActivityForResult(intent, WorkManagementDetailActivity.REQUEST_CODE);
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
            WKS_VO vo = mListTotal.get(i);

            if(vo.WKS_04.toUpperCase().contains(strSearch)){
                mListSearch.add(vo);
            }
        }
        mAdapter.update(mListSearch);
    }

    private void onClickGoWriteWork(View v) {
        Intent intent = new Intent(mContext, AddWorkActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, AddWorkActivity.REQUEST_CODE);
    }
    private void onclickGoFilter(View v){
        Intent intent = new Intent(mContext, WorkManagementFilterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("filterData", filterVO);
        mActivity.startActivityForResult(intent, WorkManagementFilterActivity.REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == WorkManagementFilterActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            filterVO = (FilterVO) data.getSerializableExtra(WorkManagementFilterActivity.CATEGORY_INFO);
            requestWKS_Read();
        }else if ((requestCode == AddWorkActivity.REQUEST_CODE||requestCode == WorkManagementDetailActivity.REQUEST_CODE) && resultCode == RESULT_OK) {
            requestWKS_Read();
        }
    }

    //==============================
    // endregion // event
    //==============================
}
