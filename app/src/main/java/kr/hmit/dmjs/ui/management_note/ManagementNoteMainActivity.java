package kr.hmit.dmjs.ui.management_note;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityManagementNoteBinding;
import kr.hmit.dmjs.model.response.LED_Model;

import kr.hmit.dmjs.model.vo.LED_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.management_note.adapter.ManagementNoteListAdapter;
import kr.hmit.dmjs.ui.management_note.model.FilterVO;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ManagementNoteMainActivity extends BaseActivity {


    private ActivityManagementNoteBinding binding;

    private ManagementNoteListAdapter mAdapter;
    private ArrayList<LED_VO> mListTotal;
    private ArrayList<LED_VO> mListSearch;

    private FilterVO filterVO;
    private Calendar mCalRequest1;
    private Calendar mCalRequest2;
    private SimpleDateFormat sdfFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManagementNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();

    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        binding.imgSearch.setOnClickListener(this::onClickSearch);
        binding.imgWriteWork.setOnClickListener(this::onClickGoWriteOrder);
        binding.imgFilter.setOnClickListener(this::onClickGoFilter);


        binding.etSearch.setOnEditorActionListener((v, actionId, event) -> {
            switch (actionId) {
                case EditorInfo.IME_ACTION_SEARCH:
                    onClickSearch(v);
                    break;
                default:
                    return false;
            }

            return true;
        });


    }




    @Override
    protected void initialize() {

        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");

        mCalRequest1 = Calendar.getInstance();
        mCalRequest1.add(Calendar.DATE, -30);
        mCalRequest2 = Calendar.getInstance();
        mCalRequest2.add(Calendar.DATE, 1);

        filterVO = new FilterVO("", "",sdfFormat.format(mCalRequest1.getTime()),sdfFormat.format(mCalRequest2.getTime()));

        mListTotal = new ArrayList<>();
        mListSearch = new ArrayList<>();

        mAdapter = new ManagementNoteListAdapter(mContext, mListTotal);

        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

        requestLED_Read();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 목록 조회
     */
    private void requestLED_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.led(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).MMO_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "LIST",
                mUser.Value.MEM_CID,
                "MMO",
                filterVO.LED_04,
                filterVO.LED_11,
                filterVO.LED_1301,
                filterVO.LED_1302

        ).enqueue(new Callback<LED_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<LED_Model> call, Response<LED_Model> response) {
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
                            Response<LED_Model> response = (Response<LED_Model>) msg.obj;
                            if (response.isSuccessful()) {
                                if (response.body().Total > 0) {
                                    if (response.body().Data.get(0).Validation) {
                                        bindingData(response.body().Data);
                                    } else {
                                        BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                } else {
                                    bindingData(new ArrayList<LED_VO>());
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
            public void onFailure(Call<LED_Model> call, Throwable t) {
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
    private void bindingData(ArrayList<LED_VO> data) {
        mListTotal = data;

        mAdapter.update(mListTotal);

        binding.imgSearch.performClick();
    }


    private void onClickSearch(View view) {
        String strSearch = binding.etSearch.getText().toString().toUpperCase().trim();

        mListSearch.clear();

        for (int i = 0; i < mListTotal.size(); i++) {
            LED_VO vo = mListTotal.get(i);

            if (vo.LED_04.toUpperCase().contains(strSearch)) {
                mListSearch.add(vo);
            }
        }

        mAdapter.update(mListSearch);
    }






    private void onItemClickGoInfo(View view, int position) {
        Intent intent = new Intent(mContext, ManagementNoteDetailActivity.class);
        intent.putExtra("ledVO", mListSearch.get(position));
        mActivity.startActivityForResult(intent, ManagementNoteDetailActivity.REQUEST_CODE);
    }

    private void onClickGoWriteOrder(View v) {
        Intent intent = new Intent(mContext, ManagementNoteAddActivity.class);
        intent.putExtra("ledVO", mListSearch);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, ManagementNoteAddActivity.REQUEST_CODE);

    }

    private void onClickGoFilter(View v) {
        Intent intent = new Intent(mContext, ManagementNoteFilterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("filterData", filterVO);
        mActivity.startActivityForResult(intent, ManagementNoteFilterActivity.REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == ManagementNoteAddActivity.REQUEST_CODE || requestCode == ManagementNoteDetailActivity.REQUEST_CODE) && resultCode == RESULT_OK) {
            requestLED_Read();
        } else if(requestCode == ManagementNoteFilterActivity.REQUEST_CODE && resultCode == RESULT_OK){
                filterVO = (kr.hmit.dmjs.ui.management_note.model.FilterVO) data.getSerializableExtra(ManagementNoteFilterActivity.CATEGORY_INFO);
                requestLED_Read();
            }

        }

    }

