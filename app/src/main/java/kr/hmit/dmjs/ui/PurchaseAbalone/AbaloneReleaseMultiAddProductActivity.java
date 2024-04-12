package kr.hmit.dmjs.ui.PurchaseAbalone;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;

import kr.hmit.base.base_activity.BaseActivity;

import kr.hmit.dmjs.databinding.ActivityAbaloneReleaseMultiAddProductBinding;

import kr.hmit.dmjs.model.vo.NGOC_VO;
import kr.hmit.dmjs.model.vo.NGO_VO;
import kr.hmit.dmjs.ui.order_request.OderRequestFindProductActivity;

import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;

public class AbaloneReleaseMultiAddProductActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8132;
    public static final String ORDER_REQUEST_ADD = "ORDER_REQUEST_ADD";

    private ActivityAbaloneReleaseMultiAddProductBinding binding;

    private ProductionInfoVO dah_vo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAbaloneReleaseMultiAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(this::onClickSave);
        binding.addProductNum.setOnClickListener(this::onClickAddProduct);
        binding.tvNGOC05.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });
        binding.tvNGOC04.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

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

        int NGOC_04 = Integer.parseInt(binding.tvNGOC04.getText().toString().isEmpty()?"0":binding.tvNGOC04.getText().toString());
        int NGOC_05 = Integer.parseInt(binding.tvNGOC05.getText().toString().isEmpty()?"0":binding.tvNGOC05.getText().toString());


        binding.tvNGOC06.setText("" + NGOC_04*NGOC_05);


        //  int NGO_06 = Integer.parseInt(binding.tvNGO06.getText().toString());
        //  int NGO_05 = Integer.parseInt(binding.tvNGO05.getText().toString());


    }


    private void onClickSave(View view) {
        if (binding.tvNGOC03.getText().toString().isEmpty()) {
            toast("품명을 선택해주세요.");
        }else if (binding.tvNGOC05.getText().toString().equals("")) {
            toast("주문수량은 최소 1이상 입니다.");
        }else {
            NGO_VO tempVO=new NGO_VO(dah_vo.DAH_01,dah_vo.DAH_11,binding.tvNGOC05.getText().toString(),binding.tvNGOC06.getText().toString(),binding.tvNGOC97.getText().toString());
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

            binding.tvNGOC03.setText(dah_vo.DAH_02);
            binding.tvUnit.setText(dah_vo.DAH_04);
            binding.tvNGOC04.setText(dah_vo.DAH_11);
        }
    }

}
