
package kr.hmit.dmjs.ui.Cusomer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;

import kr.hmit.dmjs.databinding.ActivityCustomerSaleMasterAddBinding;

import kr.hmit.dmjs.model.response.CLT_Model;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.order_request.ClientNameListActivity;
import kr.hmit.dmjs.ui.sample.AddressActivity;
import kr.hmit.dmjs.ui.sample.AddressModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerSaleMasterAddActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8023;
    public static final String OrderManagement_ADD = "OrderManagement_ADD";

    private ActivityCustomerSaleMasterAddBinding binding;
    private CLT_VO clt_vo;

    private ArrayList<CLT_VO> productList;
    private AddressModel addressData;

    private int index;

    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerSaleMasterAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(this::onClickSave);
        binding.addClientAddress.setOnClickListener(this::onClickAddClientAddress);

    }
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {

    }

    private void onClickAddClientAddress(View v) {
        Intent intent = new Intent(mContext, AddressActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, AddressActivity.REQUEST_CODE);
    }
    private void onClickSave(View view) {

        if  (binding.tvCLT02.getText().toString().isEmpty()) {
            toast("성명을 입력 해주세요.");
        }else if  (binding.tvClientPostalCode.getText().toString().isEmpty()) {
            toast("주소를 입력 해주세요.");
        }else {
            requestSaveCLT();

        }
    }
    private void dropdownCategory(String dialogTitle, String[] category, TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(dialogTitle)
                .setItems(category, (dialog, which) -> {
                    textView.setText(category[which]);
                }).setCancelable(false).create();

        builder.show();
    }


    public void requestSaveCLT() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();
        Http.clt(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).CLT_U2(
                BaseConst.URL_HOST,
                "DMJS",
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_INSERT",
                mUser.Value.MEM_CID,
                "",
                "G",
                binding.tvCLT02.getText().toString(),
                binding.tvCLT06.getText().toString(),
                binding.tvCLT07.getText().toString(),
                binding.tvCLT08.getText().toString().replaceAll("-",""),
                binding.tvCLT13.getText().toString().replaceAll("-",""),
                binding.tvClientPostalCode.getText().toString(),
                binding.tvClientAddress.getText().toString(),
                binding.etClientAddress.getText().toString(),

                binding.tvCLT19.getText().toString(),
                binding.tvCLT20.getText().toString(),
                binding.tvCLT21.getText().toString()

        ).enqueue(new Callback<CLT_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<CLT_Model> call, Response<CLT_Model> response) {
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
            public void onFailure(Call<CLT_Model> call, Throwable t) {
                call.cancel();
                closeLoadingBar();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddressActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            addressData = (AddressModel) data.getSerializableExtra("addressData");
            binding.tvClientPostalCode.setText(addressData.Zipcode);
            binding.tvClientAddress.setText(addressData.Address);

        }
    }
}

