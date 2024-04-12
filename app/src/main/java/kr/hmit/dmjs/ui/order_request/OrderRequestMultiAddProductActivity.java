package kr.hmit.dmjs.ui.order_request;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;

import kr.hmit.dmjs.databinding.ActivityOrderRequestMultiAddProductBinding;
import kr.hmit.dmjs.ui.order_request.model.MultiItemVO;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import kr.hmit.base.base_activity.BaseActivity;

public class OrderRequestMultiAddProductActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8132;
    public static final String ORDER_REQUEST_ADD = "ORDER_REQUEST_ADD";

    private ActivityOrderRequestMultiAddProductBinding binding;

    private ProductionInfoVO dah_vo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderRequestMultiAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(this::onClickSave);
        binding.addProductNum.setOnClickListener(this::onClickAddProduct);


        binding.tvOrderQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int tvUnitPrice = Integer.parseInt(binding.tvUnitPrice.getText().toString());
                int tvCount = Integer.parseInt(binding.tvOrderQuantity.getText().toString());
                binding.tvPricey.setText("" + tvCount*tvUnitPrice);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.tvUnitPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int tvUnitPrice = Integer.parseInt(binding.tvUnitPrice.getText().toString().equals("")?"0":binding.tvUnitPrice.getText().toString());
                int tvCount = Integer.parseInt(binding.tvOrderQuantity.getText().toString().equals("")?"0":binding.tvOrderQuantity.getText().toString());
                binding.tvPricey.setText("" + tvCount*tvUnitPrice);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initialize() {
    }

    private void onClickSave(View view) {
        if (binding.tvProductNum.getText().toString().isEmpty()) {
            toast("품명을 선택해주세요.");
        }else if (binding.tvOrderQuantity.getText().toString().equals("")) {
            toast("주문수량은 최소 1이상 입니다.");
        }else if (Integer.parseInt(binding.tvOrderQuantity.getText().toString())==0) {
            toast("주문수량은 최소 1이상 입니다.");
        }else {
            MultiItemVO tempVO=new MultiItemVO(dah_vo.DAH_01,dah_vo.DAH_02,dah_vo.DAH_04,Integer.parseInt(dah_vo.DAH_11),binding.tvOrderQuantity.getText().toString(),binding.tvPricey.getText().toString());
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
            binding.tvUnit.setText(dah_vo.DAH_04);
            binding.tvProductNum.setText(dah_vo.DAH_02);
            binding.tvUnitPrice.setText(dah_vo.DAH_11+"");
        }
    }

}
