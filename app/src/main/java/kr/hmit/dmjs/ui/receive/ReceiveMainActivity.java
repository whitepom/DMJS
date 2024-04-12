package kr.hmit.dmjs.ui.receive;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kr.hmit.base.settings.SettingsKey;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityReceiveMainBinding;
import kr.hmit.dmjs.model.response.ODD_Model;
import kr.hmit.dmjs.model.response.WHC_Model;
import kr.hmit.dmjs.model.vo.ODD_VO;
import kr.hmit.dmjs.model.vo.WHC_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.login.LoginActivity;
import kr.hmit.dmjs.ui.receive.adapter.ReceiceListAdapter;
import kr.hmit.dmjs.ui.receive.model.FilterVO;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReceiveMainActivity extends BaseActivity {
    public static final int REQUEST_CODE = 2020;
    public static final String ReceiveMain_INFO = "ReceiveMain_INFO";

    private ActivityReceiveMainBinding binding;
    private String[] mCategory;
    private ReceiceListAdapter mAdapter;
    private ArrayList<ODD_VO> mListTotal;
    private ArrayList<ODD_VO> mListSearch;
    private FilterVO filterVO;
    private String[] WHC_01;
    private String[] WHC_02;
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            binding.tvWarehousingDate.setText(sdfFormat.format(mCalRequest.getTime()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReceiveMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.tvWarehousingDate.setOnClickListener(this::onClickRequestDate);
        binding.imgBack.setOnClickListener(v -> finish());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        binding.imgSearch.setOnClickListener(this::onClickSearch);
        binding.etSearch.setOnEditorActionListener(this::onSearch);
        binding.addWarehouse.setOnClickListener(this::onClickAddWarehouse);
        binding.btnAllRelease.setOnClickListener(this::onClickGoRelease);
        binding.imgGoTop.setOnClickListener(this::onClickGoTop);
    }
    @Override
    protected void initialize() {
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
        binding.tvWarehousingDate.setText(sdfFormat.format(mCalRequest.getTime()));
        requestWHC_Read();
        mListTotal = new ArrayList<>();
        mListSearch = new ArrayList<>();
        filterVO = new FilterVO("","","","","","");

        mAdapter = new ReceiceListAdapter(mContext,mListTotal);
        binding.recyclerView.setAdapter(mAdapter);

        Intent intent = getIntent();
        if (!TextUtils.isEmpty(intent.getStringExtra("Qrcode"))){
            requestODD_Read((String) intent.getExtras().get("Qrcode"));
        }else{
            requestODD_Read("");
        }

    }
    private void onClickGoRelease(View view) {
        ArrayList<ODD_VO> mListcheck=mAdapter.getCheckList();
        if(mListcheck.size()>0) {
            boolean tempBool =true;
            for(int i = 0; i < mListcheck.size(); i++){
                //int tempOdd07 =Integer.parseInt(mListcheck.get(i).ODD_07);
                //int tempOdd05 =Integer.parseInt(mListcheck.get(i).ODD_05);
               // int tempOdd08 =Integer.parseInt(mListcheck.get(i).ODD_08);
               // if(tempOdd07>(tempOdd05-tempOdd08)) {
               //     tempBool=false;
               //     break;
              //  }
            }
            if (tempBool) {
                for (int i = 0; i < mListcheck.size(); i++) {
                    if (i == (mListcheck.size() - 1)) {
                        requestSaveOdd(mListcheck.get(i), true);
                    } else {
                        requestSaveOdd(mListcheck.get(i), false);
                    }
                }
            } else {
                toast("입고수량은 발주수량을 넘을 수 없습니다.");
            }
        }else{
            toast("입고처리할 주문이 없습니다.");
        }
    }

   public void requestSaveOdd(ODD_VO vo,boolean refreshFlag) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }
        openLoadingBar();
        Http.odd(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).ODD_L_Edit(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_INSERT",
                mUser.Value.MEM_CID,
                vo.ODD_01,
                vo.ODD_02,
                vo.ODD_03,
                0,
                binding.tvWarehousingDate.getText().toString().replaceAll("-",""),
                vo.CLT_01,
                binding.tvWarehouseNum1.getText().toString(),
                binding.tvWarehouseNum2.getText().toString(),
                Integer.parseInt(vo.ODD_07)

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
                        if(refreshFlag){
                            toast("입고처리 되었습니다.");
                            requestODD_Read("");
                        }
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

    //요청일자
    private void onClickRequestDate(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void requestODD_Read(String Qrcode) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.odd(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).ODD_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST2",
                mUser.Value.MEM_CID,
                "",
                filterVO.CLT_02,
                filterVO.ODD_03
                //DAH_02,DAH_04 추가
        ).enqueue(new Callback<ODD_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<ODD_Model> call, Response<ODD_Model> response) {
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

                            Response<ODD_Model> response = (Response<ODD_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if (response.body().Data.get(0).Validation) {
                                        bindingData(response.body().Data, Qrcode);
                                    } else {
                                        toast("동일계정 접속 > 로그인 페이지로 이동합니다");  // asdfasdfasdf
                                        mSettings.putBooleanItem(SettingsKey.AutoLogin,false);
                                        Intent intent = new Intent(mContext, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("simultaneous_connect",mUser.Value.MEM_01.toString());
                                        startActivity(intent);
                                        finish();
                                        //BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                }else{
                                    bindingData(new  ArrayList<ODD_VO>(), "");
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
            public void onFailure(Call<ODD_Model> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
            }
        });
    }

    /**
     * 서버 데이터 바인딩
     *
     * @param data
     */
    private void bindingData(ArrayList<ODD_VO> data,String Qrcode) {
        mListTotal = data;

        mAdapter.update(mListTotal);

        binding.imgSearch.performClick();
        onClickQR(Qrcode);
    }

    private boolean onSearch(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            onClickSearch(v);
        } else {
            return false;
        }

        return true;
    }

    private void onClickSearch(View view) {
        String strSearch = binding.etSearch.getText().toString().toUpperCase().trim();

        mListSearch.clear();

        for (int i = 0; i < mListTotal.size(); i++) {
            ODD_VO vo = mListTotal.get(i);

            if (vo.ODD_01.toUpperCase().contains(strSearch)
                    || vo.CLT_02.toUpperCase().contains(strSearch)
                    || vo.ODD_03.toUpperCase().contains(strSearch)
                    || vo.ODD_01.toUpperCase().contains(strSearch)
                    ||vo.ODD_03_NM.toUpperCase().contains(strSearch)) {
                mListSearch.add(vo);
            }
        }

        mAdapter.update(mListSearch);
    }
    private void onClickQR(String QR) {

        mListSearch.clear();

        for (int i = 0; i < mListTotal.size(); i++) {
            ODD_VO vo = mListTotal.get(i);

            if (vo.ODD_01.toUpperCase().contains(QR)) {
                mListSearch.add(vo);
            }
        }
        binding.etSearch.setText(QR);
        mAdapter.update(mListSearch);
    }


    private void onClickAddWarehouse(View v) {   // 창고위치찾기
        if (mCategory != null && mCategory.length == 1) {
            requestWHC_Read();
        } else {
            dropdownCategory("창고위치",mCategory,binding.tvWarehouseName);
        }
    }

    private void dropdownCategory(String dialogTitle, String[] category, TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(dialogTitle)
                .setItems(category, (dialog, which) -> {
                    textView.setText(category[which].contains("선택안함")?"":category[which]);
                    if(dialogTitle.equals("창고위치")) {
                        binding.tvWarehouseNum1.setText(WHC_01[which]);
                        binding.tvWarehouseNum2.setText(WHC_02[which]);

                    }
                }).setCancelable(false).create();

        builder.show();
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
    private void onClickGoTop(View v) {
        binding.recyclerView.smoothScrollToPosition(0);
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

}
