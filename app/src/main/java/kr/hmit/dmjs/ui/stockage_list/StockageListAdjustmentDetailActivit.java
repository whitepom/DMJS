package kr.hmit.dmjs.ui.stockage_list;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityStockageAdjustmentListUpdateBinding;
import kr.hmit.dmjs.model.vo.OOK_VO;
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

public class StockageListAdjustmentDetailActivit extends BaseActivity {
    public static final int REQUEST_CODE = 4261;

    private ActivityStockageAdjustmentListUpdateBinding binding;

    private String[] mCategory;
    private OOK_VO OOKData;
    private ArrayList<ProductionInfoVO> DAHVO;

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
        binding = ActivityStockageAdjustmentListUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        OOKData = (OOK_VO)intent.getExtras().get("OOKData");

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.tvDate.setOnClickListener(this::onClickDate);
        binding.tvState.setOnClickListener(this::onClickCategory);
        binding.btnSave.setOnClickListener(this::onClickSave);
        binding.btnDelete.setOnClickListener(this::onClickDelete);
    }

    @Override
    protected void initialize() {
        mCategory= new String[]{"입고","출고"};

        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
        binding.tvDate.setText(sdfFormat.format(mCalRequest.getTime()));

        binding.tvDate.setText(OOKData.OOK_02);
        binding.tvState.setText(getCategory(OOKData.OOK_06));
        binding.tvQuantity.setText(OOKData.OOK_07);
        binding.tvNote.setText(OOKData.OOK_97);

    }

    private void onClickDate(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void onClickCategory(View v) {
        dropdownCategory("입출고",mCategory,binding.tvState);
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
            requestSaveOOK("M_UPDATE");
        }
    }

    private void onClickDelete(View view) {
        AlertDialog.Builder oDialog = new AlertDialog.Builder(this,
                android.R.style.Theme_DeviceDefault_Light_Dialog);

        oDialog.setTitle("삭제하시겠습니까?")
                .setPositiveButton("아니오", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                })
                .setNeutralButton("예", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        requestSaveOOK("M_DELETE");
                    }
                })
                .setCancelable(false)
                .show();
    }

    public void requestSaveOOK(String GUBUN) {
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
                GUBUN,
                mUser.Value.MEM_CID,
                OOKData.OOK_01,
                binding.tvDate.getText().toString().replaceAll("-",""),
                "",
                OOKData.OOK_04,
                "C",
                getCategory(binding.tvState.getText().toString()),
                Integer.parseInt(binding.tvQuantity.getText().toString()),
                "",
                0,
                "",
                "",
                binding.tvNote.getText().toString(),
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
                        if(GUBUN=="M_UPDATE"){
                            toast("재고정보를 수정하였습니다.");
                        }else{
                            toast("재고정보를 삭제하였습니다.");
                        }
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
    public static int doubleToInt(double input) {
       int a= (int) Math.round(input);
        return a;
    }

}
