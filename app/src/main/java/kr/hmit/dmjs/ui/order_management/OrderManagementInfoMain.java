package kr.hmit.dmjs.ui.order_management;

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
import kr.hmit.dmjs.databinding.ActivityOrderManagementInfoMainBinding;
import kr.hmit.dmjs.model.response.ODD_Model;
import kr.hmit.dmjs.model.vo.ODD_VO;
import kr.hmit.dmjs.model.vo.ODM_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.order_management.adapter.OrderManagementInfoListAdapter;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderManagementInfoMain extends BaseActivity {
    public static final int REQUEST_CODE = 8844;

    private ActivityOrderManagementInfoMainBinding binding;

    private OrderManagementInfoListAdapter mAdapter;
    private ArrayList<ODD_VO> mListTotal;

    private ODM_VO odmVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderManagementInfoMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        odmVO = (ODM_VO) intent.getExtras().get("odmVO");

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.imgAdd.setOnClickListener(this::onClickGoWriteOrder);
        binding.btnUpdate.setOnClickListener(this::onClickUpdate);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

        binding.tvReceiveNum.setText(odmVO.ODM_01);
        binding.tvReceiverName.setText(odmVO.CLT_02);
        binding.tvReceiveDate.setText(datePatternChange(odmVO.ODM_02));

    }

    @Override
    protected void initialize() {
        mListTotal = new ArrayList<>();

        mAdapter = new OrderManagementInfoListAdapter(mContext,mListTotal);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

        requestODD_Read();
    }

    private void requestODD_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.odm(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).ODD_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST",
                mUser.Value.MEM_CID,

                odmVO.ODM_01,
                "",
                "",
                "",
                ""
        ).enqueue(new Callback<ODD_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<ODD_Model> call, Response<ODD_Model> response) {
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

                            Response<ODD_Model> response = (Response<ODD_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if (response.body().Data.get(0).Validation) {
                                        bindingData(response.body().Data);
                                    } else {
                                        BaseAlert.show(mContext, getString(R.string.network_error_2) + "--" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                }else{
                                    bindingData(new  ArrayList<ODD_VO>());
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
            public void onFailure(Call<ODD_Model> call, Throwable t) {
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
    private void bindingData(ArrayList<ODD_VO> data) {
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
        Intent intent = new Intent(mContext, OrderManagementInfoDetail.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("oddVO", mListTotal.get(position));
        intent.putExtra("odmVO",odmVO);
        mActivity.startActivityForResult(intent, OrderManagementInfoDetail.REQUEST_CODE);
    }
    private void onClickGoWriteOrder(View v) {
        Intent intent = new Intent(mContext, OrderManagementInfoAdd.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("odmVO",odmVO);
        mActivity.startActivityForResult(intent, OrderManagementInfoAdd.REQUEST_CODE);
    }
    private void onClickUpdate(View v) {
        Intent intent = new Intent(mContext, OrderManagementDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("odmVO", odmVO);
        mActivity.startActivityForResult(intent, OrderManagementDetailActivity.REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == OrderManagementInfoDetail.REQUEST_CODE||requestCode == OrderManagementInfoAdd.REQUEST_CODE) && resultCode == RESULT_OK) {
            requestODD_Read();
        }else if(requestCode == OrderManagementDetailActivity.REQUEST_CODE&&resultCode == RESULT_OK){
//            odmVO=(ODM_VO) data.getSerializableExtra("odmVO");
//            binding.tvReceiveNum.setText(odmVO.ODM_01);
//            binding.tvReceiverName.setText(odmVO.CLT_01_NM);
//            binding.tvReceiveDate.setText(datePatternChange(odmVO.ODM_02));
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();

        }


    }
}
