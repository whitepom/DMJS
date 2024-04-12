package kr.hmit.dmjs.ui.m005;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityLot02MainDetailBinding;
import kr.hmit.dmjs.model.vo.GAG_VO;
import kr.hmit.dmjs.model.vo.LOT_VO;
import kr.hmit.dmjs.model.vo.ZAG_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.m005.adapter.Lot02SubListAdapter;
import kr.hmit.dmjs.ui.main.MainDashboardActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Lot02MainDetailActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8023;
    public static final String ORDER_REQUEST_ADD = "LOT_02_DETAIL";

    private ActivityLot02MainDetailBinding binding;
    private Lot02SubListAdapter mAdapter;
    private ArrayList<GAG_VO> mList;

    private LOT_VO lot_vo = new LOT_VO();

    //파라미터
    private HashMap<String, Object> paramMap = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLot02MainDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
        initLayout();
    }

    @Override
    protected void initialize() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.btnAdd.setOnClickListener(this::onClickAdd);
        binding.imgGoHome.setOnClickListener(this::onClickGoHome);
        binding.imgGoTop.setOnClickListener(this::onClickGoTop);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

        mAdapter = new Lot02SubListAdapter();
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

        Intent intent = getIntent();
        lot_vo = (LOT_VO) intent.getExtras().get("LOT_VO");

        binding.lot02.setText(lot_vo.LOT_02);
        binding.lot01.setText(lot_vo.LOT_01);
        binding.lot04.setText(lot_vo.LOT_04 +"/" + lot_vo.LOT_04_NM);
        binding.lot06.setText(String.valueOf((int)lot_vo.LOT_07) +"/" +String.valueOf((int)lot_vo.LOT_06));
        binding.lot1601.setText(lot_vo.LOT_1601 +"~"+lot_vo.LOT_1602 +" (" + String.valueOf(lot_vo.LOT_10) + ")");
        binding.lot11Nm.setText(String.valueOf(lot_vo.LOT_11_NM));
        binding.dah14.setText(lot_vo.DAH_14);
        binding.lot18Nm.setText(lot_vo.LOT_18_NM);
        binding.lot18Nm.setText(lot_vo.LOT_18_NM);
        binding.lotwCnt.setText(lot_vo.LOTW_CNT +" 명");
    }

    @Override
    protected void initLayout() {
        GAG_Read();
    }
    private void GAG_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        //기본파라미터
        paramMap = setParamMap("GAG_ID", "M_DETAIL_LIST");
        paramMap.put("GAG_01",lot_vo.LOT_01);

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
        mAdapter.setList(data);
    }

    private void onItemClickGoInfo(View view, int position) {
        Intent intent = new Intent(mContext, Lot02SubDetailActivity.class);
        intent.putExtra("GAG_VO", mList.get(position));

        mActivity.startActivityForResult(intent, Lot02SubDetailActivity.REQUEST_CODE);
    }

    private void onClickGoHome(View v){
        Intent intent = new Intent(this, MainDashboardActivity.class);
        startActivity(intent);
    }
    private void onClickGoTop(View v) {
        binding.recyclerView.smoothScrollToPosition(0);
    }

    private void onClickAdd(View v) {
        Intent intent = new Intent(mContext, Lot02SubAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("LOT_VO", lot_vo);
        mActivity.startActivityForResult(intent, Lot02SubAddActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       if (requestCode == Lot02SubAddActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            GAG_Read();
        }
    }
}
