package kr.hmit.dmjs.ui.order_management;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityOrderManagementMainBinding;
import kr.hmit.dmjs.model.response.ODM_Model;
import kr.hmit.dmjs.model.vo.ODM_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.order_management.adapter.OrderManagementListAdapter;
import kr.hmit.dmjs.ui.order_management.model.FilterVO;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderManagementMainActivity extends BaseActivity {


    private ActivityOrderManagementMainBinding binding;

    private OrderManagementListAdapter mAdapter;
    private ArrayList<ODM_VO> mListTotal;
    private ArrayList<ODM_VO> mListSearch;
    private FilterVO filterVO;

    private Calendar mCalRequest1;
    private Calendar mCalRequest2;
    private SimpleDateFormat sdfFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderManagementMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.imgWriteOrder.setOnClickListener(this::onClickGoWriteOrder);
        binding.imgFilter.setOnClickListener(this::onClickGoFilter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        binding.etSearch.setOnEditorActionListener(this::onSearch);
        binding.imgSearch.setOnClickListener(this::onClickSearch);
    }

    @Override
    protected void initialize() {
        sdfFormat = new SimpleDateFormat("yyyyMMdd");
        mCalRequest1 = Calendar.getInstance();
        mCalRequest1.add(Calendar.MONTH,1);
        mCalRequest2 = Calendar.getInstance();
        mCalRequest2.add(Calendar.MONTH,-1);
        filterVO =new FilterVO("","",sdfFormat.format(mCalRequest2.getTime()),sdfFormat.format(mCalRequest1.getTime()));

        mListTotal = new ArrayList<>();
        mListSearch = new ArrayList<>();

        mAdapter = new OrderManagementListAdapter(mContext,mListTotal);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);

        binding.recyclerView.setAdapter(mAdapter);
        requestODM_Read();
        
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 목록 조회
     */
    private void requestODM_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.odm(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).ODM_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST",
                mUser.Value.MEM_CID,
                filterVO.ODM_02_ST,
                filterVO.ODM_02_ED,
                filterVO.ODM_01,
                filterVO.CLT_02,
                filterVO.ODD_03
        ).enqueue(new Callback<ODM_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<ODM_Model> call, Response<ODM_Model> response) {
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

                            Response<ODM_Model> response = (Response<ODM_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if (response.body().Data.get(0).Validation) {
                                        bindingData(response.body().Data);
                                    } else {
                                        BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                }else{
                                    bindingData(new  ArrayList<ODM_VO>());
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
            public void onFailure(Call<ODM_Model> call, Throwable t) {
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
    private void bindingData(ArrayList<ODM_VO> data) {
        mListSearch = data;
        mAdapter.update(mListSearch);
    }

    private boolean onSearch(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            onClickSearch(v);
        } else {
            return false;
        }

        return true;
    }
   /* private void onClickBookMark(View v) {
        if(mSettings.Value.OrderManagementBKM){

            mSettings.Value.OrderManagementBKM=false;
            mSettings.Value.menuList.remove("30");
        }else{

            mSettings.Value.OrderManagementBKM=true;
            mSettings.Value.menuList.add("30");
        }

      //  binding.imgBookMark.setSelected(mSettings.Value.OrderManagementBKM);
    }*/

    private void onClickSearch(View view) {
        requestODM_Read();
    }


    private void onItemClickGoInfo(View view, int position) {
        Intent intent = new Intent(mContext, OrderManagementInfoMain.class);
        intent.putExtra("odmVO", mListSearch.get(position));
        mActivity.startActivityForResult(intent, OrderManagementInfoMain.REQUEST_CODE);
    }
    private void onClickGoWriteOrder(View v) {
        Intent intent = new Intent(mContext, OrderManagementAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, OrderManagementAddActivity.REQUEST_CODE);

    }
    private void onClickGoFilter(View v) {
        Intent intent = new Intent(mContext, OrderManagementFilterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("filterData", filterVO);
        mActivity.startActivityForResult(intent, OrderManagementFilterActivity.REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OrderManagementFilterActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            filterVO = (FilterVO) data.getSerializableExtra(OrderManagementFilterActivity.CATEGORY_INFO);
            requestODM_Read();
        }else if ((requestCode == OrderManagementAddActivity.REQUEST_CODE||requestCode == OrderManagementInfoMain.REQUEST_CODE) && resultCode == RESULT_OK) {
            requestODM_Read();
        }

    }
}
