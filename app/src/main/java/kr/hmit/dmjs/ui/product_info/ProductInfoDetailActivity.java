package kr.hmit.dmjs.ui.product_info;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import kr.hmit.base.base_activity.BaseActivity;

public class  ProductInfoDetailActivity extends BaseActivity {
    public static final int REQUEST_CODE = 6002;
    public static final String PRODUCT_DETAIL_INFO = "PRODUCT_DETAIL_INFO";

//================================
    // region // view
    //================================

    private kr.hmit.dmjs.databinding.ActivityProductInfoDetailBinding binding;
    private String[] mMaterialList;
    private String[] mUnitList;
    private String[] mProductList;
    private String[] mProductCodeList;
    private String[] btnValueList;

    private String dah_07="";

    private ProductionInfoVO vo;
    //================================
    // endregion // view
    //================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = kr.hmit.dmjs.databinding.ActivityProductInfoDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();

        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
     //   binding.etUnit.setOnClickListener(this::onClickUnit);
     //   binding.etProductClassification.setOnClickListener(this::onClickProductClassification);
     //   binding.etMaterialClassification.setOnClickListener(this::onClickMaterialClassification);
     //   binding.btnUpdate.setOnClickListener(this::onClickUpdate);
     //   binding.btnDelete.setOnClickListener(this::onClickDelete);

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
        mMaterialList = new String[]{"원재료", "부재료", "반제품", "완제품"};
        mUnitList = new String[]{};
        mProductList = new String[]{};
        btnValueList = new String[]{"T", "N", "1"};

        Intent intent = getIntent();
        vo = (ProductionInfoVO) intent.getExtras().get("DAHData");

        binding.etProductName.setText(vo.DAH_02);
        binding.etUnit.setText(vo.DAH_04);
        binding.etProductClassification.setText(vo.CDO_03);
      //  binding.etMaterialClassification.setText(getMaterial(vo.DAH_03));
        binding.etStandard.setText(vo.DAH_14);
        binding.etBasicUnitPrice.setText("" + vo.DAH_11);
        //   dah_07=vo.DAH_07;
/*
        if(vo.DAH_08.equals("T")){
            binding.taxationBtn.setChecked(true);
        }else {
            binding.nonTaxableBtn.setChecked(true);
        }
        if(vo.DAH_09.equals("N")){
            binding.nonProductionBtn.setChecked(true);
        }else if(vo.DAH_09.equals("D")){
            binding.directProductionBtn.setChecked(true);
        }else{
            binding.OEMProductionBtn.setChecked(true);
        }
        if(vo.DAH_06.equals("1")){
            binding.generalBtn.setChecked(true);
        }else if(vo.DAH_06.equals("2")){
            binding.pesticideFreeBtn.setChecked(true);
        }else{
            binding.organicFarmingBtn.setChecked(true);
        }

        requestCDO_Read();
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
    private void onClickUpdate(View v) {
        if (binding.etProductName.getText().toString().isEmpty()) {
            toast("품명을 입력해주세요.");
        } else if (binding.etUnit.getText().toString().equals("단위")) {
            toast("단위를 선택해주세요.");
        } else if (binding.etProductClassification.getText().toString().equals("제품분류")) {
            toast("제품분류를 선택해주세요.");
        }else {
            requestSaveDAH("M_UPDATE");
        }
    }
    private void onClickDelete(View v) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("제품정보 삭제");
        alertDialogBuilder.setMessage("삭제하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("삭제",
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestSaveDAH("M_DELETE");
                            }
                        }).setNegativeButton("취소",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public static String getMaterial(String src){
        switch (src.trim()){
            case "원재료" : return "R";
            case "부재료" : return "B";
            case "반제품" : return "S";
            case "완제품" : return "P";
            case "R" : return "원재료";
            case "B" : return "부재료";
            case "S" : return "반제품";
            case "P" : return "완제품";
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
    public void requestSaveDAH(String GUBUN) {
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
                GUBUN,
                mUser.Value.MEM_CID,
                vo.DAH_01,
                binding.etProductName.getText().toString(),
                getMaterial(binding.etMaterialClassification.getText().toString()),
                binding.etUnit.getText().toString(),
                vo.DAH_05,
                btnValueList[2],
                dah_07,
                btnValueList[0],
                btnValueList[1],
                vo.DAH_10,
                binding.etBasicUnitPrice.getText().toString(),
                vo.DAH_12,
                vo.DAH_13,
                binding.etStandard.getText().toString(),
                vo.DAH_15,
                vo.DAH_1601,
                vo.DAH_1602,
                vo.DAH_1603,
                vo.DAH_17,
                vo.DAH_18,
                vo.DAH_19,
                vo.DAH_20,
                vo.DAH_80,
                vo.DAH_98,
                vo.DAH_99,
                vo.DAH_21

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
    private void requestCDO_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.red(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).CDO_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST",
                mUser.Value.MEM_CID,
                "GDR",
                "",
                "",
                "Y"

        ).enqueue(new Callback<CDO_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<CDO_Model> call, Response<CDO_Model> response) {
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

                            Response<CDO_Model> response = (Response<CDO_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                bindingCategory(response.body().Data);
                            } else {
                                BaseAlert.show(mContext, mContext.getString(R.string.network_error_2) + "-" + response.errorBody());
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<CDO_Model> call, Throwable t) {
                closeLoadingBar();
                call.cancel();
                BaseAlert.show(mContext, R.string.network_error_2);
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
*/
    }
}