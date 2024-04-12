package kr.hmit.dmjs.ui.m004;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.base.settings.SettingsKey;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityOdm03MainAddBinding;
import kr.hmit.dmjs.databinding.ActivityReceiveAddBinding;
import kr.hmit.dmjs.model.response.WHC_Model;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.model.vo.GEM_VO;
import kr.hmit.dmjs.model.vo.REM_VO;
import kr.hmit.dmjs.model.vo.WHC_VO;
import kr.hmit.dmjs.model.vo.ZAG_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.network.Http_Gem;
import kr.hmit.dmjs.network.Http_Odd;
import kr.hmit.dmjs.ui.login.LoginActivity;
import kr.hmit.dmjs.ui.order_request.ClientNameListActivity;
import kr.hmit.dmjs.ui.popup.dah.DahFindActivity;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Odm03MainAddActivity extends BaseActivity {

    public static final int REQUEST_CODE = 2929;
    public static final String GEM_INFO_ADD = "GEM_INFO_ADD";

    private ActivityOdm03MainAddBinding binding;

    //달력선택 관련
    private DatePickerDialog datePickerDialog;
    private int DateFlag;
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());

    //창고
    private String[] mCategory , mWhc01 , mWhc02;


    //파라미터
    private HashMap<String, Object> paramMap = new HashMap<String, Object>();

    //팝업
    private CLT_VO clt_vo = new CLT_VO();
    private ProductionInfoVO dah_vo = new ProductionInfoVO();

    private double GEM_06 = 0;
    private double GEM_05 = 0;
    private double GEM_07 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOdm03MainAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
        initLayout();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.gem02.setOnClickListener(v -> onClicktvDate((TextView) v , 1));

        binding.addProduct.setOnClickListener(this::onClickAddProduct);
        binding.addReceiverName.setOnClickListener(this::onClickAddClient);
        binding.addWHC.setOnClickListener(v -> onClickCombo((View) v , mCategory , "1"));

        binding.btnSave.setOnClickListener(this::onClickSave);
        binding.gem06.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(binding.gem06.getText().toString().equals("")){
                    GEM_06 = 0;
                }else{
                    GEM_06 = Double.parseDouble(binding.gem06.getText().toString());
                }

                binding.gem07.setText(GEM_06*Double.parseDouble(binding.gem05.getText().toString())+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.gem05.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(binding.gem05.getText().toString().equals("")){
                    GEM_05 = 0;
                }else{
                    GEM_05 = Double.parseDouble(binding.gem05.getText().toString());
                }
                binding.gem07.setText(Double.parseDouble(binding.gem06.getText().toString())*GEM_05+"");
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
        binding.gem02.setText(sdfFormat.format(mCalRequest.getTime()));

        WHC_Read();
    }

    private void WHC_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        paramMap = setParamMap("WHC_ID", "LIST_M");

        Http_Odd.odd(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).WHC_Read(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<ArrayList<WHC_VO>>() {
            @Override
            public void onResponse(Call<ArrayList<WHC_VO>> call, Response<ArrayList<WHC_VO>> response) {
                closeLoadingBar();
                ArrayList<WHC_VO> data = response.body();

                if (response.isSuccessful()) {
                    if(data.size() > 0){
                        bindingCategory(data);
                    }else{
                        //bindingData(new  ArrayList<WHC_VO>());
                        BaseAlert.show(mContext,"검색결과가 없습니다." );
                    }
                } else {
                    BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<WHC_VO>> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
                t.printStackTrace();
            }
        });
    }

    private void onClickCombo(View v , String[] paramArry , String comboDiv) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        String title ="";

        if(comboDiv.equals("1")){
            title ="창고선택";
        }

        builder.setTitle(title).setCancelable(true)
                .setItems(paramArry, (dialog, which) -> {
                    setArry(which , comboDiv);

                }).setCancelable(false).create();

        builder.show();
    }

    private void setArry(int which , String comboDiv) {
        if(comboDiv.equals("1")){
            binding.gem2101Nm.setText(mCategory[which]);
            binding.gem2101.setText(mWhc01[which]);
            binding.gem2102.setText(mWhc02[which]);
        }
    }

    private void bindingCategory(ArrayList<WHC_VO> data) {
        mCategory = new String[data.size() + 1];
        mWhc01=new String[data.size()+1];
        mWhc02=new String[data.size()+1];

        mCategory[0] = "-선택안함-";
        mWhc01[0] ="";
        mWhc02[0] ="";

        for (int i = 0; i < data.size(); i++) {
            mCategory[i + 1] = data.get(i).WHC_03;
            mWhc01[i+1]=data.get(i).WHC_01;
            mWhc02[i+1]=data.get(i).WHC_02;
        }
    }

    private void onClicktvDate(TextView v , int paramDateFlog) {

        DateFlag = paramDateFlog;

        datePickerDialog = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        mCalRequest.set(Calendar.YEAR, year);
                        mCalRequest.set(Calendar.MONTH, month);
                        mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        if (DateFlag == 1) {
                            binding.gem02.setText(sdfFormat.format(mCalRequest.getTime()));
                        }
                    }
                },
                mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH),
                mCalRequest.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void onClickSave(View view) {
        if (binding.gem04.getText().toString().isEmpty()) {
            toast("제품정보를 입력해주세요.");
        } else if (binding.gem02.getText().toString().isEmpty()) {
            toast("입고일자를 입력해주세요.");
        } else if (binding.gem03.getText().toString().isEmpty()) {
            toast("입고처를 입력해주세요.");
        } // 현장명 잠시 제외
        else if (binding.gem06.getText().toString().isEmpty()) {
            toast("수량을 입력해주세요.");
        } else {
            GEM_U("INSERT");
        }
    }


    private void onClickAddClient(View v) {   // 거래처찾기
        Intent intent = new Intent(mContext, ClientNameListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, ClientNameListActivity.REQUEST_CODE);
    }

    private void onClickAddProduct(View v) {    // 제품찾기
        Intent intent = new Intent(mContext, DahFindActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        dah_vo.DAH_03= "R|B|";
        intent.putExtra("DAH_VO", dah_vo);
        mActivity.startActivityForResult(intent, DahFindActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ClientNameListActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            clt_vo = (CLT_VO) data.getSerializableExtra(ClientNameListActivity.CLIENT_LIST);
            binding.gem03Nm.setText(clt_vo.CLT_02);
            binding.gem03.setText(clt_vo.CLT_01);
        } else if (requestCode == kr.hmit.dmjs.ui.order_management.FindProductActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            dah_vo = (ProductionInfoVO) data.getSerializableExtra(DahFindActivity.PRODUCT_LIST);
            binding.gem04Nm.setText(dah_vo.DAH_02);
            binding.gem04.setText(dah_vo.DAH_01);
            binding.gem06.setText(dah_vo.DAH_11);
        }
    }

    public void GEM_U(String GUBUN) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        paramMap = setParamMap("GEM_ID", GUBUN);

        paramMap.put("GEM_02", binding.gem02.getText().toString());
        paramMap.put("GEM_03", binding.gem03.getText().toString());
        paramMap.put("GEM_04", binding.gem04.getText().toString());
        paramMap.put("GEM_05", Double.parseDouble(binding.gem05.getText().toString()));
        paramMap.put("GEM_06", Double.parseDouble(binding.gem06.getText().toString()));
        paramMap.put("GEM_07", Double.parseDouble(binding.gem07.getText().toString()));
        paramMap.put("GEM_97", binding.gem97.getText().toString());

        Http_Gem.gem(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).GEM_U(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<GEM_VO>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<GEM_VO> call, Response<GEM_VO> response) {
                Message msg = new Message();
                msg.obj = response;
                msg.what = 100;

                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        closeLoadingBar();

                        toast("활전복 생산관리를 등록하였습니다.");

                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }.sendMessage(msg);
            }
            @Override
            public void onFailure(Call<GEM_VO> call, Throwable t) {
                call.cancel();
                closeLoadingBar();
            }
        });
    }

}