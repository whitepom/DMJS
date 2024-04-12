package kr.hmit.dmjs.ui.bulletin_board;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityBulletinBoardAddBinding;
import kr.hmit.dmjs.model.vo.BRD_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.bulletin_board.adapter.FileListAdapter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kr.hmit.dmjs.ui.bulletin_board.ConvertUriToFilePath.getPathFromURI;

public class BulletinBoardReplyActivity extends BaseActivity {

    public static final int REQUEST_CODE = 2020;
    public static final String BulletinBoard_ADD_INFO = "BulletinBoard_REPLY_INFO";
    private BRD_VO brdVO;
    //================================
    // region // view
    //================================

    private ActivityBulletinBoardAddBinding binding;
    private FileListAdapter mAdapter;
    private ArrayList<String> mListTotal;

    //================================
    // endregion // view
    //================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBulletinBoardAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        brdVO = (BRD_VO) intent.getExtras().get("BRDdata");

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(this::onClickSave);
        binding.addFile.setOnClickListener(this::onClickSearch);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));


        binding.etTitle.setText("[답변]"+brdVO.BRD_10.replace("&nbsp->","").replace("&nbsp",""));
        //binding.etTitle.setText("[답변]"+brdVO.BRD_10.split("&nbsp->")[0]);

    }

    @Override
    protected void initialize() {
        mListTotal = new ArrayList<>();

        mAdapter = new FileListAdapter(mContext,mListTotal);
        mAdapter.setOnItemClickListener(this::onItemDeleteFile);
        binding.recyclerView.setAdapter(mAdapter);
    }
    private void onItemDeleteFile(View view, int i) {
        mListTotal.remove(i);
        mAdapter.update(mListTotal);
    }


    private void onClickSearch(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent,10);

    }

    private void onClickSave(View view) {

        if (binding.etTitle.getText().toString().isEmpty()) {
            toast("제목을 입력해주세요.");
        } else if (binding.etContents.getText().toString().isEmpty()) {
            toast("내용를 입력해주세요.");
        }else {
            requestSaveBRD();
        }
    }

    public void requestSaveBRD() {
        Map<String, RequestBody> rqMap = new HashMap<>();
        rqMap.put("MEM_CID", RequestBody.create(MediaType.parse("text/plain"), mUser.Value.MEM_CID));
        rqMap.put("MEM_01", RequestBody.create(MediaType.parse("text/plain"), mUser.Value.MEM_01));
        rqMap.put("TKN_03", RequestBody.create(MediaType.parse("text/plain"), mUser.Value.TKN_03));
        rqMap.put("GUBUN", RequestBody.create(MediaType.parse("text/plain"), "M_INSERT"));
        rqMap.put("BRD_CID", RequestBody.create(MediaType.parse("text/plain"), mUser.Value.MEM_CID));
        rqMap.put("BRD_01", RequestBody.create(MediaType.parse("text/plain"), brdVO.BRD_01+""));
        rqMap.put("BRD_02", RequestBody.create(MediaType.parse("text/plain"), brdVO.BRD_02+""));
        rqMap.put("BRD_03", RequestBody.create(MediaType.parse("text/plain"), brdVO.BRD_03+""));
        rqMap.put("BRD_04", RequestBody.create(MediaType.parse("text/plain"), brdVO.BRD_04+""));
        rqMap.put("BRD_05", RequestBody.create(MediaType.parse("text/plain"), brdVO.BRD_05));
        rqMap.put("BRD_06", RequestBody.create(MediaType.parse("text/plain"), brdVO.BRD_06));
        rqMap.put("BRD_07", RequestBody.create(MediaType.parse("text/plain"), brdVO.BRD_07));
        rqMap.put("BRD_10", RequestBody.create(MediaType.parse("text/plain"), binding.etTitle.getText().toString()));
        rqMap.put("BRD_11", RequestBody.create(MediaType.parse("text/plain"), binding.etContents.getText().toString()));
        rqMap.put("BRD_12", RequestBody.create(MediaType.parse("text/plain"), brdVO.BRD_12));
        rqMap.put("BRD_13", RequestBody.create(MediaType.parse("text/plain"), brdVO.BRD_13));
        rqMap.put("BRD_14", RequestBody.create(MediaType.parse("text/plain"), brdVO.BRD_14));
        rqMap.put("BRD_16", RequestBody.create(MediaType.parse("text/plain"), mUser.Value.MEM_01));

        List<MultipartBody.Part> multiFiles = new ArrayList<>();
        for(String path : mListTotal){
            File file = new File(path);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("multiFiles", file.getName(), requestFile);

            multiFiles.add(body);
        }

        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }
        openLoadingBar();
        Http.brd(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).BRD_U(
                BaseConst.URL_HOST,
                rqMap,
                multiFiles
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
                        toast("답글을 추가하였습니다.");
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

        if (data != null && requestCode == 10 && resultCode == RESULT_OK) {

            String filePath = getPathFromURI(mContext, data.getData());
            mListTotal.add(filePath);
            mAdapter.update(mListTotal);

        }

    }

}
