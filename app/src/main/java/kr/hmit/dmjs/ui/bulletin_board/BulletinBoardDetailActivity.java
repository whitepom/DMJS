package kr.hmit.dmjs.ui.bulletin_board;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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
import kr.hmit.base.settings.SettingsKey;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityBulletinBoardDetailBinding;

import kr.hmit.dmjs.model.response.BRDC_Model;
import kr.hmit.dmjs.model.response.FIL_Model;
import kr.hmit.dmjs.model.vo.BRDC_VO;
import kr.hmit.dmjs.model.vo.BRD_VO;
import kr.hmit.dmjs.model.vo.FIL_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.bulletin_board.adapter.BulletinBoardCommentsListAdapter;

import kr.hmit.dmjs.ui.bulletin_board.adapter.BulletinBoardFileListAdapter;
import kr.hmit.dmjs.ui.login.LoginActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kr.hmit.dmjs.ui.bulletin_board.ConvertUriToFilePath.getPathFromURI;


public class BulletinBoardDetailActivity extends BaseActivity {
    public static final int REQUEST_CODE = 2020;
    public static final String BulletinBoard_detail = "BulletinBoard_detail";
    public String DecodeContent = "";


    private ActivityBulletinBoardDetailBinding binding;
    private BRD_VO brdVO;

    private BulletinBoardCommentsListAdapter mAdapter;
    private ArrayList<BRDC_VO> mListBRDC;

    private BulletinBoardFileListAdapter mAdapter2;
    private ArrayList<FIL_VO> mListFIL;

    private ArrayList<String> mListPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBulletinBoardDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        brdVO = (BRD_VO) intent.getExtras().get("BRDdata");

        initLayout();
        initialize();

    }


    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.btnUpdate.setOnClickListener(this::onClickUpdate);
        binding.btnDelete.setOnClickListener(this::onClickDelete);
        binding.btnAnswer.setOnClickListener(this::onClickReply);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        binding.recyclerView2.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        binding.addFile.setOnClickListener(this::onClickSearch);
        binding.btnCommentsSave.setOnClickListener(this::onClickSaveComments);

        binding.etTitle.setText(brdVO.BRD_10.replace("&nbsp->","").replace("&nbsp",""));

        DecodeContent = Html.fromHtml(brdVO.BRD_11).toString();
        binding.etContents.setText(DecodeContent
                .replace("<p>","")
                .replace("<br>","")
                .replace("&amp;","&")
                .replace("</p>","")
                .replace("&nbsp;","")
                .replace("<br />"," "));


        if(!mUser.Value.MEM_01.equals(brdVO.BRD_16)){
            binding.btnUpdate.setVisibility(View.GONE);
            binding.btnDelete.setVisibility(View.GONE);
            binding.addFile.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    protected void initialize() {
        mListBRDC = new ArrayList<>();
        mListFIL = new ArrayList<>();
        mListPath = new ArrayList<>();

        mAdapter = new BulletinBoardCommentsListAdapter(mContext, mListBRDC);
        mAdapter.setOnItemClickListener(this::onItemClickCommentBtn);

        mAdapter2 = new BulletinBoardFileListAdapter(mContext, mListFIL);
        mAdapter2.setOnItemClickListener(this::onItemClickDeleteFile);
        mAdapter2.setOnItemClickListener2(this::onItemClickFileDownLoad);
        binding.recyclerView.setAdapter(mAdapter);
        binding.recyclerView2.setAdapter(mAdapter2);

        requestBRDC_Read();
        requestFIL_Read();

    }

    private void onItemClickFileDownLoad(View view, FIL_VO fil_vo) {
        String URL = BaseConst.URL_HOST+fil_vo.FIL_7001;
        String[] tempArray = URL.split("/");
        new DownloadTask(mActivity, URL, tempArray[tempArray.length-1]);
    }

    private void onClickSearch(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent,10);

    }

    private void onItemClickDeleteFile(View view, FIL_VO vo,int position) {
        if(vo.Validation){
            requestSaveFIL(vo);
        }else{
            mListPath.remove(vo.FIL_7001);
        }
        mListFIL.remove(vo);
        mAdapter2.update(mListFIL);
    }

    private void onItemClickCommentBtn(View view, BRDC_VO vo, String GUBUN) {
        if (GUBUN.equals("M_DELETE")) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
            alertDialogBuilder.setTitle("글 삭제");
            alertDialogBuilder.setMessage("삭제하시겠습니까?")
                    .setCancelable(false)
                    .setPositiveButton("삭제",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestSaveBRDC(GUBUN, vo);
                                }
                            }).setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else if (GUBUN.equals("M_UPDATE")) {
            requestSaveBRDC("M_UPDATE", vo);
        }
        try{
            InputMethodManager keyboard = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            keyboard.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception exception){
        }

    }

    private void onClickReply(View view) {
        Intent intent = new Intent(mContext, BulletinBoardReplyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("BRDdata", brdVO);
        mActivity.startActivityForResult(intent, BulletinBoardReplyActivity.REQUEST_CODE);
        finish();
    }

    private void onClickSaveComments(View view) {
        if(binding.etComments.getText().toString().isEmpty()){
            toast("댓글을 입력해주세요.");
        }else{
            BRDC_VO vo = new BRDC_VO();
            requestSaveBRDC("M_INSERT", vo);

            try{
                InputMethodManager keyboard = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
            }
            catch (Exception exception){
            }
        }
    }

    private void onClickUpdate(View view) {
        if (binding.etTitle.getText().toString().isEmpty()) {
            toast("제목을 입력해주세요.");
        } else if (binding.etContents.getText().toString().isEmpty()) {
            toast("내용를 입력해주세요.");
        }else {
            requestSaveBRD("M_UPDATE");
        }

    }

    private void onClickDelete(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("글 삭제");
        alertDialogBuilder.setMessage("삭제하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("삭제",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestSaveBRD("M_DELETE");
                            }
                        }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void requestSaveBRD(String GUBUN) {
        Map<String, RequestBody> rqMap = new HashMap<>();
        rqMap.put("MEM_CID", RequestBody.create(MediaType.parse("text/plain"), mUser.Value.MEM_CID));
        rqMap.put("MEM_01", RequestBody.create(MediaType.parse("text/plain"), mUser.Value.MEM_01));
        rqMap.put("TKN_03", RequestBody.create(MediaType.parse("text/plain"), mUser.Value.TKN_03));
        rqMap.put("GUBUN", RequestBody.create(MediaType.parse("text/plain"), GUBUN));
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
        for(String path : mListPath){
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
                        if (GUBUN == "M_UPDATE") {
                            toast("글을 수정하였습니다.");
                        } else {
                            toast("글을 삭제하였습니다.");
                        }
                        Intent intent = new Intent();
                        intent.putExtra("BRDdata", brdVO);
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

    public void requestSaveBRDC(String GUBUN, BRDC_VO vo) {

        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }
        if (GUBUN.equals("M_INSERT")) {
            vo.BRDC_01 = 0;
            vo.BRDC_02 = brdVO.BRD_01;
            vo.BRDC_06 = binding.etComments.getText().toString();
        }
        Http.brdc(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).BRDC_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                GUBUN,
                mUser.Value.MEM_CID,
                vo.BRDC_01,
                vo.BRDC_02,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_02,
                mUser.Value.MEM_01,
                vo.BRDC_06
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
                        binding.etComments.setText("");
                        requestBRDC_Read();
                    }
                }.sendMessage(msg);
            }


            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public void requestSaveFIL(FIL_VO vo) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }
        openLoadingBar();
        Http.fil(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).FIL_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "DELETE",
                mUser.Value.MEM_CID,
                vo.FIL_01,
                vo.FIL_02,
                vo.FIL_7001,
                vo.FIL_7002,
                vo.FIL_7003,
                "BRD",
                vo.FIL_98
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

    private void requestBRDC_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }
        Http.brdc(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).BRDC_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_LIST",
                mUser.Value.MEM_CID,
                0,
                brdVO.BRD_01
        ).enqueue(new Callback<BRDC_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<BRDC_Model> call, Response<BRDC_Model> response) {
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
                            Response<BRDC_Model> response = (Response<BRDC_Model>) msg.obj;
                            if (response.isSuccessful()) {
                                if (response.body().Total > 0) {
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
                                } else {
                                    bindingData(new ArrayList<BRDC_VO>());
                                }
                            } else {
                                BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<BRDC_Model> call, Throwable t) {
                BaseAlert.show(mContext, R.string.network_error_2);

            }
        });
    }

    private void requestFIL_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }
        openLoadingBar();
        Http.fil(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).FIL_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                brdVO.BRD_01,
                "BRD"
        ).enqueue(new Callback<FIL_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<FIL_Model> call, Response<FIL_Model> response) {
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
                            Response<FIL_Model> response = (Response<FIL_Model>) msg.obj;
                            if (response.isSuccessful()) {
                                if (response.body().Total > 0) {
                                    if (response.body().Data.get(0).Validation) {
                                        bindingData2(response.body().Data);
                                    } else {
                                        BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.body().Data.get(0).ERROR_MSG);
                                    }
                                } else {
                                    bindingData2(new ArrayList<FIL_VO>());
                                }
                            } else {
                                BaseAlert.show(mContext, getString(R.string.network_error_2) + "-" + response.errorBody());
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<FIL_Model> call, Throwable t) {
                closeLoadingBar();
                BaseAlert.show(mContext, R.string.network_error_2);

            }
        });
    }

    private void bindingData(ArrayList<BRDC_VO> data) {
        mListBRDC = data;
        mAdapter.update(mListBRDC);
    }

    private void bindingData2(ArrayList<FIL_VO> data) {
        mListFIL = data;
        mAdapter2.update(mListFIL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BulletinBoardReplyActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            finish();
        }else if (data != null && requestCode == 10 && resultCode == RESULT_OK) {

            String filePath = String.valueOf(data.getData());
            String fileRealPath;
            if (filePath.contains("document")){
                fileRealPath = getPathFromURI(mContext, data.getData());
            }else fileRealPath = getRealPathFromURI(mContext, data.getData());

            mListPath.add(fileRealPath);

            FIL_VO vo = new FIL_VO();
            vo.FIL_7001 = fileRealPath;
            vo.Validation = false;

            mListFIL.add(vo);
            mAdapter2.update(mListFIL);

        }

    }
    ///storage/emulated 경로의 파일
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
