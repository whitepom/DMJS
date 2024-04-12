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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityCustomerDetailUpdateBinding;
import kr.hmit.dmjs.databinding.ActivityOrderManagementInfoDetailBinding;
import kr.hmit.dmjs.model.response.RUN2_Model;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.model.vo.ODD_VO;
import kr.hmit.dmjs.model.vo.ODM_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.Cusomer.model.MultiItemVO;
import kr.hmit.dmjs.ui.UtilBox;
import kr.hmit.dmjs.ui.order_management.FindProductActivity;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerSaleDetailUpdateActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8123;
    public static final String OrderManagement_detail = "OrderManagementInfo_detail";

    private ActivityCustomerDetailUpdateBinding binding;

    private CLT_VO cltVO;
    private MultiItemVO run2VO;
    private ProductionInfoVO vo;
    private String[] btnValueList;
    private String[] mCategory;
    private String[] mCategoryCode;
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat;
    private String DAH_01;
    private String RUN_1702;
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
        binding = ActivityCustomerDetailUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        cltVO = (CLT_VO) intent.getExtras().get("cltVO");

        run2VO = (MultiItemVO) intent.getExtras().get("run2VO");
        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        DAH_01="";
        binding.imgBack.setOnClickListener(v -> finish());
        binding.addProductNum.setOnClickListener(this::onClickAddProduct);
        binding.tvRUN02.setOnClickListener(this::onClickDate);
        binding.btnUpdate.setOnClickListener(this::onClickUpdate);
        binding.btnDelete.setOnClickListener(this::onClickDelete);
        binding.tvRUN20.setOnClickListener(this::onClickParcel);
        binding.radioRUN1702.setOnCheckedChangeListener(this::onClickChangeRadioButton);

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
    private void onClickDate(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    private void onClickParcel(View v) {
        dropdownCategory();
    }
    @Override
    protected void initialize() {
        btnValueList=new String[]{run2VO.RUN_1702};
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();

        binding.tvRUN09.setText(run2VO.RUN_09);
        binding.tvRUN2201.setText(run2VO.RUN_2201);
        binding.tvClientPostalCode.setText(run2VO.RUN_18);
        binding.tvClientAddress.setText(run2VO.RUN_2203);

        binding.tvRUN2202.setText(run2VO.RUN_2202==null?"":UtilBox.phone(run2VO.RUN_2202));
        binding.tvRUN02.setText(run2VO.RUN_02==null?sdfFormat.format(mCalRequest.getTime()):UtilBox.datePatternChange(run2VO.RUN_2202));



        binding.tvRUN04.setText(run2VO.RUN_04);
        binding.tvRUN23.setText(run2VO.RUN_23);


        binding.tvRUN07.setText(run2VO.RUN_07);
        binding.tvRUN08.setText(run2VO.RUN_08);

        binding.tvRUN1701.setText(run2VO.RUN_1701);

        binding.tvRUN20.setText(run2VO.RUN_20);
        binding.tvRUN24.setText(run2VO.RUN_24);
        binding.tvRUN25.setText(run2VO.RUN_25);
        binding.tvRUN97.setText(run2VO.RUN_97);
    }

    private void onClickAddProduct(View v) {
        Intent intent = new Intent(mContext, FindProductActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, FindProductActivity.REQUEST_CODE);
    }
    private void onClickUpdate(View view) {
        if (binding.tvRUN09.getText().toString().isEmpty()) {
            toast("제품정보를 입력 해주세요.");
        } else if (binding.tvRUN2201.getText().toString().isEmpty()) {
            toast("수취인명을 입력 해주세요.");
        }else if (binding.tvRUN2201.getText().toString().isEmpty()) {
            toast("주소를 입력 해주세요.");
        }else if (binding.tvRUN07.getText().toString().isEmpty()) {
            toast("주문금액을 입력 해주세요.");
        }else {
            requestSaveRUN2("M_UPDATE");
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
                                requestSaveRUN2("M_DELETE");
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

    public void requestSaveRUN2(String GUBUN) {
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
                GUBUN,
                mUser.Value.MEM_CID,
                run2VO.RUN_01,
                binding.tvRUN02.getText().toString().replaceAll("-",""),
                cltVO.CLT_01,
                binding.tvRUN04.getText().toString(),
                Double.parseDouble(binding.tvRUN07.getText().toString()),
                binding.tvRUN08.getText().toString(),
                binding.tvRUN09.getText().toString(),
                binding.tvRUN1701.getText().toString(),
                RUN_1702,
                binding.tvClientPostalCode.getText().toString(),
                binding.tvRUN20.getText().toString(),
                cltVO.CLT_01,
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
                        if(GUBUN=="M_UPDATE"){
                            toast("고객주문 정보를 수정하였습니다.");
                        }else{
                            toast("고객주문 정보를 삭제하였습니다.");
                        }
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

    private void onClickChangeRadioButton(RadioGroup radioGroup, int i){

        if(i==binding.RUN1702Y.getId()){ // 직거래
            RUN_1702="Y";
        }
        else if(i==binding.RUN1702Y.getId()) { // 도매
            RUN_1702="N";
        }
        else { // 홈쇼핑
            RUN_1702="N";
        }
    }
}