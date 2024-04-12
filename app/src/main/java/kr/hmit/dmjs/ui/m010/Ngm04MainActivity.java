package kr.hmit.dmjs.ui.m010;

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
import kr.hmit.dmjs.databinding.ActivityNgm04MainBinding;
import kr.hmit.dmjs.model.vo.NGM_VO;
import kr.hmit.dmjs.network.Http_Ngm;
import kr.hmit.dmjs.ui.m010.adapter.Ngm04MainListAdapter;
import kr.hmit.dmjs.ui.m010.filter.NgmFilterVO;
import kr.hmit.dmjs.ui.main.MainDashboardActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Ngm04MainActivity extends BaseActivity {

    private ActivityNgm04MainBinding binding;
    private Ngm04MainListAdapter mAdapter;
    private ArrayList<NGM_VO> mList;
    private NgmFilterVO filterVO = new NgmFilterVO();

    private SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Calendar mCalRequest;

    private HashMap<String, Object> paramMap = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNgm04MainBinding.inflate(getLayoutInflater());
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
        binding.ngm04.setOnEditorActionListener(this::onSearch);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

        mAdapter = new Ngm04MainListAdapter();
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void initLayout() {
        mCalRequest = Calendar.getInstance();
        mCalRequest.add(Calendar.DATE,-7);
        filterVO.setNGM_02_ST(sdfFormat.format(mCalRequest.getTime()));

        mCalRequest = Calendar.getInstance();
        mCalRequest.add(Calendar.DATE,0);
        filterVO.setNGM_02_ED(sdfFormat.format(mCalRequest.getTime()));
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
        NGM_Read();
    }

    private void NGM_Read() {

        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }
        String strSearch = binding.ngm04.getText().toString().toUpperCase().trim();

        //기본파라미터
        paramMap = setParamMap("NGM_ID", "M_LIST");
        paramMap.put("NGM_04",strSearch);
        paramMap.put("NGM_02_ST",filterVO.getNGM_02_ST());
        paramMap.put("NGM_02_ED",filterVO.getNGM_02_ED());

        openLoadingBar();

        Http_Ngm.ngm(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).NGM_Read(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<ArrayList<NGM_VO>>() {
            @Override
            public void onResponse(Call<ArrayList<NGM_VO>> call, Response<ArrayList<NGM_VO>> response) {
                closeLoadingBar();
                ArrayList<NGM_VO> data = response.body();

                if (response.isSuccessful()) {
                    if(data.size() > 0){
                        bindingData(data);
                    }else{
                        bindingData(new  ArrayList<NGM_VO>());
                        BaseAlert.show(mContext,"검색결과가 없습니다." );
                    }
                } else {
                    BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NGM_VO>> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
                t.printStackTrace();
            }
        });
    }

    private void bindingData(ArrayList<NGM_VO> data) {
        mList = data;
        mAdapter.setNgmList(data);
    }

    private void onClickGoHome(View v){
        Intent intent = new Intent(this, MainDashboardActivity.class);
        startActivity(intent);
    }

    private void onItemClickGoInfo(View view, int position) {
        Intent intent = new Intent(mContext, Ngm04MainDetailActivity.class);
        intent.putExtra("NGM_VO", mList.get(position));
        mActivity.startActivityForResult(intent, Ngm04MainDetailActivity.REQUEST_CODE);
    }
    private void onClickAdd(View v) {
        Intent intent = new Intent(mContext, Ngm04MainAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, Ngm04MainAddActivity.REQUEST_CODE);

    }
    private void onClickGoFilter(View v) {

        Intent intent = new Intent(mContext, Ngm04MainFilterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("NgmFilterVO", filterVO);
        mActivity.startActivityForResult(intent, Ngm04MainFilterActivity.REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Ngm04MainFilterActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            filterVO = (NgmFilterVO) data.getSerializableExtra(Ngm04MainFilterActivity.CATEGORY_INFO);
        }else if (requestCode == Ngm04MainAddActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            NGM_Read();
        }else if (requestCode == Ngm04MainDetailActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            NGM_Read();
        }else if (requestCode == Ngm04MainUpdateActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            NGM_Read();
        }
    }
}
