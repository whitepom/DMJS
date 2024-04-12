package kr.hmit.dmjs.ui.stockage_list;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityStockageAdjustmentListAddBinding;
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

public class StockageListAdjustmentAddActivit extends BaseActivity {
    public static final int REQUEST_CODE = 4261;
    public static int cnt=0;

    private ActivityStockageAdjustmentListAddBinding binding;

    private String[] mCategory;
    private ProductionInfoVO ookVO;

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
        binding = ActivityStockageAdjustmentListAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        ookVO = (ProductionInfoVO)intent.getExtras().get("ookVO");


        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.tvDate.setOnClickListener(this::onClickDate);
        binding.tvState.setOnClickListener(this::onClickCategory);
        binding.btnSave.setOnClickListener(this::onClickSave);
    }

    @Override
    protected void initialize() {
        mCategory= new String[]{"입고","출고"};

        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
        binding.tvDate.setText(sdfFormat.format(mCalRequest.getTime()));
    }

    private void onClickDate(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void onClickCategory(View v) {
        dropdownCategory("입출고",mCategory,binding.tvState);
    }



    private void dropdownCategory(String dialogTitle, String[] category, TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(dialogTitle)
                .setItems(category, (dialog, which) -> {
                    textView.setText(category[which]);
                }).setCancelable(false).create();

        builder.show();
    }

    private void onClickSave(View view) {
        if (binding.tvQuantity.getText().toString().isEmpty()) {
            toast("조정수량을 입력해주세요.");
        } else if (binding.tvNote.getText().toString().isEmpty()) {
            toast("조정사유를 입력해주세요.");
        }else {
            requestSaveOOK();
        }
    }

    public static String getCategory(String src){
        switch (src){
            case "입고" : return "I";
            case "출고" : return "O";
        }
        return "";
    }
    public void requestSaveOOK() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();
        Http.ook(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).OOK_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_INSERT",
                mUser.Value.MEM_CID,
                "",
                binding.tvDate.getText().toString().replaceAll("-",""),
                "",
                ookVO.DAH_01,
                "C",
                getCategory(binding.tvState.getText().toString()),
                Integer.parseInt(binding.tvQuantity.getText().toString()),
                "",
                0,
                "",
                "",
                binding.tvNote.getText().toString(),  // 조정사유 - 비고에
                mUser.Value.MEM_01

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
                        toast("재고 조정정보를 추가하였습니다.");
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
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

}
