package kr.hmit.dmjs.ui.Cusomer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import kr.hmit.dmjs.R;


import kr.hmit.dmjs.databinding.ActivityCustomerSaleDetailBinding;
import kr.hmit.dmjs.model.response.RUN2_Model;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.network.Http;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.ui.Cusomer.adapter.CustomerSaleDetailListAdapter;
import kr.hmit.dmjs.ui.Cusomer.model.MultiItemVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerSaleDetailActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8844;

    private ActivityCustomerSaleDetailBinding binding;

    private CustomerSaleDetailListAdapter mAdapter;
    private ArrayList<MultiItemVO> mListTotal;

    private MultiItemVO run2VO;
    private CLT_VO clt_vo;
    private String RUN_03;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerSaleDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        clt_vo = (CLT_VO) intent.getExtras().get("cltVO");

        RUN_03=clt_vo.CLT_01;

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.imgAdd.setOnClickListener(this::onClickGoWriteOrder);
        binding.btnUpdate.setOnClickListener(this::onClickUpdate);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));



    }

    @Override
    protected void initialize() {
        mListTotal = new ArrayList<>();
        binding.tvCLT02.setText(clt_vo.CLT_02);
        binding.tvCLT08.setText(clt_vo.CLT_08);
        binding.tvCLT1001.setText(clt_vo.CLT_1002+" "+clt_vo.CLT_1003);
        mAdapter = new CustomerSaleDetailListAdapter(mContext,mListTotal);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);


        requestRUN2_Read(RUN_03);


    }

    private void requestRUN2_Read(String CLT_01) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.run2(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).RUN2_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_CLT2_LIST",
                mUser.Value.MEM_CID,

                CLT_01,
                "",
                ""

        ).enqueue(new Callback<RUN2_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<RUN2_Model> call, Response<RUN2_Model> response) {
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

                            Response<RUN2_Model> response = (Response<RUN2_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if (response.body().Data.get(0).Validation) {
                                        bindingData(response.body().Data);
                                    } else {
                                        BaseAlert.show(mContext, getString(R.string.network_error_2) + "--" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                }else{
                                    bindingData(new  ArrayList<MultiItemVO>());
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
            public void onFailure(Call<RUN2_Model> call, Throwable t) {
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
    private void bindingData(ArrayList<MultiItemVO> data) {
        mListTotal = data;
        mAdapter.update(mListTotal);
    }
    private String datePatternChange(String date){
        if(date.isEmpty())
            return date;
        SimpleDateFormat beforeFormat = new SimpleDateFormat("yyyymmdd");
        SimpleDateFormat afterFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date tempDate = null;

        try {
            tempDate = beforeFormat.parse(date);
        } catch (ParseException e) {
        }

        return  afterFormat.format(tempDate);
    }

    private void onItemClickGoInfo(View view, int position) {
        Intent intent = new Intent(mContext, CustomerSaleDetailUpdateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("run2VO", mListTotal.get(position));
        intent.putExtra("cltVO", clt_vo);
        mActivity.startActivityForResult(intent, CustomerSaleDetailUpdateActivity.REQUEST_CODE);
    }
    private void onClickGoWriteOrder(View v) {
        Intent intent = new Intent(mContext, CustomerSaleDetailAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        intent.putExtra("cltVO",clt_vo);
        mActivity.startActivityForResult(intent, CustomerSaleDetailAddActivity.REQUEST_CODE);
    }
    private void onClickUpdate(View v) {
        Intent intent = new Intent(mContext, CustomerSaleMasterUpdateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("cltVO", clt_vo);
        mActivity.startActivityForResult(intent, CustomerSaleMasterUpdateActivity.REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == CustomerSaleMasterUpdateActivity.REQUEST_CODE||requestCode == CustomerSaleDetailAddActivity.REQUEST_CODE) && resultCode == RESULT_OK) {
            requestRUN2_Read(RUN_03);
        }else if(requestCode == CustomerSaleDetailUpdateActivity.REQUEST_CODE&&resultCode == RESULT_OK){
//            run2VO=(ODM_VO) data.getSerializableExtra("run2VO");
//            binding.tvReceiveNum.setText(run2VO.ODM_01);
//            binding.tvReceiverName.setText(run2VO.CLT_01_NM);
//            binding.tvReceiveDate.setText(datePatternChange(run2VO.ODM_02));
            Intent intent = new Intent();
            requestRUN2_Read(RUN_03);
            setResult(Activity.RESULT_OK, intent);
            finish();

        }


    }
}
