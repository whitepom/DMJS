package kr.hmit.dmjs.ui.m004;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.base.settings.SettingsKey;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityOdm02MainBinding;
import kr.hmit.dmjs.model.response.ODD_Model;
import kr.hmit.dmjs.model.response.WHC_Model;
import kr.hmit.dmjs.model.vo.LOT_VO;
import kr.hmit.dmjs.model.vo.ODD_VO;
import kr.hmit.dmjs.model.vo.WHC_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.network.Http_Odd;
import kr.hmit.dmjs.ui.login.LoginActivity;
import kr.hmit.dmjs.ui.m004.adapter.Odm02MainListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Odm02MainActivity extends BaseActivity {
    public static final int REQUEST_CODE = 2020;
    public static final String ODD_WATING_LIST = "ODD_WATING_LIST";

    private ActivityOdm02MainBinding binding;
    private Odm02MainListAdapter mAdapter;
    private ArrayList<ODD_VO> mList;

    //달력관련
    private Calendar mCalRequest;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
    private int DateFlag;
    
    private String[] mCategory , mWhc01 , mWhc02;

    //파라미터
    private HashMap<String, Object> paramMap = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOdm02MainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
        initLayout();

    }
    @Override
    protected void initialize() {
        //입고대기 초기화
        mList = new ArrayList<>();
        mAdapter = new Odm02MainListAdapter();
        binding.recyclerView.setAdapter(mAdapter);

        //입고일자 셋팅
        mCalRequest = Calendar.getInstance();
        mCalRequest.add(Calendar.DATE,-7);
        binding.odd10.setText(sdfFormat.format(mCalRequest.getTime()));

        //창고셋팅
        WHC_Read();

        //입고대기 목록
        ODD_Read();
    }
    @Override
    protected void initLayout() {

        binding.odd10.setOnClickListener(v -> onClicktvDate((TextView) v, 1));
        binding.imgBack.setOnClickListener(v -> finish());
        binding.imgGoTop.setOnClickListener(this::onClickGoTop);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        binding.imgSearch.setOnClickListener(this::onClickSearch);
        binding.etSearch.setOnEditorActionListener(this::onSearch);
        binding.addWhc.setOnClickListener(v -> onClickCombo((View) v , mCategory , "1"));

        binding.btnAllRelease.setOnClickListener(this::onClickGoRelease);

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
        ODD_Read();
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
                            binding.odd10.setText(sdfFormat.format(mCalRequest.getTime()));
                        }
                    }
                },
                mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH),
                mCalRequest.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
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
            binding.tvWarehouseName.setText(mCategory[which]);
            binding.tvWarehouseNum1.setText(mWhc01[which]);
            binding.tvWarehouseNum2.setText(mWhc02[which]);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void ODD_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        paramMap = setParamMap("ODD_ID", "M_LIST2");
        paramMap.put("ODD_01",binding.etSearch.getText());

        Http_Odd.odd(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).ODD_READ_API(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<ArrayList<ODD_VO>>() {
            @Override
            public void onResponse(Call<ArrayList<ODD_VO>> call, Response<ArrayList<ODD_VO>> response) {
                closeLoadingBar();
                ArrayList<ODD_VO> data = response.body();

                if (response.isSuccessful()) {
                    if(data.size() > 0){
                        bindingData(data);
                    }else{
                        bindingData(new  ArrayList<ODD_VO>());
                        BaseAlert.show(mContext,"검색결과가 없습니다." );
                    }
                } else {
                    BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ODD_VO>> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
                t.printStackTrace();
            }
        });
    }

    private void bindingData(ArrayList<ODD_VO> data) {
        mList = data;
        mAdapter.setList(mList);
    }


    private void onClickGoTop(View v) {
        binding.recyclerView.smoothScrollToPosition(0);
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

    private void onClickGoRelease(View view) {
        ArrayList<ODD_VO> mListCheck = mAdapter.getCheckList();

        if(mListCheck.size()>0) {
            boolean tempBool =true;

            for(int i = 0; i < mListCheck.size(); i++){
                if(mListCheck.get(i).ODD_05 < (mListCheck.get(i).GEM_06  + mListCheck.get(i).ODD_08)) {
                     tempBool=false;
                     break;
                }
            }

            if (tempBool) {
                for (int i = 0; i < mListCheck.size(); i++) {
                    if (i == (mListCheck.size() - 1)) {
                        ODD_U(mListCheck.get(i), true);
                    } else {
                        ODD_U(mListCheck.get(i), false);
                    }
                }
            } else {
                toast("입고수량은 발주수량을 넘을 수 없습니다.");
            }
        }else{
            toast("입고처리할 주문이 없습니다.");
        }
    }

    public void ODD_U(ODD_VO vo,boolean refreshFlag) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        paramMap = setParamMap("ODD_ID", "M_INSERT");
        paramMap.put("ODD_01", vo.ODD_01);
        paramMap.put("ODD_02", vo.ODD_02);
        paramMap.put("ODD_03", vo.ODD_03);
        paramMap.put("ODD_04", vo.ODD_04);
        paramMap.put("ODD_10", binding.odd10.getText().toString());
        paramMap.put("GEM_06", vo.GEM_06);
        paramMap.put("CLT_01", vo.CLT_01);
        paramMap.put("WHC_01", binding.tvWarehouseNum1.getText().toString());
        paramMap.put("WHC_02", binding.tvWarehouseNum2.getText().toString());

        Http_Odd.odd(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).ODD_L_Edit(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<BaseModel>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                Message msg = new Message();
                msg.obj = response;
                msg.what = 100;

                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        closeLoadingBar();
                        if(refreshFlag){
                            toast("입고처리 되었습니다.");
                            ODD_Read();
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

}
