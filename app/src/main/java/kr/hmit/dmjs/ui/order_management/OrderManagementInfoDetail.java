package kr.hmit.dmjs.ui.order_management;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityOrderManagementInfoDetailBinding;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.model.vo.ODD_VO;
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

public class OrderManagementInfoDetail extends BaseActivity {
    public static final int REQUEST_CODE = 8123;
    public static final String OrderManagement_detail = "OrderManagementInfo_detail";

    private ActivityOrderManagementInfoDetailBinding binding;
    private CLT_VO clt_vo;
    private ODM_VO odmVO;
    private ODD_VO oddVO;
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
        binding = ActivityOrderManagementInfoDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        odmVO = (ODM_VO) intent.getExtras().get("odmVO");
        oddVO = (ODD_VO) intent.getExtras().get("oddVO");
        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.addProduct.setOnClickListener(this::onClickAddProduct);
        binding.tvDate.setOnClickListener(this::onClickDate);
        binding.btnUpdate.setOnClickListener(this::onClickUpdate);
        binding.btnDelete.setOnClickListener(this::onClickDelete);

        binding.tvProductName.setText(oddVO.DAH_02);
        binding.tvProductNum.setText(oddVO.ODD_03);
        binding.tvUnitPrice.setText(String.valueOf(oddVO.ODD_04));
        binding.tvCount.setText(String.valueOf(oddVO.ODD_05));
        binding.tvPrice.setText(String.valueOf(oddVO.ODD_06));
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

        binding.tvDate.setText(datePatternChange(oddVO.ODD_10));
        binding.tvNote.setText(oddVO.ODD_97);

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

    private void onClickDate(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    protected void initialize() {
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
    }

    private void onClickAddProduct(View v) {
        Intent intent = new Intent(mContext, FindProductActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, FindProductActivity.REQUEST_CODE);
    }
    private void onClickUpdate(View view) {
        if (binding.tvProductName.getText().toString().isEmpty()) {
            toast("제품정보를 선택해주세요.");
        } else if (binding.tvDate.getText().toString().isEmpty()) {
            toast("납기일자를 선택해주세요.");
        }else {
            requestSaveODD("M_UPDATE");
        }
    }

    private void onClickDelete(View v) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("발주상세정보 삭제");
        alertDialogBuilder.setMessage("삭제하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("삭제",
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestSaveODD("M_DELETE");
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

    public void requestSaveODD(String GUBUN) {
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
                GUBUN,
                mUser.Value.MEM_CID,
                odmVO.ODM_01,
                oddVO.ODD_02,
                binding.tvProductNum.getText().toString(),
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
                        if(GUBUN=="M_UPDATE"){
                            toast("발주상세정보를 수정하였습니다.");
                        }else{
                            toast("발주상세정보를 삭제하였습니다.");
                        }
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