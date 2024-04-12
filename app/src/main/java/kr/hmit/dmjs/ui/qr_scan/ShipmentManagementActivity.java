package kr.hmit.dmjs.ui.qr_scan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.base.settings.SettingsKey;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityShipmentManagementBinding;
import kr.hmit.dmjs.model.response.RCD_Model;
import kr.hmit.dmjs.model.response.RCM_Model;
import kr.hmit.dmjs.model.vo.RCD_VO;
import kr.hmit.dmjs.model.vo.RCM_VO;
import kr.hmit.dmjs.model.vo.RUN_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.login.LoginActivity;
import kr.hmit.dmjs.ui.qr_scan.adapter.ShipmentDetailAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ShipmentManagementActivity extends BaseActivity {
    public static final int REQUEST_CODE = 4201;

    private ActivityShipmentManagementBinding binding;

    private ShipmentDetailAdapter mAdapterRCD;
    private ArrayList<RCD_VO> mListTotal;
    private ArrayList<RCM_VO> mListRcm;
    private ArrayList<RUN_VO> mListRUN;
    private String Qrcode = "";
    private String btnCk = "";
    private String GU="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShipmentManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        Qrcode = intent.getExtras().getString("Qrcode");


        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {

        binding.imgBack.setOnClickListener(this::OnclickGoMain);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        binding.btnDropCancle.setOnClickListener(this::OnClickRun);
    }



    private void OnClickRun(View view) {
        if(btnCk.equals("N")){

       //     requestRUN_U("RUN_INSERT");
            GU = "RUN_INSERT";
            System.out.println("출하완료 눌렀을때 >>>>>>>>>>>>>>>>>");

        }else if (btnCk.equals("Y")){

            GU = "RUN_DELETE";
      //      requestRUN_U("RUN_DELETE");
            System.out.println("출하취소 눌렀을때 >>>>>>>>>>>>>>>>>");
        }

    }


    @Override
    protected void initialize() {
        mListTotal = new ArrayList<>();
        mAdapterRCD = new ShipmentDetailAdapter(mContext, mListTotal);
        binding.recyclerView.setAdapter(mAdapterRCD);


        requestRCM_Read(); // --> RUN_CHK --> Y = 취소 RUN_DELETE -- N = 완료버튼 누르면 RUN_INSERT   //RCM = 메인디테일
        requestRCD_Read(); //RCD = 리사이클러뷰

    }

    private void OnclickGoMain(View view) {
        finish();
        //goActivity(MainDashboardActivity.class);
    }

    /**  @@@@@@@@   이 밑은 API     @@@@@@@@@@@ **/

    //메인한건 조회
    private void requestRCM_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.rcd(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).RCM_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "RCM_LIST",
                mUser.Value.MEM_CID,
                Qrcode,
                0
        ).enqueue(new Callback<RCM_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<RCM_Model> call, Response<RCM_Model> response) {
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

                            Response<RCM_Model> response = (Response<RCM_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if (response.body().Total > 0) {
                                    if (response.body().Data.get(0).Validation) {
                                        bindingData2(response.body().Data);

                                        btnCk = response.body().Data.get(0).RUN_CHK;

                                        binding.tvshippingNumber.setText(Qrcode);
                                        binding.tvShippingDate.setText(response.body().Data.get(0).RCM_02);
                                        binding.tvDemand.setText(response.body().Data.get(0).REM_10_NM);
                                        binding.tvRealDemand.setText(response.body().Data.get(0).REM_02_NM);
                                        binding.tvDestination.setText(response.body().Data.get(0).RCM_17);
                                        binding.tvCarrier.setText(response.body().Data.get(0).RCM_12);
                                        binding.tvCarNum.setText(response.body().Data.get(0).RCM_13);
                                        binding.tvDriverName.setText(response.body().Data.get(0).RCM_14);
                                        binding.tvPhoneNumber.setText(response.body().Data.get(0).RCM_15);

                                        if(btnCk.equals("N")){
                                            binding.btnDropCancle.setText("출하완료");
                                        }else if (btnCk.equals("Y")){
                                            binding.btnDropCancle.setText("출하취소");
                                        }

                                    } else {
                                        toast("동일계정 접속 > 로그인 페이지로 이동합니다");  // asdfasdfasdf
                                        mSettings.putBooleanItem(SettingsKey.AutoLogin,false);
                                        Intent intent = new Intent(mContext, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("simultaneous_connect",mUser.Value.MEM_01.toString());
                                        startActivity(intent);
                                        finish();
                                      //  BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                } else {
                                    bindingData2(new ArrayList<RCM_VO>());
                                }
                            } else {
                                BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<RCM_Model> call, Throwable t) {
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
    private void bindingData2(ArrayList<RCM_VO> data) {

        mListRcm = data;

    }

    //리사이클러뷰에 조회될거
    private void requestRCD_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.rcd(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).RCD_Read(

                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "RCD_LIST",
                mUser.Value.MEM_CID,
                Qrcode,
                0
        ).enqueue(new Callback<RCD_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<RCD_Model> call, Response<RCD_Model> response) {
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

                            Response<RCD_Model> response = (Response<RCD_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if (response.body().Total > 0) {
                                    if (response.body().Data.get(0).Validation) {
                                        bindingData(response.body().Data);
                                    } else {
                                        BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                } else {
                                    bindingData(new ArrayList<RCD_VO>());
                                }
                            } else {
                                BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<RCD_Model> call, Throwable t) {
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
    private void bindingData(ArrayList<RCD_VO> data) {
        mListTotal = data;
        mAdapterRCD.update(mListTotal);
    }


    // RUN_U
/*    private void requestRUN_U(String GUBUN) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.rcd(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).RUN_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                GUBUN,
                mUser.Value.MEM_CID,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                Qrcode,
                "",
                "",
                "",
                "",
                "",
                "",
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

                        //버튼 눌렀을시 새로고침해서 버튼 텍스트 바꿈
                        if (GU.equals("RUN_INSERT")) {
                            BaseAlert.show2(mContext, "", "출하완료 되었습니다", (dialog1, which) -> requestRCM_Read(), (dialog1, which) -> dialog1.dismiss());
                        } else if (GU.equals("RUN_DELETE")) {
                            BaseAlert.show2(mContext, "", "출하취소 되었습니다", (dialog1, which) -> requestRCM_Read(), (dialog1, which) -> dialog1.dismiss());
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
    }*/
}
