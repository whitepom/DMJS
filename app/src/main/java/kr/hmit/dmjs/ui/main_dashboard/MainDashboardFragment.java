package kr.hmit.dmjs.ui.main_dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.FragmentMainDashboardBinding;
import kr.hmit.dmjs.model.response.BRD_Model;
import kr.hmit.dmjs.model.response.GEM_Model;
import kr.hmit.dmjs.model.response.ODD_Model;
import kr.hmit.dmjs.model.response.RUN_Model;
import kr.hmit.dmjs.model.response.UNO_Model;
import kr.hmit.dmjs.model.response.WKS_Model;
import kr.hmit.dmjs.model.vo.CALC_VO;
import kr.hmit.dmjs.model.vo.GEM_VO;
import kr.hmit.dmjs.model.vo.ODD_VO;
import kr.hmit.dmjs.model.vo.RUN_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.main_dashboard.adapter.MainDashboardReceiveListAdapter;
import kr.hmit.dmjs.ui.main_dashboard.adapter.MainDashboardNoteListAdapter;
import kr.hmit.dmjs.ui.main_dashboard.adapter.MainDashboardCurrentReceiveListAdapter;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.base_fragment.BaseFragment;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.base.settings.InterfaceSettings;
import kr.hmit.base.user_interface.InterfaceUser;
import kr.hmit.dmjs.ui.main_dashboard.adapter.MainDashboardPaymentListAdapter;
import kr.hmit.dmjs.ui.main_dashboard.adapter.MainDashboardReleaseListAdapter;
import kr.hmit.dmjs.ui.main_dashboard.adapter.MainDashboardWorkmanageListAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainDashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class MainDashboardFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentMainDashboardBinding binding;
    protected InterfaceSettings mSettings;
    protected InterfaceUser mUser;

    private MainDashboardReceiveListAdapter mAdapterReceive;
    private MainDashboardCurrentReceiveListAdapter mAdapterCurrent;
    private MainDashboardReleaseListAdapter mAdapterRelease; // 출고

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private HashMap<String, ArrayList<CALC_VO>> mScheduleByDay;
    private ArrayList<String> mListExistDate;
    private Calendar mCalRequest1;
    private Calendar mCalRequest2;
    private SimpleDateFormat sdfFormat;

    public MainDashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainDashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainDashboardFragment newInstance(String param1, String param2) {
        MainDashboardFragment fragment = new MainDashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMainDashboardBinding.inflate(getLayoutInflater(), container, false);


        if (mSettings == null)
            mSettings = InterfaceSettings.getInstance(getContext());
        if (mUser == null)
            mUser = InterfaceUser.getInstance();



        initLayout();
        initialize();


        return binding.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();
        requestODD_Read();
        requestGEM_Read();


    }
    private void initLayout() {

        binding.recyclerViewIn.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        mAdapterReceive = new MainDashboardReceiveListAdapter(getContext(),new ArrayList<ODD_VO>());
        binding.recyclerViewIn.setAdapter(mAdapterReceive);

        binding.recyclerViewCurrent.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        mAdapterCurrent = new MainDashboardCurrentReceiveListAdapter(getContext(),new ArrayList<GEM_VO>());
        binding.recyclerViewCurrent.setAdapter(mAdapterCurrent);

        binding.recyclerViewOrder.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        mAdapterRelease = new MainDashboardReleaseListAdapter(getContext(),new ArrayList<RUN_VO>());
        binding.recyclerViewOrder.setAdapter(mAdapterRelease);




    }


    private void initialize() {
        requestODD_Read();
        requestGEM_Read();
        requestRUN_Read();

    }

    /**
     * 일정관리 리스트를 가져온다.
     */



    private void requestODD_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }


        Http.odd(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).ODD_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST2_MAIN",
                mUser.Value.MEM_CID,
                "",
                "",
                ""
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

                            Response<ODD_Model> response = (Response<ODD_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if (response.body().Data.get(0).Validation) {
                                        mAdapterReceive.update(response.body().Data);
                                        binding.tvTextNoReceive.setVisibility(View.INVISIBLE);
                                    } else {
                                        //BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }

                                }else{

                                }
                            } else {
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<ODD_Model> call, Throwable t) {
            }
        });
    }
    private void requestRUN_Read() {
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
                "M_LIST_MAIN",
                mUser.Value.MEM_CID,
                "",
                "",
                ""

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
                                if (response.body().Total > 0) {
                                    if (response.body().Data.get(0).Validation) {
                                        mAdapterRelease.update(response.body().Data);
                                        binding.tvTextNoRelease.setVisibility(View.INVISIBLE);
                                    } else {
                                        //BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }

                                } else {

                                }

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
    private void requestGEM_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }


        Http.gem(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).GEM_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST_MAIN",
                "DMJS",
                "",
                "",
                "", //품명
                ""  // 입고처

        ).enqueue(new Callback<GEM_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<GEM_Model> call, Response<GEM_Model> response) {
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

                            Response<GEM_Model> response = (Response<GEM_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if (response.body().Data.get(0).Validation) {
                                        mAdapterCurrent.update(response.body().Data);
                                        binding.tvTextReceiveCurrent2 .setVisibility(View.INVISIBLE);

                                    } else {
                                        //BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }

                                }else{

                                }
                            } else {
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<GEM_Model> call, Throwable t) {
            }
        });
    }


}
