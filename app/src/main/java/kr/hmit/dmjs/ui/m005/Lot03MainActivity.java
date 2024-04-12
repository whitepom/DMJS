package kr.hmit.dmjs.ui.m005;

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
import kr.hmit.dmjs.databinding.ActivityLot03MainBinding;
import kr.hmit.dmjs.model.vo.GAG_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.m005.adapter.Lot03MainListAdapter;
import kr.hmit.dmjs.ui.m005.filter.GagFilterVO;
import kr.hmit.dmjs.ui.main.MainDashboardActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Lot03MainActivity extends BaseActivity {

    private ActivityLot03MainBinding binding;
    private Lot03MainListAdapter mAdapter;
    private ArrayList<GAG_VO> mList;
    private GagFilterVO filterVO = new GagFilterVO();

    private SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Calendar mCalRequest;

    private HashMap<String, Object> paramMap = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLot03MainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
        initLayout();
    }

    @Override
    protected void initialize() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.imgAdd.setOnClickListener(this::onClickAdd);
        binding.imgFilter.setOnClickListener(this::onClickGoFilter);
        binding.imgSearch.setOnClickListener(this::onClickSearch);
        binding.imgGoHome.setOnClickListener(this::onClickGoHome);
        binding.gag03.setOnEditorActionListener(this::onSearch);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

        mAdapter = new Lot03MainListAdapter();
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void initLayout() {
        mCalRequest = Calendar.getInstance();
        mCalRequest.add(Calendar.DATE,-7);
        filterVO.setGAG_04_ST(sdfFormat.format(mCalRequest.getTime()));

        mCalRequest = Calendar.getInstance();
        mCalRequest.add(Calendar.DATE,0);
        filterVO.setGAG_04_ED(sdfFormat.format(mCalRequest.getTime()));
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
        GAG_Read();
    }
    private void GAG_Read() {

        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }
        String strSearch = binding.gag03.getText().toString().toUpperCase().trim();

        //기본파라미터
        paramMap = setParamMap("GAG_ID", "M_LIST_GAG");
        paramMap.put("GAG_03",strSearch);
        paramMap.put("GAG_04_ST",filterVO.getGAG_04_ST());
        paramMap.put("GAG_04_ED",filterVO.getGAG_04_ED());

        openLoadingBar();

        Http.gag(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).GAG_Read(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<ArrayList<GAG_VO>>() {
            @Override
            public void onResponse(Call<ArrayList<GAG_VO>> call, Response<ArrayList<GAG_VO>> response) {
                closeLoadingBar();
                ArrayList<GAG_VO> data = response.body();

                if (response.isSuccessful()) {
                    if(data.size() > 0){
                        bindingData(data);
                    }else{
                        bindingData(new  ArrayList<GAG_VO>());
                        BaseAlert.show(mContext,"검색결과가 없습니다." );
                    }
                } else {
                    BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GAG_VO>> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
                t.printStackTrace();
            }
        });
    }

    private void bindingData(ArrayList<GAG_VO> data) {
        mList = data;
        mAdapter.setGagList(data);
    }

    private void onClickGoHome(View v){
        Intent intent = new Intent(this, MainDashboardActivity.class);
        startActivity(intent);
    }

    private void onItemClickGoInfo(View view, int position) {
        Intent intent = new Intent(mContext, Lot03MainDetailActivity.class);
        intent.putExtra("GAG_VO", mList.get(position));
        mActivity.startActivityForResult(intent, Lot03MainDetailActivity.REQUEST_CODE);
    }
    private void onClickAdd(View v) {
        Intent intent = new Intent(mContext, Lot03MainAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, Lot03MainAddActivity.REQUEST_CODE);

    }
    private void onClickGoFilter(View v) {

        Intent intent = new Intent(mContext, Lot03MainFilterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("GagFilterVO", filterVO);
        mActivity.startActivityForResult(intent, Lot03MainFilterActivity.REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Lot03MainFilterActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            filterVO = (GagFilterVO) data.getSerializableExtra(Lot03MainFilterActivity.CATEGORY_INFO);
        }else if (requestCode == Lot03MainAddActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            GAG_Read();
        }else if (requestCode == Lot03MainDetailActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            GAG_Read();
        }
    }
}
