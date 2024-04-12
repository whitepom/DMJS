package kr.hmit.dmjs.ui.production_plan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityProductionAddBinding;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.order_management.FindProductActivity;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import kr.hmit.dmjs.ui.production_plan.date_picker.YearMonthPickerFragment;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductionAddActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8023;
    public static final String Production_Add = "Production_Add";
    private String TAG ="Main";
    //=====================================
    // region // static, final
    //=====================================
    //=====================================
    // endregion // static, final
    //=====================================
    //=====================================
    // region // view
    //=====================================
    private ActivityProductionAddBinding mBinding;
    private ProductionInfoVO vo;
    public TextView etTotalSum;
    ArrayList<String> list;
    private int totalSum;
    //=====================================
    // endregion // view
    //=====================================


    //=====================================
    // region // variable
    //=====================================

    private Calendar mCalSelected = Calendar.getInstance();
    //=====================================
    // endregion // variable
    //=====================================


    //=====================================
    // region // initialize
    //=====================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityProductionAddBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        etTotalSum = (TextView)findViewById(R.id.etTotalSum);

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        mBinding.addProduct.setOnClickListener(this::onClickAddProduct);
        mBinding.imgBack.setOnClickListener(v -> finish());
        mBinding.tvSelectedMonth.setOnClickListener(this::onClickSelectMonth);
        mBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.calendarView.clearFocus();
                list = new ArrayList<>();
                list = mBinding.calendarView.getData();
                totalSum = mBinding.calendarView.setTotal(list);
                checkValidation(list);
            }
        });
    }
    private void onClickAddProduct(View v) {
        Intent intent = new Intent(mContext, FindProductActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, FindProductActivity.REQUEST_CODE);
    }
    private void onClickSelectMonth(View v) {
        YearMonthPickerFragment dlgFrag = YearMonthPickerFragment.newInstance(mCalSelected);
        dlgFrag.setListener((year, month) -> {
            if (mCalSelected.get(Calendar.YEAR) != year || mCalSelected.get(Calendar.MONTH) != month - 1) {
                mCalSelected.set(Calendar.YEAR, year);
                mCalSelected.set(Calendar.MONTH, month - 1);
                mBinding.calendarView.setDate(mCalSelected);

                setMonth();
            }
        });
        dlgFrag.show(getSupportFragmentManager(), "YearMonthPicker");
    }

    @Override
    protected void initialize() {
        mBinding.calendarView.setDate(mCalSelected);
        setMonth();
    }

    @SuppressLint("SimpleDateFormat")
    private void setMonth() {
        mBinding.tvSelectedMonth.setText(new SimpleDateFormat("yyyy-MM").format(mCalSelected.getTime()));
    }
    
    private void checkValidation(ArrayList<String> list) {
        String strName = mBinding.tvProductName.getText().toString().trim();


        if (TextUtils.isEmpty(strName)) {
            toast("제품을 선택해주세요");
            return;
        }

        requestSaveMSM(list);
    }

    public void requestSaveMSM(ArrayList<String> list) {

        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }
        openLoadingBar();
        Http.msm(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).MSM_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_INSERT",
                mUser.Value.MEM_CID,
                mBinding.tvSelectedMonth.getText().toString().replaceAll("-",""),
                vo.DAH_01,
                ""+totalSum,
                list.get(0),
                list.get(1),
                list.get(2),
                list.get(3),
                list.get(4),
                list.get(5),
                list.get(6),
                list.get(7),
                list.get(8),
                list.get(9),
                list.get(10),
                list.get(11),
                list.get(12),
                list.get(13),
                list.get(14),
                list.get(15),
                list.get(16),
                list.get(17),
                list.get(18),
                list.get(19),
                list.get(20),
                list.get(21),
                list.get(22),
                list.get(23),
                list.get(24),
                list.get(25),
                list.get(26),
                list.get(27),
                list.get(28),
                list.get(29),
                list.get(30),
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
                        toast("생산계획를 생성하였습니다.");
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
            mBinding.tvProductName.setText(vo.DAH_02);
            mBinding.tvProductNum.setText(vo.DAH_01);
        }
    }
    //=====================================
    // endregion // initialize
    //=====================================
}