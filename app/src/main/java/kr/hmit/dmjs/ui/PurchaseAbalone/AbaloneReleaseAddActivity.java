package kr.hmit.dmjs.ui.PurchaseAbalone;

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

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;

import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityAbaloneReleaseAddBinding;

import kr.hmit.dmjs.model.response.NGO_Model;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.model.vo.NGO_VO;
import kr.hmit.dmjs.model.vo.NGO_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.order_request.ClientNameListActivity;
import kr.hmit.dmjs.ui.order_request.OderRequestFindProductActivity;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbaloneReleaseAddActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8023;
    public static final String ORDER_REQUEST_ADD = "ORDER_REQUEST_ADD";

    private ActivityAbaloneReleaseAddBinding binding;

    private boolean flag = true;
    private String[] mCategory;
    private String[] mCategoryCode;
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private CLT_VO clt_vo;
    private ProductionInfoVO dah_vo;
private String NGO_08;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            binding.tvNGO02.setText(sdfFormat.format(mCalRequest.getTime()));

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAbaloneReleaseAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.tvNGO02.setOnClickListener(this::onClickOrderDate);

        binding.tvNGO08.setOnClickListener(this::onClickCategory);
        binding.addClientName.setOnClickListener(this::onClickAddClientName);
        binding.btnSave.setOnClickListener(this::onClickSave);
        binding.addProductNum.setOnClickListener(this::onClickAddProduct);


        binding.tvNGO06.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });
        binding.tvNGO05.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });
    }

    private void calculate(){

        int NGO_05 = Integer.parseInt(binding.tvNGO05.getText().toString().isEmpty()?"0":binding.tvNGO05.getText().toString());
        int NGO_06 = Integer.parseInt(binding.tvNGO06.getText().toString().isEmpty()?"0":binding.tvNGO06.getText().toString());


        binding.tvNGO07.setText("" + NGO_05*NGO_06);


      //  int NGO_06 = Integer.parseInt(binding.tvNGO06.getText().toString());
      //  int NGO_05 = Integer.parseInt(binding.tvNGO05.getText().toString());


    }
    @Override
    protected void initialize() {
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
        binding.tvNGO02.setText(sdfFormat.format(mCalRequest.getTime()));

        bindingCategory();
        //requestCombo_Read();

    }
    private void onClickOrderDate(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        flag=true;
        datePickerDialog.show();
    }
    private void onClickDeliveryDate(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        flag=false;
        datePickerDialog.show();
    }

    private void onClickCategory(View v) {
        dropdownCategory();

    }
    private void onClickAddClientName(View v) {
        Intent intent = new Intent(mContext, ClientNameListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, ClientNameListActivity.REQUEST_CODE);
    }
    private void onClickSave(View view) {
        if (binding.tvNGO08.getText().toString().equals("판매유형")) {
            toast("출고구분을 선택해주세요.");
        } else if (binding.tvCLT.getText().toString().isEmpty()||clt_vo.CLT_01==null) {
            toast("출고처를 선택해주세요.");
        }else if (binding.tvDAH.getText().toString().isEmpty()||dah_vo.DAH_01==null) {
            toast("품명을 선택해주세요.");
        }else if (binding.tvNGO06.getText().toString().equals("")) {
            toast("수량은 최소 1이상 입니다.");
        }else {
            requestSaveNGO();
        }
    }
    private void onClickAddProduct(View v) {
        Intent intent = new Intent(mContext, OderRequestFindProductActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, OderRequestFindProductActivity.REQUEST_CODE);
    }

    private void dropdownCategory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle("출고구분")
                .setItems(mCategory, (dialog, which) -> {
                    binding.tvNGO08.setText(mCategory[which]);
                    NGO_08=mCategoryCode[which];
                }).setCancelable(false).create();

        builder.show();
    }


    /*private void bindingCategory(ArrayList<NGO_VO> data) {


                if (data.size()==0 ){

                    mCategory = new String[1];
                    mCategoryCode =  new String[1];
                    mCategory[0]=   "없음";
                    mCategoryCode[0] ="";
                }
                else {

                    mCategory = new String[data.size()];
                    mCategoryCode = new String[data.size()];
            for (int i = 0; i < data.size(); i++) {
                mCategory[i] = data.get(i).NGO_08_NM;
                mCategoryCode[i] = data.get(i).NGO_08;
            }
        }

    }*/

    private void bindingCategory() {

        mCategory = new String[3];
        mCategoryCode =  new String[3];

        mCategory[0]=  "도매";
        mCategoryCode[0] ="W";
        mCategory[1]=  "판매";
        mCategoryCode[1] ="D";
        mCategory[2]=  "가공";
        mCategoryCode[2] ="P";

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 담당자 추가
        if (requestCode == ClientNameListActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            clt_vo = (CLT_VO) data.getSerializableExtra(ClientNameListActivity.CLIENT_LIST);
            binding.tvCLT.setText(clt_vo.CLT_02);
        }else if (requestCode == OderRequestFindProductActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            dah_vo=(ProductionInfoVO) data.getSerializableExtra(OderRequestFindProductActivity.PRODUCT_LIST);
            binding.tvDAH.setText(dah_vo.DAH_02);
            binding.tvNGO05.setText(dah_vo.DAH_11+"");
        }
    }


    public void requestSaveNGO() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();
        Http.ngo(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).NGO_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_INSERT",
                mUser.Value.MEM_CID,
                "",
                binding.tvNGO02.getText().toString().replaceAll("-",""),
                clt_vo.CLT_01,
                dah_vo.DAH_01,
                Float.parseFloat(binding.tvNGO05.getText().toString()),
                Float.parseFloat(binding.tvNGO06.getText().toString()),
                Float.parseFloat(binding.tvNGO07.getText().toString()),
                NGO_08,
                "",
                0,
                binding.tvNGO97.getText().toString(),
                mUser.Value.MEM_01

        ).enqueue(new Callback<NGO_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<NGO_Model> call, Response<NGO_Model> response) {
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
                        toast("");
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }.sendMessage(msg);
            }


            @Override
            public void onFailure(Call<NGO_Model> call, Throwable t) {
                call.cancel();
                closeLoadingBar();
            }
        });
    }


}
