package kr.hmit.dmjs.ui.PurchaseAbalone;

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

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityOrderManagementInfoMainBinding;
import kr.hmit.dmjs.databinding.ActivityPurchaseDetailBinding;
import kr.hmit.dmjs.model.response.NGG_Model;

import kr.hmit.dmjs.model.vo.NGG_VO;
import kr.hmit.dmjs.model.vo.NGG_VO;
import kr.hmit.dmjs.network.Http;

import kr.hmit.dmjs.ui.PurchaseAbalone.adapter.PurchaseDetailListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseDetailActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8844;

    private ActivityPurchaseDetailBinding binding;

    private PurchaseDetailListAdapter mAdapter;
    private ArrayList<NGG_VO> mListTotal;

    private NGG_VO nggVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPurchaseDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        nggVO = (NGG_VO) intent.getExtras().get("nggVO");

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
       // binding.imgAdd.setOnClickListener(this::onClickGoWriteOrder);
        binding.imgUpdate.setOnClickListener(this::onClickUpdate);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

        binding.tvNGG03.setText(nggVO.NGG_03);
        binding.tvReceiverName.setText(nggVO.NGG_03_NM);
        binding.tvReceiveDate.setText(datePatternChange(nggVO.NGG_02));

    }

    @Override
    protected void initialize() {
        mListTotal = new ArrayList<>();

        mAdapter = new PurchaseDetailListAdapter(mContext,mListTotal);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

        requestNGG_Read();
    }

    private void requestNGG_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.ngg(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).NGG_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_DETAIL_LIST",
                mUser.Value.MEM_CID,

                "",
                nggVO.NGG_02, // NGG_02
                "",
                nggVO.NGG_03,
                "",""
        ).enqueue(new Callback<NGG_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<NGG_Model> call, Response<NGG_Model> response) {
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

                            Response<NGG_Model> response = (Response<NGG_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if (response.body().Data.get(0).Validation) {
                                        bindingData(response.body().Data);
                                    } else {
                                        BaseAlert.show(mContext, getString(R.string.network_error_2) + "--" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                }else{
                                    bindingData(new  ArrayList<NGG_VO>());
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
            public void onFailure(Call<NGG_Model> call, Throwable t) {
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
    private void bindingData(ArrayList<NGG_VO> data) {
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
        Intent intent = new Intent(mContext, PurchaseMultiAddProductUpdateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("nggVO", mListTotal.get(position));
        mActivity.startActivityForResult(intent, PurchaseMultiAddProductUpdateActivity.REQUEST_CODE);
    }
    /*private void onClickGoWriteOrder(View v) {
        Intent intent = new Intent(mContext, PurchaseDetailAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("nggVO",nggVO);
        mActivity.startActivityForResult(intent, PurchaseDetailAddActivity.REQUEST_CODE);
    }*/
    private void onClickUpdate(View v) {
        Intent intent = new Intent(mContext, PurchaseMasterUpdateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("nggVO", nggVO);
        mActivity.startActivityForResult(intent, PurchaseMasterUpdateActivity.REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == PurchaseMultiAddProductUpdateActivity.REQUEST_CODE||requestCode == PurchaseMasterUpdateActivity.REQUEST_CODE) && resultCode == RESULT_OK) {
            requestNGG_Read();
        }else if(requestCode == PurchaseMasterUpdateActivity.REQUEST_CODE&&resultCode == RESULT_OK){
//            nggVO=(ODM_VO) data.getSerializableExtra("nggVO");
//            binding.tvReceiveNum.setText(nggVO.NGG_01);
//            binding.tvReceiverName.setText(nggVO.CLT_01_NM);
//            binding.tvReceiveDate.setText(datePatternChange(nggVO.NGG_02));
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();

        }


    }
}
