package kr.hmit.dmjs.ui.release;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.base.settings.SettingsKey;
import kr.hmit.dmjs.BuildConfig;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityReleaseMainBinding;
import kr.hmit.dmjs.model.response.REQ_Model;
import kr.hmit.dmjs.model.response.RUN_Model;
import kr.hmit.dmjs.model.vo.REQ_VO;

import kr.hmit.dmjs.model.vo.RUN_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.login.LoginActivity;
import kr.hmit.dmjs.ui.receive.ReceiveDetailActivity;
import kr.hmit.dmjs.ui.release.adapter.ReleaseListAdapter;
import kr.hmit.dmjs.ui.release.model.FilterVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReleaseMainActivity extends BaseActivity {
    //=============================
    // region // view
    //=============================

    private ActivityReleaseMainBinding binding;

    //=============================
    // endregion // view
    //=============================

    //=============================
    // region // variable
    //=============================

    private ReleaseListAdapter mAdapter;
    private ArrayList<REQ_VO> mListTotal;
    private ArrayList<REQ_VO> mListSearch;
    private ArrayList<REQ_VO> mListcheck;
    private String[] mCategory1;
    private String[] mLogisticsList;
    private String[] mLogisticsCodeList;
    private DatePickerDialog datePickerDialog;
    private String lgcCode="";
    private FilterVO filterVO;
    private boolean flag = true;
    //=============================
    // endregion // variable
    //=============================
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat;

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            binding.RUN02.setText(sdfFormat.format(mCalRequest.getTime()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReleaseMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();

        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        binding.imgGoTop.setOnClickListener(this::onClickGoTop);
        binding.imgSearch.setOnClickListener(this::onClickSearch);
        binding.etSearch.setOnEditorActionListener(this::onSearch);
        binding.imgFilter.setOnClickListener(this::onClickGoFilter);
        binding.RUN02.setOnClickListener(this::onClickReleaseDate);
     //   binding.tvLogistics.setOnClickListener(this::onClickCategory1);
        binding.btnAllRelease.setOnClickListener(this::onClickGoRelease);

      /*  if(mSettings.Value.ReleaseBKM){
            binding.imgBookMark.setSelected(true);
        }else{
            binding.imgBookMark.setSelected(false);
        }
        binding.imgBookMark.setOnClickListener(this::onClickBookMark);
*/
    }

    @Override
    protected void initialize() {
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
        binding.RUN02.setText(sdfFormat.format(mCalRequest.getTime()));

        filterVO = new FilterVO("","");


        mListTotal = new ArrayList<>();
        mListSearch = new ArrayList<>();
        if (BuildConfig.DEBUG) {
        }

        mAdapter = new ReleaseListAdapter(mContext, mListTotal);
        binding.recyclerView.setAdapter(mAdapter);

        requestREQ_Read();

    }

    private void onClickReleaseDate(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
  /*  private void onClickCategory1(View v) {
        dropdownCategory("택배사",mLogisticsList,binding.tvLogistics);
    }*/
    private void dropdownCategory(String dialogTitle, String[] category, TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(dialogTitle)
                .setItems(category, (dialog, which) -> {
                    textView.setText(category[which]);
                    lgcCode=mLogisticsCodeList[which];
                }).setCancelable(false).create();

        builder.show();
    }
    private void onClickGoRelease(View view) {
        mListcheck=mAdapter.getCheckList();
        if(mListcheck.size()>0){
            boolean tempBool = true;
            for(int i =0; i<mListcheck.size();i++){
                int REQ_6004=Integer.parseInt(mListcheck.get(i).REQ_6004);
                int REQ_6004tmp=Integer.parseInt(mListcheck.get(i).REQ_6004N);
                int REQ_06=Integer.parseInt(mListcheck.get(i).REQ_06);
                if(REQ_6004+REQ_6004tmp>REQ_06){
                    tempBool=false;
                    break;
                }}
            if(tempBool){
               for(int i=0;i<mListcheck.size();i++){
                if(i==(mListcheck.size()-1)){
                    RUN_VO newVO = new RUN_VO();

                    requestSaveRun(mListcheck.get(i),true);
                }else{
                    requestSaveRun(mListcheck.get(i),false);
                }}
        }else{
            toast("기 출고수량을 넘을 수 없습니다.");
        }}else{
            toast("출고할 목록이 없습니다.");
        }

    }

    public void requestSaveRun(REQ_VO vo, boolean refreshFlag) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }
        openLoadingBar();
        Http.run(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).RUN_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_INSERT",
                "DMJS",
                "",
                binding.RUN02.getText().toString().replaceAll("-",""),
                vo.REQ_03,
                vo.REQ_04,
                Double.parseDouble(vo.REQ_05),
                Double.parseDouble(vo.REQ_6004N),
               (double) Double.parseDouble(vo.REQ_05)*Double.parseDouble(vo.REQ_6004N),
               "D",//직구분D, 도매:W, H:홈쇼핑
                vo.REQ_01,
               "",
               "",
                vo.REQ_97,
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
                        if(refreshFlag){
                            toast("출고처리 되었습니다.");
                            requestREQ_Read();
                        }
                    }

                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                call.cancel();
                closeLoadingBar();
                if(refreshFlag){
                    toast("출고처리 되었습니다.");
                    requestREQ_Read();
                }
            }

        });
    }


    private void requestREQ_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.req(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).REQ_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_RED_LIST",
                mUser.Value.MEM_CID,
                filterVO.RUN_02_ST,
                filterVO.RUN_02_ED,
                filterVO.CLT_01,
                filterVO.DAH_01,
                ""
        ).enqueue(new Callback<REQ_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<REQ_Model> call, Response<REQ_Model> response) {
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

                            Response<REQ_Model> response = (Response< REQ_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                if(response.body().Total>0){
                                    if (response.body().Data.get(0).Validation) {
                                        bindingData(response.body().Data);
                                    } else {
                                        toast("동일계정 접속 > 로그인 페이지로 이동합니다");  // asdfasdfasdf
                                        mSettings.putBooleanItem(SettingsKey.AutoLogin,false);
                                        Intent intent = new Intent(mContext, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("simultaneous_connect",mUser.Value.MEM_01.toString());
                                        startActivity(intent);
                                        finish();
                                       // BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                }else{
                                    bindingData(new  ArrayList<REQ_VO>());
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
            public void onFailure(Call<REQ_Model> call, Throwable t) {
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
    private void bindingData(ArrayList<REQ_VO> data) {
        mListTotal = data;

        mListSearch.clear();
        for (int i = 0; i < mListTotal.size(); i++) {
            mListSearch.add(mListTotal.get(i));
        }
        mAdapter.update(mListTotal);

      /*  if(filterVO.REQ_03.isEmpty()){
            String[] tempList1= new String[data.size()];

            for (int i = 0; i < data.size(); i++) {
                tempList1[i] = data.get(i).REQ_03_NM;
            }

            tempList1 = new HashSet<String>(Arrays.asList(tempList1)).toArray(new String[0]);

            mCategory1 = new String[tempList1.length + 1];

            mCategory1[0] = "전체";

            for (int i = 0; i < tempList1.length; i++) {
                mCategory1[i + 1] = tempList1[i];
            }
            filterVO.mCategory1=mCategory1;}
       */
    }


    private boolean onSearch(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            onClickSearch(v);
        } else {
            return false;
        }

        return true;
    }

    /**
     * 상단으로 올린다.
     *
     * @param v
     */
    private void onClickGoTop(View v) {
        binding.recyclerView.smoothScrollToPosition(0);
    }

    /**
     * 검색 버튼 클릭 시
     *
     * @param v
     */
    private void onClickSearch(View v) {
        String strSearch = binding.etSearch.getText().toString().toUpperCase().trim();

        if (strSearch.isEmpty()) {
            mAdapter.update(mListTotal);
            return;
        }

        mListSearch.clear();

        for (int i = 0; i < mListTotal.size(); i++) {
            REQ_VO vo = mListTotal.get(i);

            if (vo.REQ_01.toUpperCase().contains(strSearch)
                    || vo.DAH_02.toUpperCase().contains(strSearch)
                    || vo.REQ_04.toUpperCase().contains(strSearch)
                    ||vo.REQ_03_NM.toUpperCase().contains(strSearch)
                   ) {
                mListSearch.add(vo);
            }
        }

        mAdapter.update(mListSearch);
    }
    private void onClickGoFilter(View v) {
        Intent intent = new Intent(mContext, ReleaseFilterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("filterData", filterVO);
        mActivity.startActivityForResult(intent, ReleaseFilterActivity.REQUEST_CODE);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ReleaseFilterActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            filterVO = (FilterVO) data.getSerializableExtra(ReleaseFilterActivity.CATEGORY_INFO);
            requestREQ_Read();
        }else if ((requestCode == ReleaseFilterActivity.REQUEST_CODE||requestCode == ReceiveDetailActivity.REQUEST_CODE) && resultCode == RESULT_OK) {
            requestREQ_Read();
        }


    }


}