package kr.hmit.dmjs.ui.m005;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityLot04MainBinding;
import kr.hmit.dmjs.model.vo.GAG_VO;
import kr.hmit.dmjs.model.vo.LOT_VO;
import kr.hmit.dmjs.network.Http;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Lot04MainActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8023;
    public static final String GAG_INFO = "LOT_04_REAL";

    private ActivityLot04MainBinding binding;

    private GAG_VO gag_vo = new GAG_VO();

    private ArrayList<GAG_VO> mList = new ArrayList<>();

    //파라미터
    private HashMap<String, Object> paramMap = new HashMap<String, Object>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLot04MainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
        initLayout();
    }

    @Override
    protected void initialize() {
        binding.imgBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void initLayout() {
        GAG_Read();
    }
    private void GAG_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        //기본파라미터
        paramMap = setParamMap("GAG_ID", "M_REAL_LIST_LOT");
        openLoadingBar();

        Http.gag(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).GAG_Read(
                BaseConst.URL_HOST,
                paramMap
        ).enqueue(new Callback<ArrayList<GAG_VO>>() {
            @Override
            public void onResponse(Call<ArrayList<GAG_VO>> call, Response<ArrayList<GAG_VO>> response) {
                closeLoadingBar();
                ArrayList<GAG_VO> data = response.body();

                if (response.isSuccessful()) {
                    if(data.size() > 0){
                        bindingData(data);
                    }else{
                        BaseAlert.show(mContext,"실시간 생산정보가 없습니다." );
                    }
                } else {
                    BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<GAG_VO>> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
                t.printStackTrace();
            }
        });
    }

    private void bindingData(ArrayList<GAG_VO> data) {
        if(data != null && data.size() > 0){
            gag_vo = (GAG_VO) data.get(0);

            binding.gag10Nm.setText(gag_vo.GAG_10_NM);
            binding.gag03Nm.setText(gag_vo.GAG_03_NM);
            binding.dah14.setText(gag_vo.DAH_14);
            binding.lot11.setText(String.valueOf(gag_vo.LOT_11));
            binding.lot06.setText(String.valueOf(gag_vo.LOT_06));
            binding.gag11.setText(String.valueOf(gag_vo.GAG_11));
            binding.gag11Rate.setText(String.valueOf(gag_vo.GAG_11_RATE));
            binding.gag53.setText(String.valueOf(gag_vo.GAG_53));
            binding.gag5152.setText(gag_vo.GAG_5152);

        }
    }
}
