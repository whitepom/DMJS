package kr.hmit.dmjs.ui.release;
import kr.hmit.base.base_activity.BaseActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kr.hmit.base.settings.SettingsKey;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityReleaseInfoMainBinding;
import kr.hmit.dmjs.model.response.RUN_Model;
import kr.hmit.dmjs.model.vo.RUN_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.login.LoginActivity;
import kr.hmit.dmjs.ui.release.adapter.ReleaseInfoListAdapter;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.ui.release.model.FilterVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ReleaseInfoMain extends BaseActivity {
    public static final int REQUEST_CODE = 8844;

    private ActivityReleaseInfoMainBinding binding;

    private ReleaseInfoListAdapter mAdapter;
    private ArrayList<RUN_VO> mListTotal;
    private Calendar mCalRequest;
    private Calendar mCalRequest2;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog datePickerDialog2;
    private RUN_VO runVO;
    private FilterVO filterVO;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            binding.RUN02ST.setText(sdfFormat.format(mCalRequest.getTime()));
            requestRUN_Read();
        }
    };
    private DatePickerDialog.OnDateSetListener datePickerListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest2.set(Calendar.YEAR, year);
            mCalRequest2.set(Calendar.MONTH, month);
            mCalRequest2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            binding.RUN02ED.setText(sdfFormat.format(mCalRequest2.getTime()));
            requestRUN_Read();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReleaseInfoMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        runVO = (RUN_VO) intent.getExtras().get("runVO");
        filterVO = (FilterVO) intent.getExtras().get("filterVO");
        initLayout();
        initialize();
    }
    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.imgAdd.setOnClickListener(this::onClickNew);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        binding.tvReceiverName.setText(runVO.CLT_02);
        binding.tvReceiveDate.setText(runVO.RUN_02); // 출고일
        binding.RUN02ST.setOnClickListener(this::onClickDate);
        binding.RUN02ED.setOnClickListener(this::onClickDate2);
   //     binding.RUN02ST.setText(datePatternChange(filterVO.RUN_02_ST));
    //    binding.RUN02ED.setText(datePatternChange(filterVO.RUN_02_ED));

    }

    @Override
    protected void initialize() {
        mListTotal = new ArrayList<>();

        mAdapter = new ReleaseInfoListAdapter(mContext,mListTotal);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
        mCalRequest2=Calendar.getInstance();

        binding.RUN02ST.setText(datePatternChange(filterVO.RUN_02_ST));
        binding.RUN02ED.setText(datePatternChange(filterVO.RUN_02_ED));




        requestRUN_Read();
    }
    private void onClickDate(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();

    }
    private void onClickDate2(View v) {

        datePickerDialog2 = new DatePickerDialog(mContext, datePickerListener2, mCalRequest2.get(Calendar.YEAR),
                mCalRequest2.get(Calendar.MONTH), mCalRequest2.get(Calendar.DAY_OF_MONTH));
        datePickerDialog2.show();

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
                "M_LIST",
                mUser.Value.MEM_CID,
                runVO.CLT_01, // 여기서는 정확한 조회를 위해 CLT_01을 넘겨준다. 물론 DB에서의 파라미터는 CLT_02
                binding.RUN02ST.getText().toString().replaceAll("-", ""),
                binding.RUN02ED.getText().toString().replaceAll("-", "")
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
                                        bindingData(response.body().Data);
                                    } else {
                                        toast("동시접속 > 로그인 페이지로 이동합니다");  //
                                        mSettings.putBooleanItem(SettingsKey.AutoLogin, false);
                                        Intent intent = new Intent(mContext, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.putExtra("simultaneous_connect", mUser.Value.MEM_01.toString());
                                        startActivity(intent);
                                        finish();
                                    }
                                }else {
                                    Intent intent = new Intent(mContext, ReleaseCurrentActivity.class);  //OrderManagementInfoDetail 상세목록의 상세
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    mContext.startActivity(intent);
                                   // toast("검색결과가 없습니다.");
                                }
                            }else {
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

    private void bindingData(ArrayList<RUN_VO> data) {
        mListTotal = data;
        mAdapter.update(mListTotal);
    }
    private String datePatternChange(String date){
        if(date.isEmpty())
            return date;
        SimpleDateFormat beforeFormat = new SimpleDateFormat("yyyymmdd");
        SimpleDateFormat afterFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date tempDate = null;

        try {
            tempDate = beforeFormat.parse(date);
        } catch (ParseException e) {
        }

        return  afterFormat.format(tempDate);
    }
    private void onItemClickGoInfo(View view, int position) {
        Intent intent = new Intent(mContext, ReleaseInfoDetail.class);  //OrderManagementInfoDetail 상세목록의 상세
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("runVO", mListTotal.get(position));

        mActivity.startActivityForResult(intent, ReleaseInfoDetail.REQUEST_CODE);
    }
    private void onClickNew(View v) {
        Intent intent = new Intent(mContext, ReleaseInfoAdd.class); //상세목록 신규
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("runVO",runVO);
        mActivity.startActivityForResult(intent, ReleaseInfoAdd.REQUEST_CODE); //ordermanagementInfoAdd
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == ReleaseInfoDetail.REQUEST_CODE||requestCode == ReleaseInfoAdd.REQUEST_CODE) && resultCode == RESULT_OK) {
            requestRUN_Read();
        }
    }
}
