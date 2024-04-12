package kr.hmit.dmjs.ui.product_info;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityProductInfoAddBinding;
import kr.hmit.dmjs.model.response.CDO_Model;
import kr.hmit.dmjs.model.response.TYC_Model;
import kr.hmit.dmjs.model.vo.CDO_VO;
import kr.hmit.dmjs.model.vo.TYC_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductInfoActivity extends BaseActivity {
    public static final int REQUEST_CODE = 6001;
    public static final String PRODUCT_ADD_INFO = "PRODUCT_ADD_INFO";

//================================
    // region // view
    //================================

    private ActivityProductInfoAddBinding binding;
    private String[] mMaterialList;
    private String[] mUnitList;
    private String[] mProductList;
    private String[] mProductCodeList;
    private String[] btnValueList;

    private String dah_07="";

    //================================
    // endregion // view
    //================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductInfoAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();

        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.etUnit.setOnClickListener(this::onClickUnit);
        binding.etProductClassification.setOnClickListener(this::onClickProductClassification);
        binding.etMaterialClassification.setOnClickListener(this::onClickMaterialClassification);
        binding.btnSave.setOnClickListener(this::onClickSave);

        binding.radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId==binding.taxationBtn.getId()){
                        btnValueList[0]="T";
                    }else{
                        btnValueList[0]="D";
                    }
            }
        });
        binding.radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==binding.nonProductionBtn.getId()){
                    btnValueList[1]="N";
                }else if(checkedId==binding.directProductionBtn.getId()){
                    btnValueList[1]="D";
                }else{
                    btnValueList[1]="O";
                }
            }
        });
        binding.radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==binding.generalBtn.getId()){
                    btnValueList[2]="1";
                }else if(checkedId==binding.pesticideFreeBtn.getId()){
                    btnValueList[2]="2";
                }else{
                    btnValueList[2]="3";
                }
            }
        });

    }

    @Override
    protected void initialize() {
        mMaterialList= new String[]{"원재료","부재료","반제품","완제품"};
        mUnitList= new String[]{};
        mProductList= new String[]{};
        btnValueList=new String[]{"T","N","1"};


        requestTYC_Read();

    }
    private void onClickUnit(View v) {
        dropdownCategory("단위",mUnitList,binding.etUnit);
    }
    private void onClickProductClassification(View v) {
        dropdownCategory("제품분류",mProductList,binding.etProductClassification);
    }
    private void onClickMaterialClassification(View v) {
        dropdownCategory("자재구분",mMaterialList,binding.etMaterialClassification);
    }
    private void onClickSave(View v) {
        if (binding.etProductName.getText().toString().isEmpty()) {
            toast("품명을 입력해주세요.");
        } else if (binding.etUnit.getText().toString().equals("단위")) {
            toast("단위를 선택해주세요.");
        } else if (binding.etProductClassification.getText().toString().equals("제품분류")) {
            toast("제품분류를 선택해주세요.");
        }else {
            requestSaveDAH();
        }
    }
    public static String getMaterial(String src){
        switch (src.trim()){
            case "원재료" : return "R";
            case "부재료" : return "B";
            case "반제품" : return "S";
            case "완제품" : return "P";
        }
        return "R";
    }
    private void dropdownCategory(String dialogTitle, String[] category, TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(dialogTitle)
                .setItems(category, (dialog, which) -> {
                    textView.setText(category[which]);
                    if(dialogTitle.equals("제품분류"))
                        dah_07=mProductCodeList[which];
                }).setCancelable(false).create();

        builder.show();
    }
    public void requestSaveDAH() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();
        Http.dah(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).DAH_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_INSERT",
                mUser.Value.MEM_CID,
                "",
                binding.etProductName.getText().toString(),
                getMaterial(binding.etMaterialClassification.getText().toString()),
                binding.etUnit.getText().toString(),
                0,
                btnValueList[2],
                dah_07,
                btnValueList[0],
                btnValueList[1],
                "",
                binding.etBasicUnitPrice.getText().toString(),
                "",
                "",
                binding.etStandard.getText().toString(),

                0,
                "",
                "",
                0,
                0,
                0,
                0,
                "",
                "",
                "",
                "",
                ""
        ).enqueue(new Callback<BaseModel>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                Message msg = new Message();
                msg.obj = response;
                msg.what = 100;

                //=====================
                // response callback
                //=====================
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        closeLoadingBar();
                        toast("제품정보를 추가하였습니다.");
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }.sendMessage(msg);
            }


            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                call.cancel();
                closeLoadingBar();
            }
        });
    }

    private void bindingCategory(ArrayList<CDO_VO> data) {
        mProductList = new String[data.size()];
        mProductCodeList=new String[data.size()];
        for(int i=0;i<data.size();i++){
            mProductList[i] = data.get(i).CDO_03;
            mProductCodeList[i] = data.get(i).CDO_02;
        }

    }
    private void requestTYC_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.dah(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).TYC_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST",
                mUser.Value.MEM_CID,
                "Y"
        ).enqueue(new Callback<TYC_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<TYC_Model> call, Response<TYC_Model> response) {
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

                            Response<TYC_Model> response = (Response<TYC_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                bindingCategory2(response.body().Data);
                            } else {
                                BaseAlert.show(mContext, mContext.getString(R.string.network_error_2) + "-" + response.errorBody());
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<TYC_Model> call, Throwable t) {
                closeLoadingBar();
                call.cancel();
                BaseAlert.show(mContext, R.string.network_error_2);
            }
        });
    }
    private void bindingCategory2(ArrayList<TYC_VO> data) {
        mUnitList = new String[data.size()];
        for(int i=0;i<data.size();i++){
            mUnitList[i] = data.get(i).TYC_01;
        }

    }


}