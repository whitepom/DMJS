package kr.hmit.dmjs.ui.m017;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import kr.hmit.dmjs.databinding.ActivityZag02MainDetailBinding;
import kr.hmit.dmjs.model.vo.GAG_VO;
import kr.hmit.dmjs.model.vo.ZAG_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.network.Http_Zag;
import kr.hmit.dmjs.ui.m017.adapter.Zag02SubListAdapter;
import kr.hmit.dmjs.ui.main.MainDashboardActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Zag02MainDetailActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8023;
    public static final String ORDER_REQUEST_ADD = "ZAG_02_DETAIL";

    private ActivityZag02MainDetailBinding binding;
    private Zag02SubListAdapter mAdapter;
    private ArrayList<GAG_VO> mList;

    private ZAG_VO zag_vo = new ZAG_VO();
    private String ZAG_10 ="";

    //파라미터
    private HashMap<String, Object> paramMap = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityZag02MainDetailBinding.inflate(getLayoutInflater());
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

        //
        binding.btnGagaInfo.setOnClickListener(this::onClickGagaInfo);
        binding.btnGagaInfo2.setOnClickListener(this::onClickGagaInfo2);

        mAdapter = new Zag02SubListAdapter();
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

        Intent intent = getIntent();
        zag_vo = (ZAG_VO) intent.getExtras().get("ZAG_VO");

        ZAG_Read(zag_vo.ZAG_01);
    }

    private void DataSetting(ZAG_VO zag_vo){
        binding.zag02.setText(zag_vo.ZAG_02);
        binding.zag01.setText(zag_vo.ZAG_01);
        binding.zag03.setText(zag_vo.ZAG_03 +"/" + zag_vo.ZAG_03_NM);
        binding.zag05.setText(String.valueOf((int)zag_vo.ZAG_05) +"/" +String.valueOf((int)zag_vo.ZAG_04));
        binding.zag12.setText(zag_vo.ZAG_12 +"~"+zag_vo.ZAG_13 +" (" + String.valueOf(zag_vo.ZAG_11) + ")");
        binding.zag06.setText(String.valueOf(zag_vo.ZAG_06));
        binding.dah14.setText(zag_vo.DAH_14);
        binding.zag10Nm.setText(zag_vo.ZAG_10_NM);

        ZAG_10 = zag_vo.ZAG_10;

        if(ZAG_10.equals("N")){
            binding.btnGagaInfo.setText("생산시작");
            binding.btnGagaInfo.setVisibility(View.VISIBLE);
            binding.btnGagaInfo2.setVisibility(View.GONE);
        }
        if(ZAG_10.equals("S")){
            binding.btnGagaInfo.setText("시작취소");
            binding.btnGagaInfo2.setText("작업완료");
            binding.btnGagaInfo2.setVisibility(View.VISIBLE);
        }
        if(ZAG_10.equals("Y")){
            binding.btnGagaInfo.setVisibility(View.GONE);
            binding.btnGagaInfo2.setText("완료취소");
            binding.btnGagaInfo2.setVisibility(View.VISIBLE);
        }
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
        paramMap.put("GAG_01",zag_vo.ZAG_01);

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
        Intent intent = new Intent(mContext, Zag02SubDetailActivity.class);
        intent.putExtra("GAG_VO", mList.get(position));

        mActivity.startActivityForResult(intent, Zag02SubDetailActivity.REQUEST_CODE);
    }

    private void onClickGoHome(View v){
        Intent intent = new Intent(this, MainDashboardActivity.class);
        startActivity(intent);
    }
    private void onClickGoTop(View v) {
        binding.recyclerView.smoothScrollToPosition(0);
    }

    private void onClickAdd(View v) {
        Intent intent = new Intent(mContext, Zag02SubAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("ZAG_VO", zag_vo);
        mActivity.startActivityForResult(intent, Zag02SubAddActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       if (requestCode == Zag02SubAddActivity.REQUEST_CODE && resultCode == RESULT_OK) {
           ZAG_Read(zag_vo.ZAG_01);
           GAG_Read();
        }
    }

    private void onClickGagaInfo(View v) {

        if(ZAG_10.equals("N")){
            ZAG_10 = "S";
            binding.zag10Nm.setText("진행중");

            ZAG_U("ZAG_START" , binding.zag01.getText().toString(), ZAG_10);
        }else if(ZAG_10.equals("S")){
            ZAG_10 ="N";
            binding.zag10Nm.setText("미완료");

            ZAG_U("ZAG_CANCEL" , binding.zag01.getText().toString(), ZAG_10);
        }


        if(ZAG_10.equals("S"))
        {
            binding.btnGagaInfo.setText("시작취소");
            binding.btnGagaInfo.setVisibility(View.VISIBLE);
            binding.btnGagaInfo2.setText("작업완료");
            binding.btnGagaInfo2.setVisibility(View.VISIBLE);
        }else if(ZAG_10.equals("N")){
            binding.btnGagaInfo.setVisibility(View.VISIBLE);
            binding.btnGagaInfo.setText("생산시작");
            binding.btnGagaInfo2.setVisibility(View.GONE);
        }
    }

    private void onClickGagaInfo2(View v) {
        if(ZAG_10.equals("S")){
            ZAG_10 = "Y";
            binding.zag10Nm.setText("완료");

            ZAG_U("ZAG_FINSH" , binding.zag01.getText().toString(), ZAG_10);

        }else if(ZAG_10.equals("Y")){
            ZAG_10 = "S";
            binding.zag10Nm.setText("진행중");

            ZAG_U("ZAG_FINSH_CANCEL" , binding.zag01.getText().toString(), ZAG_10);
        }

        if(ZAG_10.equals("Y"))
        {
            binding.btnGagaInfo.setVisibility(View.GONE);
            binding.btnGagaInfo2.setText("완료취소");
            binding.btnGagaInfo2.setVisibility(View.VISIBLE);
        }else if(ZAG_10.equals("S")){
            binding.btnGagaInfo.setText("시작취소");
            binding.btnGagaInfo.setVisibility(View.VISIBLE);
            binding.btnGagaInfo2.setText("작업완료");
            binding.btnGagaInfo2.setVisibility(View.VISIBLE);
        }
    }

    public void ZAG_U(String GUBUN ,String ZAG_01, String ZAG_10) {

        paramMap = setParamMap("ZAG_ID", GUBUN);
        paramMap.put("ZAG_01", ZAG_01);
        paramMap.put("ZAG_10", ZAG_10);

        Http.zag(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).ZAG_U(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<ZAG_VO>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<ZAG_VO> call, Response<ZAG_VO> response) {
                Message msg = new Message();
                msg.obj = response;
                msg.what = 100;

                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        ((BaseActivity)BaseActivity.BaseContext).closeLoadingBar();
                        ((BaseActivity)BaseActivity.BaseContext).toast("활전복 생산관리를 등록하였습니다.");
                        ZAG_Read(zag_vo.ZAG_01);
                    }
                }.sendMessage(msg);
            }
            @Override
            public void onFailure(Call<ZAG_VO> call, Throwable t) {
                call.cancel();
                ((BaseActivity)BaseActivity.BaseContext).closeLoadingBar();
            }
        });
    }

    private void ZAG_Read(String ZAG_01) {

        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }
        String strSearch = binding.zag03.getText().toString().toUpperCase().trim();

        //기본파라미터
        paramMap = setParamMap("ZAG_ID", "M_DETAIL");
        paramMap.put("ZAG_01",ZAG_01);

        openLoadingBar();

        Http_Zag.zag(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).ZAG_Read(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<ArrayList<ZAG_VO>>() {
            @Override
            public void onResponse(Call<ArrayList<ZAG_VO>> call, Response<ArrayList<ZAG_VO>> response) {
                closeLoadingBar();
                ArrayList<ZAG_VO> data = response.body();

                if (response.isSuccessful()) {
                    if(data.size() > 0){
                        zag_vo = data.get(0);
                        DataSetting(zag_vo);
                    }else{
                        //bindingData(new  ArrayList<ZAG_VO>());
                        BaseAlert.show(mContext,"검색결과가 없습니다." );
                    }
                } else {
                    BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ZAG_VO>> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
                t.printStackTrace();
            }
        });
    }
}
