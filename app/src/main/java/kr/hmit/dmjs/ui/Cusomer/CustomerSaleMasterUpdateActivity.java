
package kr.hmit.dmjs.ui.Cusomer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityCustomerSaleMasterUpdateBinding;
import kr.hmit.dmjs.databinding.ActivityOrderManagementDetailBinding;
import kr.hmit.dmjs.model.response.CLT_Model;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.model.vo.ODM_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.UtilBox;
import kr.hmit.dmjs.ui.order_request.ClientNameListActivity;
import kr.hmit.dmjs.ui.sample.AddressActivity;
import kr.hmit.dmjs.ui.sample.AddressModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerSaleMasterUpdateActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8024;
    public static final String OrderManagement_detail = "OrderManagement_detail";

    private ActivityCustomerSaleMasterUpdateBinding binding;
    private CLT_VO clt_vo;

    private AddressModel addressData;
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
        binding = ActivityCustomerSaleMasterUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        clt_vo = (CLT_VO) intent.getExtras().get("cltVO");

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.addClientAddress.setOnClickListener(this::onClickAddClientAddress);
        binding.btnUpdate.setOnClickListener(this::onClickUpdate);
        binding.btnDelete.setOnClickListener(this::onClickDelete);
        


    }
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();

        binding.tvCLT07.setText(clt_vo.CLT_07);
        binding.tvCLT02.setText(clt_vo.CLT_02);
        binding.tvCLT06.setText(clt_vo.CLT_06);
        binding.tvCLT08.setText(UtilBox.phone(clt_vo.CLT_08));
        binding.tvCLT13.setText(UtilBox.phone(clt_vo.CLT_13));
        binding.tvClientPostalCode.setText(clt_vo.CLT_1001);
        binding.tvClientAddress.setText(clt_vo.CLT_1002);
        binding.etClientAddress.setText(clt_vo.CLT_1003);
        binding.tvCLT19.setText(clt_vo.CLT_19);
        binding.tvCLT20.setText(clt_vo.CLT_20);
        binding.tvCLT21.setText(clt_vo.CLT_21);


    }



    private void onClickAddClientAddress(View v) {
        Intent intent = new Intent(mContext, AddressActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, AddressActivity.REQUEST_CODE);
    }

    private void onClickUpdate(View view) {
        if (binding.tvCLT02.getText().toString().isEmpty()) {
            toast("성명을 입력 해주세요.");
        } else if (binding.tvClientPostalCode.getText().toString().isEmpty()) {
            toast("주소를 입력 해 주세요.");
        }else {


            requestSaveCLT("M_UPDATE");
        }
    }
    private void onClickDelete(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("발주상세정보 삭제");
        alertDialogBuilder.setMessage("삭제하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("삭제",
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestSaveCLT("M_DELETE");
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


    public void requestSaveCLT(String GUBUN) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }


        openLoadingBar();
        Http.clt(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).CLT_U2(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                GUBUN,
                mUser.Value.MEM_CID,
                clt_vo.CLT_01,
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
                        if(GUBUN=="M_UPDATE"){
                            toast("정보를 수정하였습니다.");
                        }else{
                            toast("정보를 삭제하였습니다.");
                        }
                        Intent intent = new Intent();
                        intent.putExtra("clt_vo", clt_vo);
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
    /**
     * 요청일자 클릭
     *
     * @param v
     */
    private void onClickReceiveDate(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
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

