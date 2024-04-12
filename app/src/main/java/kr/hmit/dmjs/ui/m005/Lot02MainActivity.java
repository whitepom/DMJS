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
import kr.hmit.dmjs.databinding.ActivityLot02MainBinding;
import kr.hmit.dmjs.model.vo.LOT_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.m005.adapter.Lot02MainListAdapter;
import kr.hmit.dmjs.ui.m005.filter.LotFilterVO;
import kr.hmit.dmjs.ui.main.MainDashboardActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Lot02MainActivity extends BaseActivity {

    private ActivityLot02MainBinding binding;
    private Lot02MainListAdapter mAdapter;
    private ArrayList<LOT_VO> mList;
    private LotFilterVO filterVO = new LotFilterVO();

    private SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Calendar mCalRequest;

    private HashMap<String, Object> paramMap = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLot02MainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
        initLayout();
    }

    @Override
    protected void initialize() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.imgFilter.setOnClickListener(this::onClickGoFilter);
        binding.imgSearch.setOnClickListener(this::onClickSearch);
        binding.imgGoHome.setOnClickListener(this::onClickGoHome);
        binding.zag03.setOnEditorActionListener(this::onSearch);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

        mAdapter = new Lot02MainListAdapter();
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void initLayout() {
        mCalRequest = Calendar.getInstance();
        mCalRequest.add(Calendar.DATE,-7);
        filterVO.setLOT_02_ST(sdfFormat.format(mCalRequest.getTime()));

        mCalRequest = Calendar.getInstance();
        mCalRequest.add(Calendar.DATE,0);
        filterVO.setLOT_02_ED(sdfFormat.format(mCalRequest.getTime()));
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
        LOT_Read();
    }
    private void LOT_Read() {

        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }
        String strSearch = binding.zag03.getText().toString().toUpperCase().trim();

        //기본파라미터
        paramMap = setParamMap("LOT_ID", "M_LIST");
        paramMap.put("LOT_04",strSearch);
        paramMap.put("LOT_02_ST",filterVO.getLOT_02_ST());
        paramMap.put("LOT_02_ED",filterVO.getLOT_02_ED());

        openLoadingBar();

        Http.lot(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).LOT_Read(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<ArrayList<LOT_VO>>() {
            @Override
            public void onResponse(Call<ArrayList<LOT_VO>> call, Response<ArrayList<LOT_VO>> response) {
                closeLoadingBar();
                ArrayList<LOT_VO> data = response.body();

                if (response.isSuccessful()) {
                    if(data.size() > 0){
                        bindingData(data);
                    }else{
                        bindingData(new  ArrayList<LOT_VO>());
                        BaseAlert.show(mContext,"검색결과가 없습니다." );
                    }
                } else {
                    BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<LOT_VO>> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
                t.printStackTrace();
            }
        });
    }

    private void bindingData(ArrayList<LOT_VO> data) {
        mList = data;
        mAdapter.setLotList(data);
    }

    private void onClickGoHome(View v){
        Intent intent = new Intent(this, MainDashboardActivity.class);
        startActivity(intent);
    }

    private void onItemClickGoInfo(View view, int position) {
        Intent intent = new Intent(mContext, Lot02MainDetailActivity.class);
        intent.putExtra("LOT_VO", mList.get(position));
        mActivity.startActivityForResult(intent, Lot02MainDetailActivity.REQUEST_CODE);
    }

    private void onClickGoFilter(View v) {

        Intent intent = new Intent(mContext, Lot02MainFilterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("LotFilterVO", filterVO);
        mActivity.startActivityForResult(intent, Lot02MainFilterActivity.REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Lot02MainFilterActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            filterVO = (LotFilterVO) data.getSerializableExtra(Lot02MainFilterActivity.CATEGORY_INFO);
        }else if (requestCode == Lot02MainDetailActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            LOT_Read();
        }
    }
}
