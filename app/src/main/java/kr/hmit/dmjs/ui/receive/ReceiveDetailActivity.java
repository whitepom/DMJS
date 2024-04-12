package kr.hmit.dmjs.ui.receive;

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
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.base.settings.SettingsKey;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityReceiveDetailBinding;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.dmjs.model.response.WHC_Model;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.model.vo.GEM_VO;
import kr.hmit.dmjs.model.vo.REM_VO;
import kr.hmit.dmjs.model.vo.WHC_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.login.LoginActivity;
import kr.hmit.dmjs.ui.order_management.FindProductActivity;
import kr.hmit.dmjs.ui.order_request.ClientNameListActivity;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiveDetailActivity extends BaseActivity {
    public static final int REQUEST_CODE = 77;
    private Calendar mCalRequest;
    private Calendar mCalRequest2;
    private ActivityReceiveDetailBinding binding;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog datePickerDialog2;
    private GEM_VO gemVO;
    private REM_VO remVO;
    private CLT_VO cltVO;
    private String[] WHC_01;
    private String[] WHC_02;
    private String[] mCategory;
    private ProductionInfoVO dahVO;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);


            binding.tvReceiveDate.setText(sdfFormat.format(mCalRequest.getTime()));
            binding.tvProductDate.setText(sdfFormat.format(mCalRequest2.getTime()));
        }
    };
    private DatePickerDialog.OnDateSetListener datePickerListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest2.set(Calendar.YEAR, year);
            mCalRequest2.set(Calendar.MONTH, month);
            mCalRequest2.set(Calendar.DAY_OF_MONTH, dayOfMonth);


             binding.tvProductDate.setText(sdfFormat.format(mCalRequest2.getTime()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReceiveDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        gemVO = (GEM_VO) intent.getExtras().get("gemVO");
        initLayout();
        initialize();


    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.btnUpdate.setOnClickListener(this::onClickUpdate);
        binding.btnDelete.setOnClickListener(this::onClickDelete);
        binding.addProduct.setOnClickListener(this::onClickAddProduct);
        binding.addReceiverName.setOnClickListener(this::onClickAddCLT);
        binding.addSightTitle.setOnClickListener(this::onClickAddWHC);
        binding.tvReceiveDate.setOnClickListener(this::onClickDate);
        binding.tvReceiveDate.setText(datePatternChange(gemVO.GEM_02));
        binding.tvProductDate.setOnClickListener(this::onClickDate2);
        if(gemVO.GEM_2101.equals(""))
        {binding.tvWarehouse.setText("");}
        else {binding.tvWarehouse.setText(gemVO.WHC_03+" - "+gemVO.GEM_2101+" - "+gemVO.GEM_2102);}
        binding.tvReceiverName.setText(gemVO.CLT_02);
        binding.tvReceiveNum.setText((gemVO.GEM_01));
        binding.tvProductName.setText(gemVO.GEM_04_NM);
        binding.tvProductNum.setText(gemVO.GEM_04);
        binding.tvProductDate.setText(datePatternChange(gemVO.GEM_10));
        binding.tvMgr.setText(gemVO.GEM_08);
        binding.tvUnitPrice.setText(String.valueOf(gemVO.GEM_05));
        binding.tvCount.setText(changeToMoney(String.valueOf(gemVO.GEM_06)));
        binding.tvPrice.setText(changeToMoney(gemVO.GEM_07));
        binding.tvBox.setText(changeToMoney(gemVO.GEM_20));
        binding.tvNote.setText(gemVO.GEM_97);
        binding.tvCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int tvUnitPrice = Integer.parseInt(binding.tvUnitPrice.getText().toString().equals("")?"0":removeComma(binding.tvUnitPrice.getText().toString()));
                int tvCount = Integer.parseInt(binding.tvCount.getText().toString().equals("")?"0":removeComma(binding.tvCount.getText().toString()));
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
                int tvUnitPrice = Integer.parseInt(binding.tvUnitPrice.getText().toString().equals("")?"0":removeComma(binding.tvUnitPrice.getText().toString()));
                int tvCount = Integer.parseInt(binding.tvCount.getText().toString().equals("")?"0":removeComma(binding.tvCount.getText().toString()));
                binding.tvPrice.setText("" + tvCount*tvUnitPrice);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    protected void initialize() {
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
        mCalRequest2=Calendar.getInstance();
        requestWHC_Read();
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
    public static String moneyFormatToEA(String inputMoney) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        return decimalFormat.format(inputMoney);
    }

    public static String moneyFormatToWon2(String inputMoney) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        return decimalFormat.format(inputMoney);
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

    private void onClickAddWHC(View v) {// 수주 임시

        if (mCategory != null && mCategory.length == 1) {
            requestWHC_Read();
        } else {
            dropdownCategory("창고위치",mCategory,binding.tvWarehouse);
        }

    }

    private void dropdownCategory(String dialogTitle, String[] category, TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(dialogTitle)
                .setItems(category, (dialog, which) -> {
                    textView.setText(category[which].contains("선택안함")?"":category[which]+" - "+WHC_01[which]+" - "+WHC_02[which]);
                    gemVO.GEM_2101=WHC_01[which];
                    gemVO.GEM_2102=WHC_02[which];

                }).setCancelable(false).create();

        builder.show();
    }

    private void onClickDate(View v) {

        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void onClickDate2(View v) {

        datePickerDialog2 = new DatePickerDialog(mContext, datePickerListener2, mCalRequest2.get(Calendar.YEAR),
                mCalRequest2.get(Calendar.MONTH), mCalRequest2.get(Calendar.DAY_OF_MONTH));
        datePickerDialog2.show();
    }


    private void onClickUpdate(View view) {
        if (binding.tvProductName.getText().toString().isEmpty()) {
            toast("제품정보를 선택해주세요.");
        } else if (binding.tvReceiveDate.getText().toString().isEmpty()) {
            toast("납기일자를 선택해주세요.");
        }else {
            requestSaveGEM("M_UPDATE");
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
                                requestSaveGEM("M_DELETE");
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
    private void requestWHC_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.odd(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).WHC_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "LIST_M",
                "DMJS"
        ).enqueue(new Callback<WHC_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<WHC_Model> call, Response<WHC_Model> response) {
                Message msg = new Message();
                msg.obj = response;
                msg.what = 100;

                //=====================
                // response callback
                //=====================
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 100) {
                            closeLoadingBar();

                            Response<WHC_Model> response = (Response<WHC_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if (response.body().Data.get(0).Validation) {
                                        bindingCategory(response.body().Data);
                                    } else {
                                        toast("동시접속 > 로그인 페이지로 이동합니다");  //
                                        mSettings.putBooleanItem(SettingsKey.AutoLogin,false);
                                        Intent intent = new Intent(mContext, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("simultaneous_connect",mUser.Value.MEM_01.toString());
                                        startActivity(intent);
                                        finish();
                                        // BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                }else{
                                    bindingCategory(new ArrayList<WHC_VO>());
                                    toast("검색결과가 없습니다.");
                                }
                            } else {
                                BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<WHC_Model> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
            }
        });
    }
    private void bindingCategory(ArrayList<WHC_VO> data) {
        mCategory = new String[data.size() + 1];
        WHC_01=new String[data.size()+1];
        WHC_02=new String[data.size()+1];
        mCategory[0] = "선택안함";
        WHC_01[0]="";
        WHC_02[0]="";
        for (int i = 0; i < data.size(); i++) {
            mCategory[i + 1] = data.get(i).WHC_03;
            WHC_01[i+1]=data.get(i).WHC_01;
            WHC_02[i+1]=data.get(i).WHC_02;
        }


}
    public void requestSaveGEM(String GUBUN) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();
        Http.gem(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).GEM_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                GUBUN,
                mUser.Value.MEM_CID,
                gemVO.GEM_01,
                binding.tvReceiveDate.getText().toString().replaceAll("-",""),  // 입고일
                gemVO.GEM_03, //입고처
                gemVO.GEM_04,//품목명 바인딩안됨
                Integer.parseInt(removeComma(binding.tvUnitPrice.getText().toString())), // 단가
                Integer.parseInt(removeComma(binding.tvCount.getText().toString())), // 수량
                Integer.parseInt(removeComma(binding.tvPrice.getText().toString())), // 금액
                binding.tvMgr.getText().toString(),
                binding.tvProductDate.getText().toString().replaceAll("-",""),  // 제조일
                Integer.parseInt(removeComma(binding.tvBox.getText().toString())), //박스수량
                gemVO.GEM_2101,
                gemVO.GEM_2102,
                gemVO.GEM_1501,
                gemVO.GEM_1502,
                binding.tvNote.getText().toString(), // 비고
                mUser.Value.MEM_01 // 최종수정자
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
                            toast("입고정보를 수정하였습니다.");
                        }else{
                            toast("입고정보를 삭제하였습니다.");
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

    public String removeComma(String data){

        return data.replaceAll("\\,","");
    }


    public String returnREM_03(String str){
        if(str=="") return "현장명 없음";
        return str;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ClientNameListActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            cltVO=(CLT_VO) data.getSerializableExtra(ClientNameListActivity.CLIENT_LIST);
            binding.tvReceiverName.setText(cltVO.CLT_02);
            gemVO.GEM_03 = cltVO.CLT_01;
        }
        else if (requestCode == FindProductActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            dahVO=(ProductionInfoVO) data.getSerializableExtra(FindProductActivity.PRODUCT_LIST);
            binding.tvProductName.setText(dahVO.DAH_02);
            binding.tvProductNum.setText(dahVO.DAH_01);
            gemVO.GEM_04=dahVO.DAH_01;
            binding.tvUnitPrice.setText(dahVO.DAH_07); // 단가 몰라서 일단 dAH_07로 해놓음
        }
    }

    private String changeToMoney(String str){

        String amount = str;
        amount = amount.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
        return amount;

    }

}