package kr.hmit.dmjs.ui.receive;

import android.app.AlertDialog;
import android.app.DatePickerDialog;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.base.settings.SettingsKey;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityReceiveAddBinding;
import kr.hmit.dmjs.model.response.WHC_Model;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.model.vo.GEM_VO;
import kr.hmit.dmjs.model.vo.REM_VO;
import kr.hmit.dmjs.model.vo.WHC_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.login.LoginActivity;
import kr.hmit.dmjs.ui.order_request.ClientNameListActivity;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiveAddActivity extends BaseActivity {
    // public static final int REQUEST_CODE = 8124;
    public static final int REQUEST_CODE = 2929;
    public static final String ReceiveCurrent_ADD = "ReceiveCurrent_ADD";
    private GEM_VO gemVo;
    private ProductionInfoVO dahVO;
    private CLT_VO cltVO;
    private REM_VO remVO;
    private String[] WHC_01;
    private String[] WHC_02;
    private String[] mCategory;
    private String box;
    // private ODM_VO odmVO;
    private ActivityReceiveAddBinding binding;
    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private String money;
    private Calendar mCalRequest;
    private Calendar mCalRequest2;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog datePickerDialog2;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            binding.tvDate.setText(sdfFormat.format(mCalRequest.getTime()));
         //   binding.tvProductDate.setText(sdfFormat.format(mCalRequest.getTime()));
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
        binding = ActivityReceiveAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.addProduct.setOnClickListener(this::onClickAddProduct);
        binding.addReceiverName.setOnClickListener(this::onClickAddClient);
        binding.addWHC.setOnClickListener(this::onClickAddWHC);
        binding.tvDate.setOnClickListener(this::onClickDate);
        binding.tvProductDate.setOnClickListener(this::onClickDate2);
        binding.btnSave.setOnClickListener(this::onClickSave);
        binding.tvCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
              //  int tvUnitPrice = Integer.parseInt(binding.tvUnitPrice.getText().toString().equals("") ? "0" : binding.tvUnitPrice.getText().toString());
              //  int tvCount = Integer.parseInt(binding.tvCount.getText().toString().equals("") ? "0" : binding.tvCount.getText().toString());
             //   binding.tvPrice.setText("" + tvCount * tvUnitPrice);
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
          //      int tvUnitPrice = Integer.parseInt(binding.tvUnitPrice.getText().toString().equals("") ? "0" : binding.tvUnitPrice.getText().toString());
           //     int tvCount = Integer.parseInt(binding.tvCount.getText().toString().equals("") ? "0" : binding.tvCount.getText().toString());
            //    binding.tvPrice.setText("" + tvCount * tvUnitPrice);
                int tvUnitPrice = Integer.parseInt(binding.tvUnitPrice.getText().toString().equals("")?"0":removeComma(binding.tvUnitPrice.getText().toString()));
                int tvCount = Integer.parseInt(binding.tvCount.getText().toString().equals("")?"0":removeComma(binding.tvCount.getText().toString()));
                binding.tvPrice.setText(changeToMoney(((long)tvCount*tvUnitPrice)+""));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.tvBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(charSequence.toString()) && !charSequence.toString().equals(box)){
                    box = decimalFormat.format(Double.parseDouble(charSequence.toString().replaceAll(",","")));
                    binding.tvBox.setText(box);
                    binding.tvBox.setSelection(box.length());
                }
                else if(TextUtils.isEmpty(charSequence.toString())||charSequence.toString().equals("")){
                    binding.tvBox.setText("0");
                    binding.tvBox.setSelection(box.length());}
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
    private void onClickDate2(View v) {

        datePickerDialog2 = new DatePickerDialog(mContext, datePickerListener2, mCalRequest2.get(Calendar.YEAR),
                mCalRequest2.get(Calendar.MONTH), mCalRequest2.get(Calendar.DAY_OF_MONTH));
        datePickerDialog2.show();
    }
    @Override
    protected void initialize() {
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
        mCalRequest2=Calendar.getInstance();
        binding.tvDate.setText(sdfFormat.format(mCalRequest.getTime()));
        binding.tvProductDate.setText(sdfFormat.format(mCalRequest2.getTime()));
      //  binding.tvProductDate.setText(sdfFormat.format(mCalRequest.getTime()));
        requestWHC_Read();
    }

    private void onClickSave(View view) {
        if (binding.tvProductName.getText().toString().isEmpty()) {
            toast("제품정보를 입력해주세요.");
        } else if (binding.tvDate.getText().toString().isEmpty()) {
            toast("입고일자를 입력해주세요.");
        } else if (binding.tvReceiverName.getText().toString().isEmpty()) {
            toast("입고처를 입력해주세요.");
        } // 현장명 잠시 제외
        else if (binding.tvProductDate.getText().toString().isEmpty()) {
            toast("제조일자를 입력해주세요.");
        } else if (binding.tvProductManager.getText().toString().isEmpty()) {
            toast("생산자를 입력해주세요.");
        } else if (binding.tvCount.getText().toString().isEmpty()) {
            toast("수량을 입력해주세요.");
        } else {
            requestSaveGEM();
        }
    }
    private String changeToMoney(String str){

        String amount = str;
        amount = amount.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
        return amount;

    }
    public void requestSaveGEM() {
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

                "M_INSERT",
                mUser.Value.MEM_CID,
                "",                                     //입고번호 자동생성
                binding.tvDate.getText().toString().replaceAll("-", ""),  // 입고일자
                binding.tvReceiveNumber.getText().toString(), //입고처
                binding.tvProductNum.getText().toString(),//품목명 바인딩안됨
                Integer.parseInt(removeComma(binding.tvUnitPrice.getText().toString())), // 단가
                Integer.parseInt(removeComma(binding.tvCount.getText().toString())), // 수량
                Integer.parseInt(removeComma(binding.tvPrice.getText().toString())), // 금액
                binding.tvProductManager.getText().toString(), //생산자
                binding.tvProductDate.getText().toString().replaceAll("-", ""),  // 제조일자
                Integer.parseInt(removeComma(binding.tvBox.getText().toString())), //박스수량
                binding.tvWHC01.getText().toString(), // 창고위치1
                binding.tvWHC02.getText().toString(), // 창고위치2
                "",
                0,
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
                        toast("입고정보를 생성하였습니다.");
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
        // 제품번호 추가
        if (requestCode == ClientNameListActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            cltVO = (CLT_VO) data.getSerializableExtra(ClientNameListActivity.CLIENT_LIST);
            binding.tvReceiverName.setText(cltVO.CLT_02);
            binding.tvReceiveNumber.setText(cltVO.CLT_01);
        } else if (requestCode == kr.hmit.dmjs.ui.order_management.FindProductActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            dahVO = (ProductionInfoVO) data.getSerializableExtra(kr.hmit.dmjs.ui.order_management.FindProductActivity.PRODUCT_LIST);
            binding.tvProductName.setText(dahVO.DAH_02);
            binding.tvProductNum.setText(dahVO.DAH_01);
            binding.tvUnitPrice.setText(changeToMoney(dahVO.DAH_11));
        }
        /*else if (requestCode == kr.hmit.dmjs.ui.receive.FindConstructionActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            remVO=(REM_VO) data.getSerializableExtra(FindConstructionActivity.CONSTRUCTION_LIST);
            binding.tvConstruction.setText(remVO.REM_03);
            binding.tvConstructionNum.setText(remVO.REM_01);

        }*/

    }

    private void onClickAddClient(View v) {   // 거래처찾기
        Intent intent = new Intent(mContext, ClientNameListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, ClientNameListActivity.REQUEST_CODE);
    }

    private void onClickAddWHC(View v) {  // 현장명( 수주잧기)
        if (mCategory != null && mCategory.length == 1) {
            requestWHC_Read();
        } else {
            dropdownCategory("창고위치", mCategory, binding.tvConstruction);
        }

    }

    private void dropdownCategory(String dialogTitle, String[] category, TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(dialogTitle)
                .setItems(category, (dialog, which) -> {
                    textView.setText(category[which].contains("선택안함")?"":category[which]);
                    if (dialogTitle.equals("창고위치")) {
                        binding.tvWHC01.setText(WHC_01[which]);
                        binding.tvWHC02.setText(WHC_02[which]);

                    }
                }).setCancelable(false).create();

        builder.show();
    }

    private void onClickAddProduct(View v) {    // 제품찾기
        //Intent intent = new Intent(mContext, FindProductActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        //mActivity.startActivityForResult(intent, FindProductActivity.REQUEST_CODE);
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
                                if (response.body().Total > 0) {
                                    if (response.body().Data.get(0).Validation) {
                                        bindingCategory(response.body().Data);
                                    } else {
                                        toast("동시접속 > 로그인 페이지로 이동합니다");  //
                                        mSettings.putBooleanItem(SettingsKey.AutoLogin, false);
                                        Intent intent = new Intent(mContext, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("simultaneous_connect", mUser.Value.MEM_01.toString());
                                        startActivity(intent);
                                        finish();
                                        // BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                } else {
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
        WHC_01 = new String[data.size() + 1];
        WHC_02 = new String[data.size() + 1];
        mCategory[0] = "선택안함";
        WHC_01[0] = "";
        WHC_02[0] = "";
        for (int i = 0; i < data.size(); i++) {
            mCategory[i + 1] = data.get(i).WHC_03;
            WHC_01[i + 1] = data.get(i).WHC_01;
            WHC_02[i + 1] = data.get(i).WHC_02;
        }

    }


}