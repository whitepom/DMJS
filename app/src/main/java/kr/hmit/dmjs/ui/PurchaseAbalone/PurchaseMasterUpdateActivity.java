
package kr.hmit.dmjs.ui.PurchaseAbalone;

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
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityPurchaseMainUpdateBinding;
import kr.hmit.dmjs.model.response.NGG_Model;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.model.vo.NGG_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.order_request.ClientNameListActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseMasterUpdateActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8024;
    public static final String OrderManagement_detail = "OrderManagement_detail";

    private ActivityPurchaseMainUpdateBinding binding;
    private CLT_VO clt_vo;
    private NGG_VO nggVO;

    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            binding.tvNGG02.setText(sdfFormat.format(mCalRequest.getTime()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPurchaseMainUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        nggVO = (NGG_VO) intent.getExtras().get("nggVO");

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.tvNGG02.setOnClickListener(this::onClickPurchaseDate);
        binding.addReceiverName.setOnClickListener(this::onClickAddClient);
        binding.btnSave.setOnClickListener(this::onClickUpdate);
        binding.btnDelete.setOnClickListener(this::onClickDelete);
        
        binding.tvNGG01.setText(nggVO.NGG_01);
        binding.tvCLT01.setText(nggVO.NGG_03);
        binding.tvCLT02.setText(nggVO.CLT_02);

        binding.tvNGG97.setText(nggVO.NGG_97);

    }
    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
    }


    private void onClickAddClient(View v) {
        Intent intent = new Intent(mContext, ClientNameListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, ClientNameListActivity.REQUEST_CODE);
    }

    private void onClickUpdate(View view) {
        if (binding.tvNGG02.getText().toString().isEmpty()) {
            toast("수매일자를 선택해주세요.");
        } else if (binding.tvCLT01.getText().toString().isEmpty()) {
            toast("수매처를 선택해주세요.");
        }else {
            nggVO.NGG_02=binding.tvNGG02.getText().toString().replaceAll("-","");
            nggVO.NGG_03=binding.tvCLT01.getText().toString();
            nggVO.NGG_97=binding.tvNGG97.getText().toString();
            nggVO.CLT_02=binding.tvCLT02.getText().toString();

            requestSaveNGG("M_UPDATE");
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
                                requestSaveNGG("M_DELETE");
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

    public void requestSaveNGG(String GUBUN) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();
        Http.ngg(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).NGG_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_MASTER_UPDATE",
                mUser.Value.MEM_CID,
                nggVO.NGG_01,
                nggVO.NGG_02,
                nggVO.NGG_03,
                "",
               0,
                0,
                0,
                0,
                0,
                0,"",0,"","","","",
                nggVO.NGG_97,
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
                        if(GUBUN=="M_UPDATE"){
                            toast("발주상세정보를 수정하였습니다.");
                        }else{
                            toast("발주상세정보를 삭제하였습니다.");
                        }
                        Intent intent = new Intent();
                        intent.putExtra("nggVO", nggVO);
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
    /**
     * 요청일자 클릭
     *
     * @param v
     */
    private void onClickPurchaseDate(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ClientNameListActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            clt_vo=(CLT_VO) data.getSerializableExtra(ClientNameListActivity.CLIENT_LIST);
            binding.tvCLT01.setText(clt_vo.CLT_01);
            binding.tvCLT02.setText(clt_vo.CLT_02);

        }
    }
}

