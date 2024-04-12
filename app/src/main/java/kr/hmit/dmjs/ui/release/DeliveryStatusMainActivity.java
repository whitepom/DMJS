package kr.hmit.dmjs.ui.release;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityDeliveryStatusMainBinding;
import kr.hmit.dmjs.databinding.ActivityReleasecurrentMain2Binding;
import kr.hmit.dmjs.model.response.RUN_Model;
import kr.hmit.dmjs.model.vo.RUN_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.release.adapter.DeliveryStatusListAdapter;
import kr.hmit.dmjs.ui.release.model.DeliveryStatusFilterVO;
import kr.hmit.dmjs.ui.release.model.FilterVO;
import kr.hmit.dmjs.ui.main.MainDashboardActivity;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DeliveryStatusMainActivity extends BaseActivity {

    private ActivityReleasecurrentMain2Binding binding;

    private DeliveryStatusListAdapter mAdapter;
    private ArrayList<RUN_VO> mListTotal;
    private ArrayList<RUN_VO> mListSearch;
    private DeliveryStatusFilterVO filterVO;

    private Calendar mCalRequest1;
    private Calendar mCalRequest2;
    private SimpleDateFormat sdfFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReleasecurrentMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
     //   binding.imgWrite.setOnClickListener(this::onClickGoWriteWork);
        binding.imgFilter.setOnClickListener(this::onClickGoFilter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        binding.etSearch.setOnEditorActionListener(this::onSearch);
        binding.imgSearch.setOnClickListener(this::onClickSearch);

        binding.imgGoTop.setOnClickListener(this::onClickGoTop);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest1 = Calendar.getInstance();
        mCalRequest1.add(Calendar.MONTH,0);
        mCalRequest2 = Calendar.getInstance();
        mCalRequest2.add(Calendar.MONTH,-1);
        filterVO =new DeliveryStatusFilterVO("",sdfFormat.format(mCalRequest2.getTime()),sdfFormat.format(mCalRequest1.getTime()));

        mListTotal = new ArrayList<>();
        mListSearch = new ArrayList<>();

        mAdapter = new DeliveryStatusListAdapter(mContext,mListTotal);
      //  mAdapter.setOnItemClickListener(this::onItemClickGoInfo);

        binding.recyclerView.setAdapter(mAdapter);

        requestRUN_Read();

    }

    @Override
    protected void onRestart() {

        super.onRestart();
        binding.etSearch.setText("");
        binding.imgSearch.performClick();
    }
    private void onClickGoHome(View v){
        Intent intent = new Intent(this, MainDashboardActivity.class);
        startActivity(intent);
    }
    private void onClickGoTop(View v) {
        binding.recyclerView.smoothScrollToPosition(0);
    }
    /**
     * 목록 조회
     */
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
                "M_RUN_LIST",
                mUser.Value.MEM_CID,
                filterVO.RUN_03,
                filterVO.RUN_02_ST, //수주일자시작일 filterVO.REQ_02_ST
                filterVO.RUN_02_ED //수주일자종료일   filterVO.REQ_02_ED
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
                                if(response.body().Total>0){
                                    bindingData(response.body().Data);
                                }else{
                                    bindingData(new  ArrayList<RUN_VO>());
                                    toast("검색결과가 없습니다.");
                                    BaseAlert.show(mContext,"검색결과가 없습니다." );
                                }
                            } else {
                                BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 서버 데이터 바인딩
     *
     * @param data
     */
    private void bindingData(ArrayList<RUN_VO> data) {
        mListTotal = data;

        mAdapter.update(mListTotal);
        binding.imgSearch.performClick();
    }

    private boolean onSearch(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            onClickSearch(v);
        } else {
            return false;
        }

        return true;
    }

    private void onClickSearch(View view) {
        String strSearch = binding.etSearch.getText().toString().toUpperCase().trim();

        mListSearch.clear();
        for (int i = 0; i < mListTotal.size(); i++) {
            RUN_VO vo = mListTotal.get(i);

            if (vo.RUN_03.toUpperCase().contains(strSearch)
                || vo.CLT_02.toUpperCase().contains(strSearch)) {
                mListSearch.add(vo);
            }
        }
        mAdapter.update(mListSearch);
    }


  /*  private void onItemClickGoInfo(View view, int position) {
        Intent intent = new Intent(mContext, JsDeliveryStatusMain2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("js_runVO", mListTotal.get(position));
        mActivity.startActivityForResult(intent, JsDeliveryStatusMain2Activity.REQUEST_CODE);
    }
    private void onClickGoWriteWork(View v) {
        Intent intent = new Intent(mContext, JsDeliveryStatusAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, JsDeliveryStatusAddActivity.REQUEST_CODE);

    }*/
    private void onClickGoFilter(View v) {
        Intent intent = new Intent(mContext, DeliveryStatusFilterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("filterData", filterVO);
        mActivity.startActivityForResult(intent, DeliveryStatusFilterActivity.REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == DeliveryStatusFilterActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            filterVO = (DeliveryStatusFilterVO) data.getSerializableExtra(DeliveryStatusFilterActivity.CATEGORY_INFO);
            requestRUN_Read();
        }
    }
}
