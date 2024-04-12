package kr.hmit.dmjs.ui.main_dashboard.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.Fragment4ColumnBinding;
import kr.hmit.dmjs.model.response.MAIN_Model;
import kr.hmit.dmjs.model.vo.MAIN_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.main_dashboard.adapter.MainDashboardAgendaListAdapter;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.base_fragment.BaseFragment;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.base.settings.InterfaceSettings;
import kr.hmit.base.user_interface.InterfaceUser;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryStatus extends BaseFragment {
    private Fragment4ColumnBinding binding;
    private MainDashboardAgendaListAdapter mAdapter;

    protected InterfaceSettings mSettings;
    protected InterfaceUser mUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = Fragment4ColumnBinding.inflate(getLayoutInflater(), container, false);

        initLayout();
        initialize();

        return binding.getRoot();
    }

    private void initLayout() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

        binding.tvcolumn1.setText("출고일자");
        binding.tvcolumn2.setText("택배사");
        binding.tvcolumn3.setText("미발송건수");

    }
    private void initialize() {
        if (mSettings == null)
            mSettings = InterfaceSettings.getInstance(getContext());
        if (mUser == null)
            mUser = InterfaceUser.getInstance();


        mAdapter = new MainDashboardAgendaListAdapter(getContext(),new ArrayList<MAIN_VO>());
        binding.recyclerView.setAdapter(mAdapter);
        requestMAIN_Read();
    }


    private void requestMAIN_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }


        Http.main(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).MAIN_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LGCLIM_LIST",
                mUser.Value.MEM_CID
        ).enqueue(new Callback<MAIN_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<MAIN_Model> call, Response<MAIN_Model> response) {
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

                            Response<MAIN_Model> response = (Response<MAIN_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    bindingData(response.body().Data);
                                }else{
                                    bindingData(new  ArrayList<MAIN_VO>());
                                }
                            } else {
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<MAIN_Model> call, Throwable t) {
            }
        });
    }
    private void bindingData(ArrayList<MAIN_VO> data) {
        ArrayList<MAIN_VO> mListTotal = data;

        mAdapter.update(mListTotal);

    }
}
