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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityDeliveryStatusFilterBinding;
import kr.hmit.dmjs.model.response.CODE_Model;
import kr.hmit.dmjs.model.vo.CODE_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.ui.release.model.DeliveryStatusFilterVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryStatusFilterActivity extends BaseActivity{
    public static final int REQUEST_CODE = 2481;
    public static final String CATEGORY_INFO = "CATEGORY_INFO";

    private ActivityDeliveryStatusFilterBinding binding;
    private DeliveryStatusFilterVO filterVO;
    private boolean flag;
    private String[] mCategory;
    private String[] mCategorynum;
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
        binding = ActivityDeliveryStatusFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.tvFilter2.setOnClickListener(v -> onClickRequestDate((TextView) v, true));
        binding.tvFilter3.setOnClickListener(v -> onClickRequestDate((TextView) v, false));
        binding.tvFilter31.setOnClickListener(v -> dropdownWorkList());
        binding.btnSave.setOnClickListener(this::onClickSave);

        requestCode();
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {
        Intent intent = getIntent();
        filterVO = (DeliveryStatusFilterVO) intent.getExtras().get("filterData");
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        mCalRequest = Calendar.getInstance();

        binding.tvFilter2.setText(filterVO.RUN_02_ST);
        binding.tvFilter3.setText(filterVO.RUN_02_ED);

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
        String REQ_02_ST = binding.tvFilter2.getText().toString();
        String REQ_02_ED = binding.tvFilter3.getText().toString();
        String CLT_01 = saveNum;

        filterVO = new DeliveryStatusFilterVO(CLT_01,REQ_02_ST, REQ_02_ED);
        Intent intent = new Intent();
        intent.putExtra(CATEGORY_INFO, filterVO);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void dropdownWorkList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle("업체구분")
                .setItems(mCategory, (dialog, which) -> {
                    setworklist(which);

                }).setCancelable(true).create();

        builder.show();
    }


    private void setworklist(int which){
        binding.tvFilter31.setText(mCategory[which]);
        if(which == 0){
            saveNum = "";
        }else{
            saveNum = mCategorynum[which -1];
        }

    }


    public void requestCode() {
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
                "DAH19",
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
                                    bindingCategory(response.body().Data);
                                }else{
                                    bindingCategory(new ArrayList<CODE_VO>());
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
    private void bindingCategory(ArrayList<CODE_VO> data) {
        mCategory = new String[data.size() + 1];
        mCategorynum = new String[data.size()];

        mCategory[0] = "-전체-";

        for (int i = 0; i < data.size(); i++) {
            mCategory[i + 1] = data.get(i).CD_NM;
            mCategorynum[i] = data.get(i).CD;
        }
    }


}
