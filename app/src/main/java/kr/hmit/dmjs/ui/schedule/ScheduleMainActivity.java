 package kr.hmit.dmjs.ui.schedule;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import kr.hmit.dmjs.BuildConfig;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityScheduleMainBinding;
import kr.hmit.dmjs.model.response.CALC_Model;
import kr.hmit.dmjs.model.vo.CALC_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.schedule.adapter.ScheduleListAdapter;
import kr.hmit.dmjs.ui.schedule.view.ScheduleCalendarView;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleMainActivity extends BaseActivity {
    //=====================================
    // region // static, final
    //=====================================

    //=====================================
    // endregion // static, final
    //=====================================


    //=====================================
    // region // view
    //=====================================

    private ActivityScheduleMainBinding binding;

    //=====================================
    // endregion // view
    //=====================================


    //=====================================
    // region // variable
    //=====================================

    private ScheduleListAdapter mAdapter;
    private HashMap<String, ArrayList<CALC_VO>> mScheduleByDay;
    private ArrayList<String> mListExistDate;
    private ArrayList<CALC_VO> mList;

    //=====================================
    // endregion // variable
    //=====================================


    //=====================================
    // region // initialize
    //=====================================


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScheduleMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();

        initialize();
    }


    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.imgWriteWork.setOnClickListener(this::onClickGoWriteWork);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        binding.calendarView.setOnDayClickListener(new ScheduleCalendarView.OnDayClickListener() {
            @Override
            public void onClick(String date) {
                bindingDate(binding.calendarView.getSelectedDate());
            }

            @Override
            public void onChangedMonth() {
                requestCALC_Read(binding.calendarView.getCurrentMonth());
                mAdapter.update(new ArrayList<>());
            }
        });


/*        if(mSettings.Value.ScheduleBKM){
            binding.imgBookMark.setSelected(true);
        }else{
            binding.imgBookMark.setSelected(false);
        }
        binding.imgBookMark.setOnClickListener(this::onClickBookMark);*/

    }


    @Override
    protected void initialize() {
        mAdapter = new ScheduleListAdapter(mContext);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

        // 날짜 초기화
        // todo api 호출 후 데이터 받도록 변경
        if (BuildConfig.DEBUG) {
//            String data = "{\"Data\":[{\"CALC_ID\":\"HUMAN\",\"CALC_01\":331,\"CALC_02\":\"20201207\",\"CALC_03\":\"13:00 휴먼화상회의\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":332,\"CALC_02\":\"20201214\",\"CALC_03\":\"13:00 휴먼화상회의\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":333,\"CALC_02\":\"20201221\",\"CALC_03\":\"13:00 휴먼화상회의\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":334,\"CALC_02\":\"20201228\",\"CALC_03\":\"13:00 휴먼화상회의\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":496,\"CALC_02\":\"20201201\",\"CALC_03\":\"14:00 고성민 삼영비앤에프 현장컨설팅\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":499,\"CALC_02\":\"20201202\",\"CALC_03\":\"10:00 고성민 도담 현장워크샵\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":506,\"CALC_02\":\"20201216\",\"CALC_03\":\"10:00 고성민 남도식품 중간보고\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":512,\"CALC_02\":\"20201201\",\"CALC_03\":\"15:00 목포관광플랫폼 제안접수\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":513,\"CALC_02\":\"20201210\",\"CALC_03\":\"??:00 목포관광플랫폼 제안발표\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":514,\"CALC_02\":\"20201209\",\"CALC_03\":\"09:30 김유미 KDN 3개 완료보고\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":517,\"CALC_02\":\"20201203\",\"CALC_03\":\"10:00 고성민 아라 미팅\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":518,\"CALC_02\":\"20201208\",\"CALC_03\":\"14:00 고성민 삼영비앤에프 중간보고\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":522,\"CALC_02\":\"20201207\",\"CALC_03\":\"김태현 연차\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":525,\"CALC_02\":\"20201202\",\"CALC_03\":\"10:00 유대성 성지에프앤디 PLC 테스트\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":526,\"CALC_02\":\"20201201\",\"CALC_03\":\"15:00 박주명 해원 현장컨설팅\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":528,\"CALC_02\":\"20201202\",\"CALC_03\":\"14:00 박주명 향아식품\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":529,\"CALC_02\":\"20201202\",\"CALC_03\":\"10:00 박주명 유일\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":530,\"CALC_02\":\"20201203\",\"CALC_03\":\"10:00 박주명 윈가람\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":531,\"CALC_02\":\"20201201\",\"CALC_03\":\"10:00 서종원 승진엔지니어링 네트워크 공사\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":532,\"CALC_02\":\"20201202\",\"CALC_03\":\"15:00 서종원 남도식품 현장 사전답사\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":533,\"CALC_02\":\"20201203\",\"CALC_03\":\"09:00 서종원 삼영비앤에프 네트워크 설치공사\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":534,\"CALC_02\":\"20201209\",\"CALC_03\":\"10:00 김유미 KPS 2개사 중간보고\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":535,\"CALC_02\":\"20201209\",\"CALC_03\":\"11:00 김유미 KDN 2개 완료보고\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":536,\"CALC_02\":\"20201203\",\"CALC_03\":\"11:00 휴먼 화상회의 (4팀)\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":537,\"CALC_02\":\"20201202\",\"CALC_03\":\"13:00 고성민 승진엔지니어링 미팅\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":538,\"CALC_02\":\"20201204\",\"CALC_03\":\"14:00 고성민 아라 미팅\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":539,\"CALC_02\":\"20201224\",\"CALC_03\":\"신상철 연차\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":540,\"CALC_02\":\"20201210\",\"CALC_03\":\"14:00 김유미 KDN이음테크, KPS그린팜 완료보고\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":541,\"CALC_02\":\"20201222\",\"CALC_03\":\"10:00 고성민 도담 중간보고\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":542,\"CALC_02\":\"20201204\",\"CALC_03\":\"09:00 서종원 이엠푸드, 자연숨결그대로, 블랙푸드 iot센서설치\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":543,\"CALC_02\":\"20201210\",\"CALC_03\":\"17:00 김유미 KPS 그린팜 완료보고\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":544,\"CALC_02\":\"20201204\",\"CALC_03\":\"박서진 목포출장(근대역사관관련회의)\",\"CALC_04\":\"N\",\"Validation\":\"true\"},{\"CALC_ID\":\"HUMAN\",\"CALC_01\":545,\"CALC_02\":\"20201204\",\"CALC_03\":\"15:00 박주명 골드판넬 미팅\",\"CALC_04\":\"N\",\"Validation\":\"true\"}],\"Total\":33,\"AggregateResults\":null,\"Errors\":null}";
//            mList = new Gson().fromJson(data, CalcModel.class);
//
//            parsingSchedule(mList.Data);
//
//            bindingDate(binding.calendarView.getSelectedDate());
        }

        requestCALC_Read(binding.calendarView.getCurrentMonth());
    }

    /**
     * 일정관리 리스트를 가져온다.
     */
    private void requestCALC_Read(String date) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.cal(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).CALC_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "LIST",
                mUser.Value.MEM_CID,
                date
        ).enqueue(new Callback<CALC_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<CALC_Model> call, Response<CALC_Model> response) {
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

                            Response<CALC_Model> response = (Response<CALC_Model>) msg.obj;
                             if (response.isSuccessful()) {
                                if (response.body().Total > 0) {
                                    if (response.body().Data.get(0).Validation) {
                                        parsingSchedule(response.body().Data);
                                        bindingDate(binding.calendarView.getSelectedDate());
                                    } else {
                                        BaseAlert.show(mContext, getString(R.string.network_error_2));
                                    }
                                }else{
                                    parsingSchedule(new ArrayList<CALC_VO>());
                                    bindingDate(binding.calendarView.getSelectedDate());
                                }
                            } else {
                                BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<CALC_Model> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);
            }
        });
    }

    /**
     * 날짜 바인딩한다.
     */
    private void bindingDate(String selectedDate) {
        ArrayList<CALC_VO> temp = mScheduleByDay.get(selectedDate);
        mAdapter.update(temp);

        if (temp == null || temp.size() == 0) {
            binding.tvNoData.setVisibility(View.VISIBLE);
        } else {
            binding.tvNoData.setVisibility(View.GONE);
        }
    }

    /**
     * 날짜별로 파싱한다.
     *
     * @param data
     */
    private void parsingSchedule(ArrayList<CALC_VO> data) {
        Collections.sort(data, new CALC_VO.AscendingDate());
        mScheduleByDay = new HashMap<>();
        mListExistDate = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            CALC_VO vo = data.get(i);

            ArrayList<CALC_VO> temp = mScheduleByDay.get(vo.CALC_02);

            if (temp != null) {
                temp.add(vo);
            } else {
                mListExistDate.add(vo.CALC_02);
                temp = new ArrayList<>();
                temp.add(vo);
                mScheduleByDay.put(vo.CALC_02, temp);
            }
        }
        binding.calendarView.setData(mListExistDate);
    }


    //=====================================
    // endregion // initialize
    //=====================================

    //============================
    // region // event
    //============================
   /* private void onClickBookMark(View v) {
        if(mSettings.Value.ScheduleBKM){

            mSettings.Value.ScheduleBKM=false;
            mSettings.Value.menuList.remove("01");
        }else{

            mSettings.Value.ScheduleBKM=true;
            mSettings.Value.menuList.add("01");
        }

       // binding.imgBookMark.setSelected(mSettings.Value.ScheduleBKM);
    }*/

    private void onItemClickGoInfo(View view, int position, CALC_VO vo) {
        Intent intent = new Intent(mContext, ScheduleDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("CALCData",vo);
        mActivity.startActivityForResult(intent, ScheduleDetailActivity.REQUEST_CODE);

    }

    private void onClickGoWriteWork(View v) {
        Intent intent = new Intent(mContext, ScheduleAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, ScheduleAddActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == ScheduleDetailActivity.REQUEST_CODE||requestCode == ScheduleAddActivity.REQUEST_CODE) && resultCode == RESULT_OK) {
            requestCALC_Read(binding.calendarView.getCurrentMonth());
        }

    }
    //============================
    // endregion // event
    //============================
}
