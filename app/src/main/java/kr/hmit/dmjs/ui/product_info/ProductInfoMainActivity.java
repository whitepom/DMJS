package kr.hmit.dmjs.ui.product_info;

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

import kr.hmit.base.settings.SettingsKey;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityProductInfoMainBinding;
import kr.hmit.dmjs.model.response.ProductionInfo_Model;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.login.LoginActivity;
import kr.hmit.dmjs.ui.product_info.adapter.ProductionInfoListAdapter;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductInfoMainActivity extends BaseActivity {
    //=============================
    // region // view
    //=============================

    private ActivityProductInfoMainBinding binding;

    //=============================
    // endregion // view
    //=============================

    //=============================
    // region // variable
    //=============================

    private ProductionInfoListAdapter mAdapter;
    private ArrayList<ProductionInfoVO> mListTotal;
    private ArrayList<ProductionInfoVO> mListSearch;

    //=============================
    // endregion // variable
    //=============================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductInfoMainBinding.inflate(getLayoutInflater());
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

        if(mSettings.Value.ProductInfoBKM){
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

        mAdapter = new ProductionInfoListAdapter(mContext, mListTotal);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

        requestDAH_Read();
    }

    private void requestDAH_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.dah(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).DAH_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST_DAH",
                mUser.Value.MEM_CID,
                "",
                "",
                "",
                ""

        ).enqueue(new Callback<ProductionInfo_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<ProductionInfo_Model> call, Response<ProductionInfo_Model> response) {
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
                            Response<ProductionInfo_Model> response = (Response<ProductionInfo_Model>) msg.obj;
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
                                      // BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                }else{
                                    bindingData(new  ArrayList<ProductionInfoVO>());
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
            public void onFailure(Call<ProductionInfo_Model> call, Throwable t) {
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
    private void bindingData(ArrayList<ProductionInfoVO> data) {
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
                "Z02001003001",
                "",
                "Z02001",
                "003",
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

    private void onClickBookMark(View v) {
        if(mSettings.Value.ProductInfoBKM){
            requestSaveBMK("M_DELETE");
            mSettings.Value.ProductInfoBKM=false;
            mSettings.Value.menuList.remove("20");
        }else{
            requestSaveBMK("M_INSERT");
            mSettings.Value.ProductInfoBKM=true;
            mSettings.Value.menuList.add("20");
        }

        binding.imgBookMark.setSelected(mSettings.Value.ProductInfoBKM);
    }

    private boolean onSearch(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            onClickSearch(v);
        } else {
            return false;
        }

        return true;
    }

    /**
     * 제품정보 추가
     *
     * @param view
     */
    private void onClickAdd(View view) {
        Intent intent = new Intent(mContext, AddProductInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, AddProductInfoActivity.REQUEST_CODE);

    }
    private void onItemClickGoInfo(View view, int position) {
        Intent intent = new Intent(mContext, ProductInfoDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("DAHData",mListSearch.get(position));
        mActivity.startActivityForResult(intent, ProductInfoDetailActivity.REQUEST_CODE);

    }


    /**
     * 상단으로 올린다.
     *
     * @param v
     */
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
            ProductionInfoVO vo = mListTotal.get(i);

            if (vo.DAH_01.toUpperCase().contains(strSearch)
                    || vo.DAH_02.toUpperCase().contains(strSearch)
                    || vo.DAH_14.toUpperCase().contains(strSearch)) {
                mListSearch.add(vo);
            }
        }

        mAdapter.update(mListSearch);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 담당자 추가
        if ((requestCode == AddProductInfoActivity.REQUEST_CODE||requestCode ==ProductInfoDetailActivity.REQUEST_CODE) && resultCode == RESULT_OK) {
            requestDAH_Read();
        }


    }

}