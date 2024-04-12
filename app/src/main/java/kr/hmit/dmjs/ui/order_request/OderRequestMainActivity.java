package kr.hmit.dmjs.ui.order_request;

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

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityOrderRequestMainBinding;
import kr.hmit.dmjs.model.response.REQ_Model;
import kr.hmit.dmjs.model.vo.REQ_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.order_request.model.FilterVO;
import kr.hmit.dmjs.ui.order_request.adapter.OrderRequestListAdapter;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OderRequestMainActivity extends BaseActivity {

    private ActivityOrderRequestMainBinding binding;

    private OrderRequestListAdapter mAdapter;
    private ArrayList<REQ_VO> mListTotal;
    private ArrayList<REQ_VO> mListSearch;

    private FilterVO filterVO;

    private Calendar mCalRequest1;
    private Calendar mCalRequest2;
    private SimpleDateFormat sdfFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderRequestMainBinding.inflate(getLayoutInflater());
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
        if(mSettings.Value.OrderRequestBKM){
//            binding.imgBookMark.setSelected(true);
        }else{
         //   binding.imgBookMark.setSelected(false);
        }
      //  binding.imgBookMark.setOnClickListener(this::onClickBookMark);

    }

    @Override
    protected void initialize() {
        sdfFormat = new SimpleDateFormat("yyyyMMdd");
        mCalRequest1 = Calendar.getInstance();
        mCalRequest1.add(Calendar.MONTH,1);
        mCalRequest2 = Calendar.getInstance();
        mCalRequest2.add(Calendar.MONTH,-1);
        filterVO =new FilterVO(sdfFormat.format(mCalRequest2.getTime()),sdfFormat.format(mCalRequest1.getTime()),"","","");

        mListTotal = new ArrayList<>();
        mListSearch = new ArrayList<>();

        mAdapter = new OrderRequestListAdapter(mContext,mListTotal);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

        requestREQ_Read();
    }
    private void requestREQ_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {

            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.req(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).REQ_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST",
                mUser.Value.MEM_CID,

                filterVO.REQ_02_ST,
                filterVO.REQ_02_ED,
                "",
                "",
                filterVO.REQ_09
        ).enqueue(new Callback<REQ_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<REQ_Model> call, Response<REQ_Model> response) {
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

                            Response<REQ_Model> response = (Response<REQ_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if (response.body().Data.get(0).Validation) {
                                        bindingData(response.body().Data);
                                    } else {
                                        BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                }else{
                                    bindingData(new  ArrayList<REQ_VO>());
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
            public void onFailure(Call<REQ_Model> call, Throwable t) {
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
    private void bindingData(ArrayList<REQ_VO> data) {
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
                "Z02001008001",
                "",
                "Z02001",
                "008",
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
        if(mSettings.Value.OrderRequestBKM){
            requestSaveBMK("M_DELETE");
            mSettings.Value.OrderRequestBKM=false;
            mSettings.Value.menuList.remove("60");

        }else{
            requestSaveBMK("M_INSERT");
            mSettings.Value.OrderRequestBKM=true;
            mSettings.Value.menuList.add("60");
        }

    //    binding.imgBookMark.setSelected(mSettings.Value.OrderRequestBKM);
    }

    private void onItemClickGoInfo(View view, int position) {
        Intent intent = new Intent(mContext, OrderRequestDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("REQData",mListSearch.get(position));
        mActivity.startActivityForResult(intent, OrderRequestDetailActivity.REQUEST_CODE);

    }
    private void onClickGoWriteWork(View v) {
        String[] tempList = {"일반주문","다량주문"};

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("주문유형")
                .setItems(tempList, (dialog, which) -> {
                    if(which==0){
                        Intent intent = new Intent(mContext, OrderRequestAddActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        mActivity.startActivityForResult(intent, OrderRequestAddActivity.REQUEST_CODE);
                    }else{
                        Intent intent = new Intent(mContext, OrderRequestMultiAddActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        mActivity.startActivityForResult(intent, OrderRequestMultiAddActivity.REQUEST_CODE);
                    }
                }).setCancelable(false).create();

        builder.show();
    }
    private void onClickGoFilter(View v){
        Intent intent = new Intent(mContext, OrderRequestFilterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("filterData", filterVO);
        mActivity.startActivityForResult(intent, OrderRequestFilterActivity.REQUEST_CODE);
    }
    private void onClickSearch(View v) {
        String strSearch = binding.etSearch.getText().toString().toUpperCase().trim();

        mListSearch.clear();

        for (int i = 0; i < mListTotal.size(); i++) {
            REQ_VO vo = mListTotal.get(i);

            if(vo.REQ_01.toUpperCase().contains(strSearch)
            ||vo.REQ_03_NM.toUpperCase().contains(strSearch)
            ||(vo.DAH_02==null?"":vo.DAH_02).toUpperCase().contains(strSearch)){
                mListSearch.add(vo);
            }
        }

        mAdapter.update(mListSearch);

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OrderRequestFilterActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            filterVO = (FilterVO) data.getSerializableExtra(OrderRequestFilterActivity.CATEGORY_INFO);
            requestREQ_Read();
        }else if ((requestCode == OrderRequestAddActivity.REQUEST_CODE||requestCode == OrderRequestDetailActivity.REQUEST_CODE) && resultCode == RESULT_OK) {
            requestREQ_Read();
        }

        // 담당자 추가
    }
}
