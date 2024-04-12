package kr.hmit.dmjs.ui.PurchaseAbalone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;

import kr.hmit.base.base_activity.BaseActivity;

import kr.hmit.base.base_alret.BaseAlert;

import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityPurchaseMultiAddProductUpdateBinding;
import kr.hmit.dmjs.model.response.NGG_Model;
import kr.hmit.dmjs.model.vo.NGG_VO;

import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.PurchaseAbalone.vo.MultiItemVO;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseMultiAddProductUpdateActivity extends BaseActivity {
    public static final int REQUEST_CODE = 7232;
    public static final String ORDER_REQUEST_ADD = "ORDER_REQUEST_ADD";

    private ActivityPurchaseMultiAddProductUpdateBinding binding;

    private ProductionInfoVO dah_vo;
    private String NGG_01;

    private NGG_VO ngg_vo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPurchaseMultiAddProductUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        ngg_vo  = (NGG_VO) intent.getExtras().get("nggVO");
       /* NGG_01= ngg_vo.NGG_01;
        vo.NGG_04=ngg_vo.NGG_04;
        vo.NGG_05= ngg_vo.NGG_05;
        vo.NGG_06= ngg_vo.NGG_06;
        vo.NGG_07= ngg_vo.NGG_07;
        vo.NGG_08= ngg_vo.NGG_08;
        vo.NGG_0901= ngg_vo.NGG_0901;
        vo.NGG_0902= ngg_vo.NGG_0902;
        vo.NGG_97= ngg_vo.NGG_97;*/



        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());

        binding.btnUpdate.setOnClickListener(this::onClickUpdate);
        binding.btnDelete.setOnClickListener(this::onClickDelete);


        binding.tvNGG06.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               /* int NGG_05 = Integer.parseInt(binding.tvNGG05.getText().toString());
                int NGG_06 = Integer.parseInt(binding.tvNGG06.getText().toString());
                int NGG_08 = Integer.parseInt(binding.tvNGG08.getText().toString());

                binding.tvNGG0901.setText("" + NGG_08*NGG_06);
                binding.tvNGG07.setText("" + NGG_05*NGG_08*NGG_06);*?

                */
              //  calculate();
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });
        binding.tvNGG05.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               // calculate();
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });
        binding.tvNGG08.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
              //  calculate();
            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });

        binding.tvProductNum.setText(ngg_vo.NGG_04_NM);
        binding.tvNGG05.setText(ngg_vo.NGG_05);
        binding.tvNGG06.setText(ngg_vo.NGG_06);
        binding.tvNGG07.setText(ngg_vo.NGG_07);
        binding.tvNGG08.setText(ngg_vo.NGG_08);
        binding.tvNGG0901.setText(ngg_vo.NGG_0901);
        binding.tvNGGK04.setText(ngg_vo.NGG_0902);
        binding.tvNGG97.setText(ngg_vo.NGG_97);

    }

    @Override
    protected void initialize() {
    }

    private void calculate(){
        int NGG_05 = Integer.parseInt(binding.tvNGG05.getText().toString().isEmpty()?"0":binding.tvNGG05.getText().toString());
        int NGG_06 = Integer.parseInt(binding.tvNGG06.getText().toString().isEmpty()?"0":binding.tvNGG06.getText().toString());
        int NGG_08 = Integer.parseInt(binding.tvNGG08.getText().toString().isEmpty()?"0":binding.tvNGG08.getText().toString());

        binding.tvNGG0901.setText("" + NGG_08*NGG_06);
        binding.tvNGG07.setText("" + NGG_05*NGG_08*NGG_06);
    }
    private void onClickUpdate(View view) {
        if (binding.tvProductNum.getText().toString().isEmpty()) {
            toast("품명을 선택해주세요.");
        }else if (binding.tvNGG06.getText().toString().equals("")) {
            toast("수량은 최소 1이상 입니다.");
        }else if (Integer.parseInt(binding.tvNGGK04.getText().toString())==0) {
            toast("수량은 최소 1이상 입니다.");
        }else {
          /* MultiItemVO tempVO=new MultiItemVO(vo.NGG_04,vo.NGG_04_NM,vo.NGG_05
                    ,binding.tvNGG06.getText().toString()
                    ,binding.tvNGG07.getText().toString()
                    ,binding.tvNGG08.getText().toString()
                    ,binding.tvNGG0901.getText().toString()
                    ,binding.tvNGGK04.getText().toString()
                    ,binding.tvNGG97.getText().toString()
            );*/
          /*  Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            intent.putExtra("productInfo",tempVO);
            finish();*/
            requestSaveNGG_Multi("M_UPDATE");
        }
    }

    public void requestSaveNGG_Multi( String GUBUN) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();
        Http.ngg(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).NGG_U(
                BaseConst.URL_HOST,
                "DMJS",
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                GUBUN,
                mUser.Value.MEM_CID,
                ngg_vo.NGG_01,
                ngg_vo.NGG_02,
                ngg_vo.NGG_03,
                ngg_vo.NGG_04,
                Float.parseFloat(binding.tvNGG05.getText().toString()),
                Float.parseFloat(binding.tvNGG06.getText().toString()),
                Float.parseFloat(binding.tvNGG07.getText().toString()),
                Float.parseFloat(binding.tvNGG08.getText().toString()),
                Float.parseFloat(binding.tvNGG0901.getText().toString()),
                Float.parseFloat(binding.tvNGGK04.getText().toString()),
                "",
                0,
               "","","","",
                binding.tvNGG97.getText().toString(),
                mUser.Value.MEM_01

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
                        closeLoadingBar();
                        toast("수정하였습니다.");
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }.sendMessage(msg);
            }


            @Override
            public void onFailure(Call<NGG_Model> call, Throwable t) {
                call.cancel();
                closeLoadingBar();
            }
        });
    }

    private void onClickDelete(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("수매정보 삭제");
        alertDialogBuilder.setMessage("삭제하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("삭제",
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestSaveNGG_Multi("M_DELETE");
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
    /*private void onClickAddProduct(View v) {
        Intent intent = new Intent(mContext, OderRequestFindProductActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, OderRequestFindProductActivity.REQUEST_CODE);
    }*/



  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 담당자 추가
        if (requestCode == OderRequestFindProductActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            dah_vo=(ProductionInfoVO) data.getSerializableExtra(OderRequestFindProductActivity.PRODUCT_LIST);
            binding.tvUnit.setText(dah_vo.DAH_04);
            binding.tvProductNum.setText(dah_vo.DAH_02);
            binding.tvUnitPrice.setText(dah_vo.DAH_11);
            vo.DAH_01=dah_vo.DAH_01;
            vo.DAH_02=dah_vo.DAH_02;
            vo.DAH_04=dah_vo.DAH_04;
          //  vo.DAH_11=dah_vo.DAH_11;

        }
    }*/

}
