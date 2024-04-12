package kr.hmit.dmjs.ui.notice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityNoticeAddBinding;
import kr.hmit.dmjs.network.Http;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NoticeAddActivity extends BaseActivity {

    public static final int REQUEST_CODE = 2021;
    public static final String Notice_ADD_INFO = "Notice_ADD_INFO";

    //================================
    // region // view
    //================================

    private ActivityNoticeAddBinding binding;

    private ArrayList<String> mListTotal;

    //================================
    // endregion // view
    //================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoticeAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(this::onClickSave);
    }

    @Override
    protected void initialize() {
        mListTotal = new ArrayList<>();

    }


    private void onClickSave(View view) {

        if (binding.etTitle.getText().toString().isEmpty()) {
            toast("제목을 입력해주세요.");
        } else if (binding.etContents.getText().toString().isEmpty()) {
            toast("내용을 입력해주세요.");
        }else {
            requestSaveUNO();
        }
    }

    public void requestSaveUNO() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }
        openLoadingBar();
        Http.uno(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).UNO_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "INSERT",
                mUser.Value.MEM_CID,
                0,
                "",
                "",
                binding.etTitle.getText().toString(),
                binding.etContents.getText().toString(),
                "",
                "",
                "",
                "",
                ""
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
                        toast("등록되었습니다.");
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
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

    }

}
