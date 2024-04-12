package kr.hmit.dmjs.ui.stockage_list;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.DecimalFormat;
import java.util.ArrayList;

import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.base.settings.SettingsKey;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityStockageListDetailBinding;
import kr.hmit.dmjs.model.response.OOK_Model;
import kr.hmit.dmjs.model.vo.OOK_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.login.LoginActivity;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import kr.hmit.dmjs.ui.stockage_list.adapter.StockageListAdjustmentAdapter;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.ClsNetworkCheck;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockageListDetailActivity extends BaseActivity {
    public static final int REQUEST_CODE = 6002;
    public static final String StockageList_DETAIL_INFO = "StockageList_DETAIL_INFO";

    private ActivityStockageListDetailBinding binding;
    private StockageListAdjustmentAdapter mAdapter;
    private ArrayList<OOK_VO> mList;
    private ProductionInfoVO ookVO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStockageListDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        ookVO = (ProductionInfoVO)intent.getExtras().get("ookVO");


        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.btnInsert.setOnClickListener(this::onClickAdd);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }


    @Override
    protected void initialize() {
        mList= new ArrayList<>();
        mAdapter = new StockageListAdjustmentAdapter(mContext,mList);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        requestOOK_Read(ookVO);
        binding.recyclerView.setAdapter(mAdapter);

        //바인딩
        binding.etProductNum.setText(ookVO.DAH_01);
        binding.etProductName.setText(ookVO.DAH_02);
        binding.tvStandards.setText(ookVO.DAH_14);
        binding.tvUnitPrice.setText(changeToMoney(ookVO.DAH_11)+"원");
        binding.tvUnit.setText(ookVO.DAH_04);
        binding.tvCDO03.setText(ookVO.CDO_03);


    }
    private String changeToMoney(String str){

        String amount = str;
        amount = amount.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
        return amount;

    }
    private void onItemClickGoInfo(View view, int position) {
        Intent intent = new Intent(mContext, StockageListAdjustmentDetailActivit.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("OOKData",mList.get(position));
        mActivity.startActivityForResult(intent, StockageListAdjustmentDetailActivit.REQUEST_CODE);

    }

    private void onClickAdd(View view) {
        Intent intent = new Intent(mContext, StockageListAdjustmentAddActivit.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("ookVO",ookVO);
        mActivity.startActivityForResult(intent, StockageListAdjustmentAddActivit.REQUEST_CODE);

    }

    public static String getCategory(String src){
        switch (src.trim()){
            case "입고" : return "I";
            case "출고" : return "O";
            case "I" : return "입고";
            case "O" : return "출고";
        }
        return "";
    }
    private void requestOOK_Read(ProductionInfoVO ookVO) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.ook(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).OOK_Read2(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_DETAIL",
                mUser.Value.MEM_CID,
                "",
                "",
                ookVO.DAH_01

        ).enqueue(new Callback<OOK_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<OOK_Model> call, Response<OOK_Model> response) {
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

                            Response<OOK_Model> response = (Response<OOK_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if (response.body().Data.get(0).Validation) {
                                        bindingData(response.body().Data);
                                    } else {
                                        toast("동일계정 접속 > 로그인 페이지로 이동합니다");  // asdfasdfasdf
                                        mSettings.putBooleanItem(SettingsKey.AutoLogin,false);
                                        Intent intent = new Intent(mContext, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("simultaneous_connect",mUser.Value.MEM_01.toString());
                                        startActivity(intent);
                                        finish();
                                       // BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                }else{
                                    bindingData(new  ArrayList<OOK_VO>());
                                    toast("입출고 및 재고조정 이력이 없습니다.");
                                }
                            } else {
                                BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<OOK_Model> call, Throwable t) {
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
    private void bindingData(ArrayList<OOK_VO> data) {
        mList = data;
        mAdapter.update(mList);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == StockageListAdjustmentAddActivit.REQUEST_CODE&&resultCode == RESULT_OK){
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            requestOOK_Read(ookVO);
        }else if(requestCode == StockageListAdjustmentDetailActivit.REQUEST_CODE&&resultCode == RESULT_OK){
            requestOOK_Read(ookVO);
        }
    }

    public static String moneyFormatToWon(double inputMoney) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        return decimalFormat.format(inputMoney);
    }
}
