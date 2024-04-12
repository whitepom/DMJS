package kr.hmit.dmjs.ui.order_management;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityOrderManagementInfoAddBinding;
import kr.hmit.dmjs.model.vo.ODM_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderManagementInfoAdd extends BaseActivity{

    public static final int REQUEST_CODE = 8124;
    public static final String OrderManagementInfo_ADD = "OrderManagementInfo_ADD";
    private ODM_VO odmVO;
    private ActivityOrderManagementInfoAddBinding binding;
    private ProductionInfoVO vo;

    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            binding.tvDate.setText(sdfFormat.format(mCalRequest.getTime()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderManagementInfoAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        odmVO = (ODM_VO) intent.getExtras().get("odmVO");

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {

        binding.imgBack.setOnClickListener(v -> finish());
        binding.addProduct.setOnClickListener(this::onClickAddProduct);
        binding.tvDate.setOnClickListener(this::onClickDate);
        binding.btnSave.setOnClickListener(this::onClickSave);
        binding.tvCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int tvUnitPrice = Integer.parseInt(binding.tvUnitPrice.getText().toString().equals("")?"0":binding.tvUnitPrice.getText().toString());
                int tvCount = Integer.parseInt(binding.tvCount.getText().toString().equals("")?"0":binding.tvCount.getText().toString());
                binding.tvPrice.setText("" + tvCount*tvUnitPrice);
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
                int tvCount = Integer.parseInt(binding.tvCount.getText().toString().equals("")?"0":binding.tvCount.getText().toString());
                binding.tvPrice.setText("" + tvCount*tvUnitPrice);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    private void onClickDate(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    protected void initialize() {
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
        binding.tvDate.setText(sdfFormat.format(mCalRequest.getTime()));
    }

    private void onClickAddProduct(View v) {
        Intent intent = new Intent(mContext, FindProductActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, FindProductActivity.REQUEST_CODE);
    }
    private void onClickSave(View view) {
        if (binding.tvProductName.getText().toString().isEmpty()) {
            toast("제품정보를 선택해주세요.");
        } else if (binding.tvDate.getText().toString().isEmpty()) {
            toast("납기일자를 선택해주세요.");
        }else {
            requestSaveODD();
        }
    }
    public void requestSaveODD() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();
        Http.odd(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).ODD_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_INSERT",
                mUser.Value.MEM_CID,
                odmVO.ODM_01,
                0,
                vo.DAH_01,
                binding.tvUnitPrice.getText().toString(),
                binding.tvCount.getText().toString(),
                binding.tvPrice.getText().toString(),
                "",
                "",
                "",
                binding.tvDate.getText().toString().replaceAll("-",""),
                binding.tvNote.getText().toString(),
                "",
                "",
                mUser.Value.MEM_02,
                "",
                "",
                "",
                vo.DAH_04
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
                        toast("발주상세정보를 생성하였습니다.");
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
 @Override
 protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
     super.onActivityResult(requestCode, resultCode, data);
     // 담당자 추가
     if (requestCode == FindProductActivity.REQUEST_CODE && resultCode == RESULT_OK) {
         vo=(ProductionInfoVO) data.getSerializableExtra(FindProductActivity.PRODUCT_LIST);
         binding.tvProductName.setText(vo.DAH_02);
         binding.tvProductNum.setText(vo.DAH_01);
     }

 }
}
