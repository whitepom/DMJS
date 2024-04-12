
package kr.hmit.dmjs.ui.order_management;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityOrderManagementAddBinding;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.order_request.ClientNameListActivity;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderManagementAddActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8023;
    public static final String OrderManagement_ADD = "OrderManagement_ADD";

    private ActivityOrderManagementAddBinding binding;
    private CLT_VO clt_vo;


    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            binding.tvReceiveDate.setText(sdfFormat.format(mCalRequest.getTime()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderManagementAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.tvReceiveDate.setOnClickListener(this::onClickReceiveDate);
        binding.addReceiverName.setOnClickListener(this::onClickAddClient);
        binding.btnSave.setOnClickListener(this::onClickSave);

    }
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
        binding.tvReceiveDate.setText(sdfFormat.format(mCalRequest.getTime()));
    }


    private void onClickAddClient(View v) {
        Intent intent = new Intent(mContext, ClientNameListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, ClientNameListActivity.REQUEST_CODE);
    }

    private void onClickSave(View view) {
        if (binding.tvReceiveDate.getText().toString().isEmpty()) {
            toast("발주일자를 선택해주세요.");
        } else if (binding.tvReceiverNum.getText().toString().isEmpty()) {
            toast("입고처를 선택해주세요.");
        }else {
            requestSaveODM();
        }
    }

    public void requestSaveODM() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();
        Http.odm(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).ODM_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_INSERT",
                mUser.Value.MEM_CID,
                "",
                binding.tvReceiveDate.getText().toString().replaceAll("-",""),
                clt_vo.CLT_01,
                "",
                "",
                "",
                "",
                binding.tvNote.getText().toString(),
                "",
                ""

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
                        toast("발주처를 추가하였습니다.");
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
        if (requestCode == ClientNameListActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            clt_vo=(CLT_VO) data.getSerializableExtra(ClientNameListActivity.CLIENT_LIST);
            binding.tvReceiverNum.setText(clt_vo.CLT_01);
            binding.tvReceiveTitle.setText(clt_vo.CLT_02);
        }
    }
}

