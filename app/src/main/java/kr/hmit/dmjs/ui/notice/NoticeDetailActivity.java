package kr.hmit.dmjs.ui.notice;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.DatePicker;
import android.text.Html;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityNoticeDetailBinding;
import kr.hmit.dmjs.model.vo.UNO_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeDetailActivity extends BaseActivity {
    public static final int REQUEST_CODE = 2021;
    public String DecodeContent = "";
    //===============================
    // region // view
    //===============================
    private ActivityNoticeDetailBinding binding;
    private String[] mCategory;


    private UNO_VO UNOData;

    //===============================
    // endregion // view
    //===============================


    //===============================
    // region // variable
    //===============================
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);

//            binding.tvRequestDate.setText(sdfFormat.format(mCalRequest.getTime()));
        }
    };

    //===============================
    // endregion // variable
    //===============================

    //===============================
    // region // initialize
    //===============================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoticeDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        UNOData = (UNO_VO) intent.getExtras().get("UNOdata");

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.btnUpdate.setOnClickListener(this::onClickUpdate);
        binding.btnDelete.setOnClickListener(this::onClickDelete);
        binding.etTitle.setText(UNOData.UNO_04.replace("&nbsp->","").replace("&nbsp",""));
        DecodeContent = Html.fromHtml(UNOData.UNO_05).toString();
        binding.etContents.setText(DecodeContent
                .replace("<p>","")
                .replace("<br>","")
                .replace("&amp;","&")
                .replace("</p>","")
                .replace("&nbsp;","")
                .replace("<br />"," "));
        //binding.tvRequestDate.setOnClickListener(this::onClickRequestDate);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
//        binding.tvRequestDate.setText(sdfFormat.format(mCalRequest.getTime()));
//        binding.tvNumber.setText(WKSData.WKS_01);
//        binding.tvRequestDate.setText(WKSData.UNO_02);


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


    //===============================
    // endregion // initialize
    //===============================


    //===============================
    // region // event
    //===============================


    /**
     * 요청일자 클릭
     *
     * @param v
     */
    private void onClickRequestDate(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    private void onClickDelete(View view) {
        AlertDialog.Builder oDialog = new AlertDialog.Builder(this,
                android.R.style.Theme_DeviceDefault_Light_Dialog);

        oDialog.setTitle("삭제하시겠습니까?")
                .setPositiveButton("아니오", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }

                })
                .setNeutralButton("예", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        requestSaveUNO("DELETE");
                    }
                })

                .setCancelable(false)
                .show();
    }


    private void onClickUpdate(View view) {
        checkValidation();
    }

    private void checkValidation() {
 //       String strDate = binding.tvRequestDate.getText().toString().trim();

        if (binding.etTitle.getText().toString().isEmpty()) {
            toast("제목을 입력해주세요.");
        } else if (binding.etContents.getText().toString().isEmpty()) {
            toast("내용을 입력해주세요.");
        }else {
            requestSaveUNO("UPDATE");
        }
    }

    public void requestSaveUNO(String GUBUN) {
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
                GUBUN,
                mUser.Value.MEM_CID,
                UNOData.UNO_01,
                //binding.tvRequestDate.getText().toString().replaceAll("-",""),
                "",
                "",
                binding.etTitle.getText().toString(),
                binding.etContents.getText().toString(),
                mUser.Value.MEM_01,
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
                        if(GUBUN.equals("UPDATE")) {
                            toast("글을 수정하였습니다.");
                        }else{
                            toast("글이 삭제되었습니다.");
                        }
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        intent.putExtra("UNOdata", UNOData);
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

    //===============================
    // endregion // method
    //===============================

    //===============================
    // region // api
    //=============================


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    //=============================
    // endregion // api
    //===============================
}