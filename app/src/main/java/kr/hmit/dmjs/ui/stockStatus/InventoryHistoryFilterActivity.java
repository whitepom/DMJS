package kr.hmit.dmjs.ui.stockStatus;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityInventoryHistoryFilterBinding;
import kr.hmit.dmjs.model.response.CODE_Model;
import kr.hmit.dmjs.model.vo.CODE_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityInventoryHistoryFilterBinding;
import kr.hmit.dmjs.model.response.CDO_Model;
import kr.hmit.dmjs.model.vo.CDO_VO;
import kr.hmit.dmjs.model.vo.CODE_VO;
import kr.hmit.dmjs.ui.stockStatus.model.InventoryFilterVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventoryHistoryFilterActivity extends BaseActivity{
    public static final int REQUEST_CODE = 2481;
    public static final String CATEGORY_INFO = "CATEGORY_INFO";

    private ActivityInventoryHistoryFilterBinding binding;
    private InventoryFilterVO filterVO;
    private boolean flag;

    private String[] mCategoryGubun;
    private String[] mCategoryGubunnum;

    private String[] mCategoryGrade;
    private String[] mCategoryGradenum;

    private String[] mCategoryIpChul;


    private String saveNum;

    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private final DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if (flag) {
                binding.tvFilter2.setText(sdfFormat.format(mCalRequest.getTime()));
                binding.tvFilter3.performClick();
            }else {
                binding.tvFilter3.setText(sdfFormat.format(mCalRequest.getTime()));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInventoryHistoryFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.tvFilter2.setOnClickListener(v -> onClickRequestDate((TextView) v, true));
        binding.tvFilter3.setOnClickListener(v -> onClickRequestDate((TextView) v, false));
        binding.tvFilter31.setOnClickListener(v -> dropdownGubunlist());
        binding.tvFilter32.setOnClickListener(v -> dropdownGradeList());
        binding.tvFilter33.setOnClickListener(v -> dropdownipchullist());
        binding.btnSave.setOnClickListener(this::onClickSave);

        requestCode("DAH_10");

        requestCode("DAH_03");

    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {
        Intent intent = getIntent();
        filterVO = (InventoryFilterVO) intent.getExtras().get("filterData");
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        mCalRequest = Calendar.getInstance();

        binding.tvFilter2.setText(filterVO.OOK_02_ST);
        binding.tvFilter3.setText(filterVO.OOK_02_ED);

    }
    //요청일자
    private void onClickRequestDate(TextView v, boolean flag) {
        this.flag = flag;
        String src;
        if (flag) {
            src = binding.tvFilter3.getText().toString();
        } else {
            src = binding.tvFilter2.getText().toString();
        }
        String tv = v.getText().toString();
        onRequestDate(tv, src, datePickerListener);
    }

    private void onClickSave(View view) {
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        String OOK_02_ST = binding.tvFilter2.getText().toString();
        String OOK_02_ED = binding.tvFilter3.getText().toString();
        String CLT_01 = saveNum;

        filterVO = new InventoryFilterVO("","","");
        Intent intent = new Intent();
        intent.putExtra(CATEGORY_INFO, filterVO);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void dropdownGubunlist() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle("제품구분")
                .setItems(mCategoryGubun, (dialog, which) -> {
                    setGubunlist(which);

                }).setCancelable(true).create();

        builder.show();
    }


    private void setGubunlist(int which){
        binding.tvFilter31.setText(mCategoryGubun[which]);
        if(which == 0){
            saveNum = "";
        }else{
            saveNum = mCategoryGubunnum[which -1];
        }

    }

    private void dropdownGradeList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle("제품구분")
                .setItems(mCategoryGubun, (dialog, which) -> {
                    setGradelist(which);

                }).setCancelable(true).create();

        builder.show();
    }


    private void setGradelist(int which){
        binding.tvFilter31.setText(mCategoryGrade[which]);
        if(which == 0){
            saveNum = "";
        }else{
            saveNum = mCategoryGradenum[which -1];
        }

    }

    private void dropdownipchullist() {

        mCategoryIpChul = new String[]{"-전체-","입고","출고"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle("제품구분")
                .setItems(mCategoryIpChul, (dialog, which) -> {
                    setipchul(which);

                }).setCancelable(true).create();

        builder.show();
    }

    private void setipchul(int which){
        binding.tvFilter33.setText(mCategoryIpChul[which]);
    }



    public void requestCode(String CODE_DIV) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }
        openLoadingBar();

        Http.ook(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).CODE_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "",
                "LIST",
                mUser.Value.MEM_CID,
                "",
                CODE_DIV,
                ""

        ).enqueue(new Callback<CODE_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<CODE_Model> call, Response<CODE_Model> response) {
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

                            Response<CODE_Model> response = (Response<CODE_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if(CODE_DIV.equals("DAH_03")){
                                        bindingCategoryGubun(response.body().Data);
                                    }else{
                                        bindingCategoryGrade(response.body().Data);
                                    }
                                }else{
                                    if(CODE_DIV.equals("DAH_03")){
                                        bindingCategoryGubun(response.body().Data);
                                    }else{
                                        bindingCategoryGrade(response.body().Data);
                                    }
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
            public void onFailure(Call<CODE_Model> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
            }
        });
    }


    /**
     * 업무분류 바인딩
     *
     * @param data
     */
    private void bindingCategoryGubun(ArrayList<CODE_VO> data) {
        mCategoryGubun = new String[data.size() + 1];
        mCategoryGubunnum = new String[data.size()];

        mCategoryGubun[0] = "-전체-";

        for (int i = 0; i < data.size(); i++) {
            mCategoryGubun[i + 1] = data.get(i).CD_NM;
            mCategoryGubunnum[i] = data.get(i).CD;
        }
    }

    private void bindingCategoryGrade(ArrayList<CODE_VO> data) {
        mCategoryGrade = new String[data.size() + 1];
        mCategoryGradenum = new String[data.size()];

        mCategoryGrade[0] = "-전체-";

        for (int i = 0; i < data.size(); i++) {
            mCategoryGrade[i + 1] = data.get(i).CD_NM;
            mCategoryGradenum[i] = data.get(i).CD;
        }
    }


}
