package kr.hmit.dmjs.ui.production_plan;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityProductionDetailBinding;
import kr.hmit.dmjs.model.vo.MSM_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.order_management.FindProductActivity;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductionDetailActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8013;
    private MSM_VO MSM;
    public TextView tvTotalSum;
    private int totalSum;
    private ProductionInfoVO vo;
    private ArrayList<String> list;
    //=====================================
    // region // static, final
    //=====================================

    //=====================================
    // endregion // static, final
    //=====================================


    //=====================================
    // region // view
    //=====================================

    private ActivityProductionDetailBinding mBinding;

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
        mBinding = ActivityProductionDetailBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        tvTotalSum = (TextView)findViewById(R.id.tvTotalSum);

        Intent intent = getIntent();
        MSM = (MSM_VO) intent.getExtras().get("msmVO");

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        mBinding.imgBack.setOnClickListener(v -> finish());
//        mBinding.imgUpdate.setOnClickListener(this::onClickUpdateProduct);
        mBinding.btnUpdate.setOnClickListener(v -> {
            mBinding.calendarView.clearFocus();
            list = new ArrayList<>();
            list = mBinding.calendarView.getData();
            totalSum = mBinding.calendarView.setTotal(list);
            //todo 업데이트 api 호출
            requestSaveMSM("M_UPDATE",list);
        });
        mBinding.btnDelete.setOnClickListener(this::onClickDelete);
    }
    private void onClickUpdateProduct(View v) {
        Intent intent = new Intent(mContext, FindProductActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, FindProductActivity.REQUEST_CODE);
    }

    private void onClickDelete(View view) {
        AlertDialog.Builder oDialog = new AlertDialog.Builder(this,
                android.R.style.Theme_DeviceDefault_Light_Dialog);

        oDialog.setTitle("삭제하시겠습니까?")
                .setPositiveButton("아니오", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                })
                .setNeutralButton("예", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        list = new ArrayList<>();
                        list = mBinding.calendarView.getData();
                        requestSaveMSM("M_DELETE",list);
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .setCancelable(false)
                .show();

    }
    @Override
    protected void initialize() {
        // todo 날짜 설정
        //mCalSelected
        mBinding.calendarView.setDate(mCalSelected);
        ArrayList<String> list = new ArrayList<>();
        list.add(0,MSM.MSMS_1001);
        list.add(1,MSM.MSMS_1002);
        list.add(2,MSM.MSMS_1003);
        list.add(3,MSM.MSMS_1004);
        list.add(4,MSM.MSMS_1005);
        list.add(5,MSM.MSMS_1006);
        list.add(6,MSM.MSMS_1007);
        list.add(7,MSM.MSMS_1008);
        list.add(8,MSM.MSMS_1009);
        list.add(9,MSM.MSMS_1010);
        list.add(10,MSM.MSMS_1011);
        list.add(11,MSM.MSMS_1012);
        list.add(12,MSM.MSMS_1013);
        list.add(13,MSM.MSMS_1014);
        list.add(14,MSM.MSMS_1015);
        list.add(15,MSM.MSMS_1016);
        list.add(16,MSM.MSMS_1017);
        list.add(17,MSM.MSMS_1018);
        list.add(18,MSM.MSMS_1019);
        list.add(19,MSM.MSMS_1020);
        list.add(20,MSM.MSMS_1021);
        list.add(21,MSM.MSMS_1022);
        list.add(22,MSM.MSMS_1023);
        list.add(23,MSM.MSMS_1024);
        list.add(24,MSM.MSMS_1025);
        list.add(25,MSM.MSMS_1026);
        list.add(26,MSM.MSMS_1027);
        list.add(27,MSM.MSMS_1028);
        list.add(28,MSM.MSMS_1029);
        list.add(29,MSM.MSMS_1030);
        list.add(30,MSM.MSMS_1031);
        mBinding.calendarView.setProductionPlan(list);
        mBinding.tvProductName.setText(MSM.DAH_02);
        mBinding.tvProductCode.setText(MSM.MSMS_03);
        mBinding.tvTotalSum.setText(MSM.MSMS_10);
    }

    public void requestSaveMSM(String GUBUN,ArrayList<String> list) {
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
                GUBUN,
                mUser.Value.MEM_CID,
                MSM.MSMS_02,
                mBinding.tvProductCode.getText().toString(),
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

                        if(GUBUN.equals("M_UPDATE")){
                            toast("처리결과를 입력하였습니다.");
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 담당자 추가
        if (requestCode == FindProductActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            vo=(ProductionInfoVO) data.getSerializableExtra(FindProductActivity.PRODUCT_LIST);
            mBinding.tvProductName.setText(vo.DAH_02);
            mBinding.tvProductCode.setText(vo.DAH_01);
        }
    }
}