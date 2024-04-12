package kr.hmit.dmjs.ui.release;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityOrderManagementInfoAddBinding;
import kr.hmit.dmjs.databinding.ActivityReleaseInfoAdd2Binding;
import kr.hmit.dmjs.databinding.ActivityReleaseInfoAddBinding;
import kr.hmit.dmjs.model.response.WKS_Model;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.model.vo.RUN_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.UtilBox;
import kr.hmit.dmjs.ui.find_employee.FindEmployeeActivity;
import kr.hmit.dmjs.ui.order_management.FindProductActivity;
import kr.hmit.dmjs.ui.order_management.OrderManagementMainActivity;
import kr.hmit.dmjs.ui.order_management.adapter.OrderManagementListAdapter;
import kr.hmit.dmjs.ui.order_request.ClientNameListActivity;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import kr.hmit.dmjs.ui.work_management.AddWorkActivity;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ReleaseInfoAdd extends BaseActivity{
    public static final int REQUEST_CODE = 8124;
    public static final String OrderManagementInfo_ADD = "OrderManagementInfo_ADD";
    private RUN_VO runVO;
    private String box;
    private String RUN13;
    private String money;
    private String tvCount2;
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private ActivityReleaseInfoAdd2Binding binding;
    private ProductionInfoVO vo;
    private String DAHExpireDate;
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
        binding = ActivityReleaseInfoAdd2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        runVO = (RUN_VO) intent.getExtras().get("runVO");

        RUN13="D";
        initLayout();
        initialize();
    }


    @Override
    protected void initLayout() {

        binding.imgBack.setOnClickListener(v -> finish());
        binding.addProduct.setOnClickListener(this::onClickAddProduct);
        binding.tvDate.setOnClickListener(this::onClickDate);
        binding.radioGroup2.setOnCheckedChangeListener(this::onClickChangeRadioButton);
        binding.btnSave.setOnClickListener(this::onClickSave);
        binding.tvCount.setText("0");
        binding.tvPrice.setText("0");

        binding.tvUnitPrice.setText("0");


        binding.tvCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int tvUnitPrice = Integer.parseInt(binding.tvUnitPrice.getText().toString().equals("")?"0":removeComma(binding.tvUnitPrice.getText().toString()));
                int tvCount = Integer.parseInt(binding.tvCount.getText().toString().equals("")?"0":removeComma(binding.tvCount.getText().toString()));
                binding.tvPrice.setText(changeToMoney(((long)tvCount*tvUnitPrice)+""));

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
                int tvUnitPrice = Integer.parseInt(binding.tvUnitPrice.getText().toString().equals("")?"0":removeComma(binding.tvUnitPrice.getText().toString()));
                int tvCount = Integer.parseInt(binding.tvCount.getText().toString().equals("")?"0":removeComma(binding.tvCount.getText().toString()));
                binding.tvPrice.setText(changeToMoney(((long)tvCount*tvUnitPrice)+""));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    public String removeComma(String data){

        return data.replaceAll("\\,","");
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

        binding.RUN13D.setChecked(true); // default - 직거래 default
        binding.tvCLT02.setText(runVO.CLT_02);
        binding.tvCLT01.setText(runVO.CLT_01);

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

    private void onClickAddProduct(View v) {
        Intent intent = new Intent(mContext, FindProductActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, FindProductActivity.REQUEST_CODE);
    }
    private void onClickSave(View view) {
        if (binding.tvProductName.getText().toString().isEmpty()) {
            toast("제품정보를 선택해주세요.");
        } else if (binding.tvDate.getText().toString().isEmpty()) {
            toast("출고일자를 선택해주세요.");
        }else if (binding.tvCount.getText().toString().isEmpty()) {
            toast("수량을 입력해주세요.");
        }else if (binding.tvPrice.getText().toString().isEmpty()) {
            toast("금익은 0원 이상이어야 합니다.");
        }else {
            requestSaveRUN();
        }
    }
    public void requestSaveRUN() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();
        Http.run(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).RUN_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_INSERT",
                mUser.Value.MEM_CID,
                "", //신규
                binding.tvDate.getText().toString().replaceAll("-",""),
                runVO.CLT_01,
                binding.tvProductNum.getText().toString(), //품번
                Double.parseDouble(removeComma(binding.tvUnitPrice.getText().toString())),
                Double.parseDouble(removeComma(binding.tvCount.getText().toString())),
                Double.parseDouble(removeComma(binding.tvPrice.getText().toString())),
                RUN13, // 거래구분
                "",
                "",
                "",
                binding.tvRUN97.getText().toString(),
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
                        toast("출고상세정보를 신규 생성하였습니다.");
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
            vo = (ProductionInfoVO) data.getSerializableExtra(FindProductActivity.PRODUCT_LIST);
            binding.tvProductName.setText(vo.DAH_02);
            binding.tvProductNum.setText(vo.DAH_01);
            binding.tvUnitPrice.setText(changeToMoney(vo.DAH_11));
            binding.tvDAH04.setText(vo.DAH_04);
        }
    }

    private void onClickChangeRadioButton(RadioGroup radioGroup, int i){

        if(i==binding.RUN13D.getId()){ // 직거래
            RUN13="D";
        }
        else if(i==binding.RUN13W.getId()) { // 도매
            RUN13="W";
        }
        else if(i==binding.RUN13H.getId()){ // 홈쇼핑
            RUN13="H";
        }
    }


    private String changeToMoney(String str){

        String amount = str;
        amount = amount.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
        return amount;

    }
}
