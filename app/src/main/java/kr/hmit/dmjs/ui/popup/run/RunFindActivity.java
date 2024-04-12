package kr.hmit.dmjs.ui.popup.run;


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
import kr.hmit.dmjs.databinding.ActivityRunFindBinding;
import kr.hmit.dmjs.model.vo.RUN2_VO;
import kr.hmit.dmjs.model.vo.ZAG_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.popup.run.adapter.RunFindListAdapter;
import kr.hmit.dmjs.ui.popup.run.model.RunFilterVO;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RunFindActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8001;
    public static final String RUN_LIST = "RUN_LIST";

    private ActivityRunFindBinding binding;
    private RunFindListAdapter mAdapter;

    private RunFilterVO filterVo = new RunFilterVO();

    private ArrayList<RUN2_VO> mList;

    private RUN2_VO paramRun2Vo = new RUN2_VO();

    private SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Calendar mCalRequest;

    private String GCM_03;

    private HashMap<String, Object> paramMap = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRunFindBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        GCM_03 = intent.getExtras().get("GCM_03").toString();

        initialize();
        initLayout();
    }
    @Override
    protected void initialize() {
        binding.imgBack.setOnClickListener(v -> finish());

        binding.imgFilter.setOnClickListener(this::onClickGoFilter);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        binding.imgSearch.setOnClickListener(this::onClickSearch);
        binding.etSearch.setOnEditorActionListener(this::onSearch);

        mAdapter = new RunFindListAdapter();
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

    }
    @Override
    protected void initLayout() {
        mCalRequest = Calendar.getInstance();
        mCalRequest.add(Calendar.DATE,-7);
        filterVo.setRUN_02_ST(sdfFormat.format(mCalRequest.getTime()));

        mCalRequest = Calendar.getInstance();
        mCalRequest.add(Calendar.DATE,0);
        filterVo.setRUN_02_ED(sdfFormat.format(mCalRequest.getTime()));
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
        RUN2_Read();
    }

    private void RUN2_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        //기본파라미터
        paramMap = setParamMap("RUN_ID", "M_POP_LIST");
        paramMap.put("RUN_02_ST", filterVo.getRUN_02_ST());
        paramMap.put("RUN_02_ED", filterVo.getRUN_02_ED());
        paramMap.put("RUN_2101", binding.etSearch.getText());
        paramMap.put("RUN_03", GCM_03);

        openLoadingBar();

        Http.run2(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).RUN2_READ_API(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<ArrayList<RUN2_VO>>() {
            @Override
            public void onResponse(Call<ArrayList<RUN2_VO>> call, Response<ArrayList<RUN2_VO>> response) {
                closeLoadingBar();
                ArrayList<RUN2_VO> data = response.body();

                if (response.isSuccessful()) {
                    if(data.size() > 0){
                        bindingData(data);
                    }else{
                        bindingData(new  ArrayList<RUN2_VO>());
                        BaseAlert.show(mContext,"검색결과가 없습니다." );
                    }
                } else {
                    BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RUN2_VO>> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
                t.printStackTrace();
            }
        });
    }

    private void bindingData(ArrayList<RUN2_VO> data) {
        mList = data;
        mAdapter.setList(data);
    }

    private void onItemClickGoInfo(View view, int position) {

        Intent intent = new Intent();
        intent.putExtra(RUN_LIST, mList.get(position));
        setResult(RESULT_OK, intent);
        finish();
    }

    private void onClickGoFilter(View v) {

        Intent intent = new Intent(mContext, RunFindFilterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        intent.putExtra("RunFilterVo", filterVo);
        mActivity.startActivityForResult(intent, RunFindFilterActivity.REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RunFindFilterActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            filterVo = (RunFilterVO) data.getSerializableExtra(RunFindFilterActivity.CATEGORY_INFO);
            RUN2_Read();
        }
    }

}

