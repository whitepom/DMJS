package kr.hmit.dmjs.ui.Cusomer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityCustomerSaleDetailAddBinding;
import kr.hmit.dmjs.model.response.RUN2_Model;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.model.vo.ODM_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.Cusomer.model.MultiItemVO;
import kr.hmit.dmjs.ui.order_management.FindProductActivity;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerSaleDetailAddActivity extends BaseActivity{

    public static final int REQUEST_CODE = 8124;
    public static final String OrderManagementInfo_ADD = "OrderManagementInfo_ADD";

    private ActivityCustomerSaleDetailAddBinding binding;
    private ProductionInfoVO vo;
    private CLT_VO cltVO;
    private MultiItemVO run2VO;

    private String[] btnValueList;
    private String[] mCategory;
    private String[] mCategoryCode;
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat;
    private String DAH_01;
    private String RUN_20;

    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            binding.tvRUN02.setText(sdfFormat.format(mCalRequest.getTime()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerSaleDetailAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        cltVO = (CLT_VO) intent.getExtras().get("cltVO");

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        DAH_01="";

        binding.imgBack.setOnClickListener(v -> finish());
        binding.addProductNum.setOnClickListener(this::onClickAddProduct);
        binding.tvRUN02.setOnClickListener(this::onClickDate);
        binding.btnSave.setOnClickListener(this::onClickSave);
        binding.tvRUN20.setOnClickListener(this::onClickParcel);
        binding.radioRUN1702.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==binding.RUN1702Y.getId()){
                    btnValueList[0]="Y";
                }else if(checkedId==binding.RUN1702N.getId()){
                    btnValueList[0]="N";
                }
            }
        });

    }
    private void bindingCategory() {

        mCategory = new String[2];
        mCategoryCode =  new String[2];
        mCategory[0]=   "우체국택배";
        mCategoryCode[0] ="우체국택배";
        mCategory[1]=   "로젠택배";
        mCategoryCode[1] ="로젠택배";


    }
    private void dropdownCategory() {

        bindingCategory();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle("택배사")
                .setItems(mCategory, (dialog, which) -> {
                    binding.tvRUN20.setText(mCategory[which]);
                    RUN_20=mCategoryCode[which];
                }).setCancelable(false).create();

        builder.show();
    }
    private void onClickParcel(View v) {
        dropdownCategory();
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
        binding.tvRUN02.setText(sdfFormat.format(mCalRequest.getTime()));
        btnValueList=new String[]{"N"};
    }

    private void onClickAddProduct(View v) {
        Intent intent = new Intent(mContext, FindProductActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, FindProductActivity.REQUEST_CODE);
    }
    private void onClickSave(View view) {
        if (binding.tvRUN09.getText().toString().isEmpty()) {
            toast("제품정보를 입력 해주세요.");
        } else if (binding.tvRUN2201.getText().toString().isEmpty()) {
            toast("수취인명을 입력 해주세요.");
        }else if (binding.tvRUN2201.getText().toString().isEmpty()) {
            toast("주소를 입력 해주세요.");
        }else if (binding.tvRUN07.getText().toString().isEmpty()) {
            toast("주문금액을 입력 해주세요.");
        }else {
            requestSaveRUN2();
        }
    }
    public void requestSaveRUN2() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();
        Http.run2(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).RUN2_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_INSERT",
                mUser.Value.MEM_CID,
                "",
                binding.tvRUN02.getText().toString().replaceAll("-",""),
                cltVO.CLT_01,
                binding.tvRUN04.getText().toString(),
                Double.parseDouble(binding.tvRUN07.getText().toString()),
                binding.tvRUN08.getText().toString(),
                binding.tvRUN09.getText().toString(),
                binding.tvRUN1701.getText().toString(),
                btnValueList[0].toString(),
                binding.tvClientPostalCode.getText().toString(),
                binding.tvRUN20.getText().toString(),
                cltVO.CLT_02,
                binding.tvRUN2201.getText().toString(),
                binding.tvRUN2202.getText().toString().replaceAll("-",""),
                binding.tvClientAddress.getText().toString()+" "+binding.etClientAddress.getText().toString(),
                binding.tvRUN23.getText().toString(),
                binding.tvRUN24.getText().toString(),
                binding.tvRUN25.getText().toString(),
                DAH_01,
                mUser.Value.MEM_01
        ).enqueue(new Callback<RUN2_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<RUN2_Model> call, Response<RUN2_Model> response) {
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
                        toast("고객 주문정보를 생성하였습니다.");
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }.sendMessage(msg);
            }


            @Override
            public void onFailure(Call<RUN2_Model> call, Throwable t) {
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
            binding.tvRUN09.setText(vo.DAH_02);
            DAH_01=vo.DAH_01;

        }
    }
}
