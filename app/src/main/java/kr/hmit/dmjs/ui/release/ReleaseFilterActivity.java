package kr.hmit.dmjs.ui.release;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.base.settings.SettingsKey;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityReleaseFilterBinding;
import kr.hmit.dmjs.model.response.RUN_Model;
import kr.hmit.dmjs.model.vo.RUN_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.login.LoginActivity;
import kr.hmit.dmjs.ui.release.model.FilterVO;
import kr.hmit.base.base_activity.BaseActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ReleaseFilterActivity extends BaseActivity  {
    public static final int REQUEST_CODE = 7721;
    public static final String CATEGORY_INFO = "CATEGORY_INFO";

    private ActivityReleaseFilterBinding binding;

    private boolean flag = true;
    private String[] mCategory1;
    private FilterVO filterVO;
    private String[] CLT_01;
    private String CLT_01_;
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if (flag) {
                mCalRequest.set(Calendar.DAY_OF_MONTH,1);
                binding.RUN02ST.setText(sdfFormat.format(mCalRequest.getTime()));
            }
            else binding.RUN02ED.setText(sdfFormat.format(mCalRequest.getTime()));

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReleaseFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        CLT_01_="";
        //  mCategory1=filterVO.mCategory1;


        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.tvFilter1.setOnClickListener(this::onClickCategory1);
        binding.btnSave.setOnClickListener(this::onClickSave);
        binding.RUN02ST.setOnClickListener(this::onClickRequestDate);
        binding.RUN02ED.setOnClickListener(this::onClickRequestDate2);


    }
    private void onClickRequestDate(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        flag=true;
        datePickerDialog.show();
    }
    private void onClickRequestDate2(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        flag=false;
        datePickerDialog.show();
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
    private void onClickSave(View view) {
        String tvFilter1Text = binding.tvFilter1.getText().toString().equals("전체")?"":binding.tvFilter1.getText().toString();
        String RUN_02_ST = binding.RUN02ST.getText().toString().replaceAll("-","");

        String RUN_02_ED = binding.RUN02ED.getText().toString().replaceAll("-","");
        filterVO = new FilterVO(CLT_01_,tvFilter1Text,RUN_02_ST,RUN_02_ED,""); // 순서대로 CLT_01, CLT_02, RUN_02_ST, RUN_02_ED, DAH_01
        Intent intent = new Intent();
        intent.putExtra(CATEGORY_INFO, filterVO);
        setResult(RESULT_OK, intent);
        finish();
    }
    private void onClickCategory1(View v) {

        dropdownCategory("거래처",mCategory1,binding.tvFilter1);
    }
    private void dropdownCategory(String dialogTitle, String[] category, TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(dialogTitle)
                .setItems(category, (dialog, which) -> {
                    textView.setText(category[which]);
                    CLT_01_=CLT_01[which];
                }).setCancelable(false).create();

        builder.show();
    }

    @Override
    protected void initialize() {
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
        Intent intent = getIntent();
        filterVO = (FilterVO) intent.getExtras().get("filterData");
        binding.tvFilter1.setText(textCheck(filterVO.CLT_02));
        binding.RUN02ED.setText(datePatternChange(filterVO.RUN_02_ED));
        binding.RUN02ST.setText(datePatternChange(filterVO.RUN_02_ST));

        requestRunCLT();
    }
    private String textCheck(String str) {
        if(str.equals(""))
            str="전체";
        else if(str.equals("전체"))
            str="";

        return str;
    }

    private void requestRunCLT() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.run(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).RUN_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_FILTER_CLT",
                "DMJS",
                "",
                "",""
        ).enqueue(new Callback<RUN_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<RUN_Model> call, Response<RUN_Model> response) {
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

                            Response<RUN_Model> response = (Response<RUN_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if (response.body().Data.get(0).Validation) {
                                        bindingCategory(response.body().Data);
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
                                    bindingCategory(new ArrayList<RUN_VO>());
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
            public void onFailure(Call<RUN_Model> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
            }
        });
    }

    private void bindingCategory(ArrayList<RUN_VO> data) {
        mCategory1 = new String[data.size() + 1];

        CLT_01=new String[data.size()+1];
        mCategory1[0] = "전체";
        CLT_01[0]="";
        for (int i = 0; i < data.size(); i++) {
            mCategory1[i + 1] = data.get(i).CLT_02;
            CLT_01[i+1]=data.get(i).CLT_01;
        }

    }


}
