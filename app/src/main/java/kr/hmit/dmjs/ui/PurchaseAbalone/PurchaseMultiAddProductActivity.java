package kr.hmit.dmjs.ui.PurchaseAbalone;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.dmjs.databinding.ActivityPurchaseMultiAddProductBinding;

import kr.hmit.dmjs.ui.PurchaseAbalone.vo.MultiItemVO;
import kr.hmit.dmjs.ui.order_request.OderRequestFindProductActivity;

import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;

public class PurchaseMultiAddProductActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8132;
    public static final String ORDER_REQUEST_ADD = "ORDER_REQUEST_ADD";

    private ActivityPurchaseMultiAddProductBinding binding;

    private ProductionInfoVO dah_vo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPurchaseMultiAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(this::onClickSave);
        binding.addProductNum.setOnClickListener(this::onClickAddProduct);
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
    private void onClickSave(View view) {
        if (binding.tvDAH02.getText().toString().isEmpty()) {
            toast("품명을 선택해주세요.");
        }else if (binding.tvNGG06.getText().toString().equals("")) {
            toast("수량은 최소 1이상 입니다.");
        }else if (Integer.parseInt(binding.tvNGGK04.getText().toString())==0) {
            toast("수량은 최소 1이상 입니다.");
        }else {
            MultiItemVO tempVO=new MultiItemVO(
                    dah_vo.DAH_01,
                    dah_vo.DAH_02,
                    dah_vo.DAH_11,
                    binding.tvNGG06.getText().toString(),
                    binding.tvNGG07.getText().toString(),
                    binding.tvNGG08.getText().toString(),
                    binding.tvNGG0901.getText().toString(),
                    binding.tvNGGK04.getText().toString(),
                    binding.tvNGG97.getText().toString()
            );
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            intent.putExtra("productInfo",tempVO);
            finish();
        }
    }
    private void onClickAddProduct(View v) {
        Intent intent = new Intent(mContext, OderRequestFindProductActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, OderRequestFindProductActivity.REQUEST_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 담당자 추가
        if (requestCode == OderRequestFindProductActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            dah_vo=(ProductionInfoVO) data.getSerializableExtra(OderRequestFindProductActivity.PRODUCT_LIST);
            binding.tvNGG05.setText(dah_vo.DAH_11);
            binding.tvDAH02.setText(dah_vo.DAH_02);
            binding.tvNGG08.setText("10");
        }
    }

}
