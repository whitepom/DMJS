package kr.hmit.dmjs.ui.receive;

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

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.base.settings.SettingsKey;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityReceivecurrentMainBinding;
import kr.hmit.dmjs.model.response.GEM_Model;
import kr.hmit.dmjs.model.vo.GEM_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.login.LoginActivity;
import kr.hmit.dmjs.ui.receive.adapter.ReceiveCurrentListAdapter;
import kr.hmit.dmjs.ui.receive.model.FilterVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiveCurrentActivity extends BaseActivity {
    private ActivityReceivecurrentMainBinding binding;

    private ReceiveCurrentListAdapter mAdapter;
    private FilterVO filterVO;

    private ArrayList<GEM_VO> mListTotal;
    private ArrayList<GEM_VO> mListSearch;

    private Calendar mCalRequest1;
    private Calendar mCalRequest2;
    private SimpleDateFormat sdfFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReceivecurrentMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }



    @Override
    protected void initLayout() {

        binding.imgBack.setOnClickListener(v -> finish());
        binding.imgWriteOrder.setOnClickListener(this::onClickGoWriteReceive);
        binding.imgFilter.setOnClickListener(this::onClickGoFilter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        binding.etSearch.setOnEditorActionListener(this::onSearch);
        binding.imgSearch.setOnClickListener(this::onClickSearch);
      /*  if(mSettings.Value.OrderManagementBKM){
            binding.imgBookMark.setSelected(true);
        }else{
            binding.imgBookMark.setSelected(false);
        }
        binding.imgBookMark.setOnClickListener(this::onClickBookMark);*/
        binding.imgGoTop.setOnClickListener(this::onClickGoTop);

    }
    private void onClickGoTop(View v) {
        binding.recyclerView.smoothScrollToPosition(0);
    }
    @Override
    protected void initialize() {

        sdfFormat = new SimpleDateFormat("yyyyMMdd");
        mCalRequest1 = Calendar.getInstance();
        mCalRequest1.add(Calendar.MONTH,1);
        mCalRequest2 = Calendar.getInstance();
        mCalRequest2.add(Calendar.MONTH,-1);
        filterVO =new FilterVO("", //품번
                               "", // 입고처명
                               "", // 품명
                                "", //입고처번호
                                sdfFormat.format(mCalRequest2.getTime()),
                                sdfFormat.format(mCalRequest1.getTime())
                               );

        mListTotal = new ArrayList<>();
        mListSearch = new ArrayList<>();

        mAdapter = new ReceiveCurrentListAdapter(mContext,mListTotal);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);

        binding.recyclerView.setAdapter(mAdapter);
        requestGEM_Read();

    }
    @Override
    protected void onResume() {
        super.onResume();
        requestGEM_Read();
    }

    private void requestGEM_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.gem(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).GEM_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST",
                "DMJS",
                filterVO.GEM_02_ST,
                filterVO.GEM_02_ED,
                filterVO.ODD_03, //품명
                filterVO.CLT_01  // 입고처

        ).enqueue(new Callback<GEM_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<GEM_Model> call, Response<GEM_Model> response) {
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

                            Response<GEM_Model> response = (Response<GEM_Model>) msg.obj;

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
                                    bindingData(new  ArrayList<GEM_VO>());
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
            public void onFailure(Call<GEM_Model> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
            }
        });
    }


    private void bindingData(ArrayList<GEM_VO> data) {        mListTotal = data;

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
  /*  private void onClickBookMark(View v) {
        if(mSettings.Value.OrderManagementBKM){
            requestSaveBMK("M_DELETE");
            mSettings.Value.OrderManagementBKM=false;
            mSettings.Value.menuList.remove("30");
        }else{
            requestSaveBMK("M_INSERT");
            mSettings.Value.OrderManagementBKM=true;
            mSettings.Value.menuList.add("30");
        }

        binding.imgBookMark.setSelected(mSettings.Value.OrderManagementBKM);
    }
*/

    private void onClickSearch(View view) {
        String strSearch = binding.etSearch.getText().toString().toUpperCase().trim();


        mListSearch.clear();

        for (int i = 0; i < mListTotal.size(); i++) {
            GEM_VO vo = mListTotal.get(i);

            if ((vo.CLT_02.toUpperCase().contains(strSearch))||
                    (vo.GEM_04_NM.toUpperCase().contains(strSearch))
            ) {
                mListSearch.add(vo);
            }
        }

        mAdapter.update(mListSearch);
    }


    private void onItemClickGoInfo(View view, int position) {
        Intent intent = new Intent(mContext, ReceiveDetailActivity.class);
        intent.putExtra("gemVO", mListSearch.get(position));
        mActivity.startActivityForResult(intent, ReceiveDetailActivity.REQUEST_CODE);
    }

    private void onClickGoWriteReceive(View v) {
        Intent intent = new Intent(mContext, ReceiveAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, ReceiveAddActivity.REQUEST_CODE);

    }
    private void onClickGoFilter(View v) {
        Intent intent = new Intent(mContext, ReceiveFilterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("filterData", filterVO);
        mActivity.startActivityForResult(intent, ReceiveFilterActivity.REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ReceiveFilterActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            filterVO = (FilterVO) data.getSerializableExtra(ReceiveFilterActivity.CATEGORY_INFO);
            requestGEM_Read();
        }else if ((requestCode == ReceiveAddActivity.REQUEST_CODE||requestCode == ReceiveDetailActivity.REQUEST_CODE) && resultCode == RESULT_OK) {
            requestGEM_Read();
        }
    }
}
