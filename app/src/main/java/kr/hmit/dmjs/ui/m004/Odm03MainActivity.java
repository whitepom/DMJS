package kr.hmit.dmjs.ui.m004;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityOdm03MainBinding;
import kr.hmit.dmjs.databinding.ActivityReceivecurrentMainBinding;
import kr.hmit.dmjs.model.vo.GEM_VO;
import kr.hmit.dmjs.network.Http_Gem;
import kr.hmit.dmjs.ui.m004.adapter.Odm03MainListAdapter;
import kr.hmit.dmjs.ui.m004.filter.OddFilterVO;
import kr.hmit.dmjs.ui.receive.ReceiveAddActivity;
import kr.hmit.dmjs.ui.receive.ReceiveDetailActivity;
import kr.hmit.dmjs.ui.receive.ReceiveFilterActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Odm03MainActivity extends BaseActivity {

    private ActivityOdm03MainBinding binding;
    private Odm03MainListAdapter mAdapter;
    private ArrayList<GEM_VO> mList;
    private OddFilterVO filterVO = new OddFilterVO();


    private SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Calendar mCalRequest;

    private HashMap<String, Object> paramMap = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOdm03MainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initialize() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.imgWriteOrder.setOnClickListener(this::onClickGoWriteReceive);
        binding.imgFilter.setOnClickListener(this::onClickGoFilter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        binding.etSearch.setOnEditorActionListener(this::onSearch);
        binding.imgSearch.setOnClickListener(this::onClickSearch);
        binding.imgGoTop.setOnClickListener(this::onClickGoTop);

        mAdapter = new Odm03MainListAdapter();
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

        //requestGEM_Read();
    }


    @Override
    protected void initLayout() {
        mCalRequest = Calendar.getInstance();
        mCalRequest.add(Calendar.DATE,-7);
        filterVO.setGEM_02_ST(sdfFormat.format(mCalRequest.getTime()));

        mCalRequest = Calendar.getInstance();
        mCalRequest.add(Calendar.DATE,0);
        filterVO.setGEM_02_ED(sdfFormat.format(mCalRequest.getTime()));

    }

    private void onClickGoTop(View v) {
        binding.recyclerView.smoothScrollToPosition(0);
    }

    private boolean onSearch(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            onClickSearch(v);
        } else {
            return false;
        }

        return true;
    }

    private void onClickSearch(View view) {
        GEM_Read();
    }

    private void GEM_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        String strSearch = binding.etSearch.getText().toString().toUpperCase().trim();

        //기본파라미터
        paramMap = setParamMap("GEM_ID", "M_LIST");
        paramMap.put("GEM_04",strSearch);
        paramMap.put("GEM_02_ST",filterVO.getGEM_02_ST());
        paramMap.put("GEM_02_ED",filterVO.getGEM_02_ED());
        paramMap.put("GEM_03",filterVO.getGEM_03());

        openLoadingBar();

        Http_Gem.gem(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).GEM_READ_API(
                BaseConst.URL_HOST,
                paramMap

        ).enqueue(new Callback<ArrayList<GEM_VO>>() {
            @Override
            public void onResponse(Call<ArrayList<GEM_VO>> call, Response<ArrayList<GEM_VO>> response) {
                closeLoadingBar();
                ArrayList<GEM_VO> data = response.body();

                if (response.isSuccessful()) {
                    if(data.size() > 0){
                        bindingData(data);
                    }else{
                        bindingData(new  ArrayList<GEM_VO>());
                        BaseAlert.show(mContext,"검색결과가 없습니다." );
                    }
                } else {
                    BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GEM_VO>> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
                t.printStackTrace();
            }
        });
    }


    private void bindingData(ArrayList<GEM_VO> data) {
        mList = data;
        mAdapter.setList(mList);
    }


    private void onItemClickGoInfo(View view, int position) {
        Intent intent = new Intent(mContext, Odm03MainDetailActivity.class);
        intent.putExtra("GEM_VO", mList.get(position));
        mActivity.startActivityForResult(intent, Odm03MainDetailActivity.REQUEST_CODE);
    }

    private void onClickGoWriteReceive(View v) {
        Intent intent = new Intent(mContext, Odm03MainAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, Odm03MainAddActivity.REQUEST_CODE);

    }
    private void onClickGoFilter(View v) {
        Intent intent = new Intent(mContext, Odm03MainFilterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("OddFilterVO", filterVO);
        mActivity.startActivityForResult(intent, Odm03MainFilterActivity.REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ReceiveFilterActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            filterVO = (OddFilterVO) data.getSerializableExtra(Odm03MainFilterActivity.CATEGORY_INFO);
            GEM_Read();
        }
        else if (requestCode == Odm03MainAddActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            GEM_Read();
        }
        else if ((requestCode == Odm03MainDetailActivity.REQUEST_CODE) && resultCode == RESULT_OK) {
            GEM_Read();
        }
    }
}
