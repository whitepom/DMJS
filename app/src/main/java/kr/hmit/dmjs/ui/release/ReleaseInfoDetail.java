package kr.hmit.dmjs.ui.release;

import kr.hmit.base.base_activity.BaseActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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

import kr.hmit.dmjs.databinding.ActivityReleaseInfoDetailBinding;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.model.vo.RUN_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.find_employee.FindEmployeeActivity;
import kr.hmit.dmjs.ui.order_management.FindProductActivity;
import kr.hmit.dmjs.ui.order_request.ClientNameListActivity;
import kr.hmit.dmjs.ui.release.ReleaseCurrentActivity;
import kr.hmit.dmjs.ui.release.adapter.ReleaseCurrentListAdapter;
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
public class ReleaseInfoDetail extends BaseActivity{
    public static final int REQUEST_CODE = 8123;
    public static final String OrderManagement_detail = "OrderManagementInfo_detail";

    private ActivityReleaseInfoDetailBinding binding;
    private CLT_VO clt_vo;
    private RUN_VO runVO;
    private String deposit;
    private String RUN13;
    private ProductionInfoVO vo;
    private String DAHExpireDate;
    private String box;
    private String money;
    private Calendar mCalRequest;
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
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
        binding = ActivityReleaseInfoDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        deposit="N";
        DAHExpireDate="";
        Intent intent = getIntent();
        runVO = (RUN_VO) intent.getExtras().get("runVO");
        initLayout();
        initialize();
    }
    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.addProduct.setOnClickListener(this::onClickAddProduct);
        binding.tvDate.setText(runVO.RUN_02);
        binding.tvDate.setOnClickListener(this::onClickDate);
        binding.tvCLT02.setClickable(false);
        binding.btnUpdate.setOnClickListener(this::onClickUpdate);
        binding.btnDelete.setOnClickListener(this::onClickDelete);
        binding.radioGroup2.setOnCheckedChangeListener(this::onClickChangeRadioButton);
        binding.tvCLT02.setText(runVO.CLT_02);
        binding.tvRUN97.setText(runVO.RUN_97);
        binding.tvDAH04.setText(runVO.DAH_04);
        binding.tvProductName.setText(runVO.DAH_02);
        binding.tvProductNum.setText(runVO.DAH_01);
        binding.tvUnitPrice.setText(changeToMoney(runVO.RUN_05)); // 단가
        binding.tvCount.setText(changeToMoney(runVO.RUN_06)); // 수량
        binding.tvPrice.setText(changeToMoney(runVO.RUN_07)); // 금액
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
    private void onClickDate(View v) {
          datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                  mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
          datePickerDialog.show();
      }
    @Override
    protected void initialize() {
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
        switch(runVO.RUN_13){
            case "D" : default:
                binding.RUN13D.setChecked(true); //직거래
                break;
            case "W" :
                binding.RUN13W.setChecked(true); // 도매
                break;
            case "H" :
                binding.RUN13H.setChecked(true); //홈쇼핑
                break;

        }
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
    public String removeComma(String data){

        return data.replaceAll("\\,","");
    }


    private void onClickAddProduct(View v) {
        Intent intent = new Intent(mContext, FindProductActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, FindProductActivity.REQUEST_CODE);
    }

    private void onClickAddCLT(View v) {
        Intent intent = new Intent(mContext, ClientNameListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, ClientNameListActivity.REQUEST_CODE);


    }
    private void onClickUpdate(View view) {
        if (binding.tvProductName.getText().toString().isEmpty()) {
            toast("제품정보를 선택해주세요.");
        }else if (binding.tvCount.getText().toString().isEmpty()) {
            toast("수량을 입력 해주세요.");
        }else if (binding.tvDate.getText().toString().isEmpty()) {
            toast("출고일을 입력 해주세요.");
        }else if (binding.tvPrice.getText().toString().isEmpty()) {
            toast("공급가액은 0원 이상이어야 합니다.");
        }else {
            requestSaveRUN("M_UPDATE");
        }
    }

    private void onClickDelete(View v) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("출고상세정보 삭제");
        alertDialogBuilder.setMessage("삭제하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("삭제",
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestSaveRUN("M_DELETE");
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
    private String changeToMoney(String str){

        String amount = str;
        amount = amount.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
        return amount;

    }
    public void requestSaveRUN(String GUBUN) {
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
                GUBUN,
                mUser.Value.MEM_CID,
                runVO.RUN_01,
                binding.tvDate.getText().toString().replaceAll("-",""),
                runVO.CLT_01, // 주문처
                binding.tvProductNum.getText().toString(),
                Double.parseDouble(removeComma(binding.tvUnitPrice.getText().toString())),
                Double.parseDouble(removeComma(binding.tvCount.getText().toString())),
                Double.parseDouble(removeComma(binding.tvPrice.getText().toString())),
                RUN13, // 거래구분 (도매, 직거래, 홈쇼핑)
                "", //
                binding.tvPay.getText().toString(), //
                "",
                binding.tvRUN97.getText().toString(), // 비고
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
                            toast("출고상세정보를 수정하였습니다.");
                        }else{
                            toast("출고상세정보를 삭제하였습니다.");
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
            binding.tvUnitPrice.setText(changeToMoney(vo.DAH_11));
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
}
