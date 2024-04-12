
package kr.hmit.dmjs.ui.management_note;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityManagementNoteDetailBinding;
import kr.hmit.dmjs.model.vo.LED_VO;
import kr.hmit.dmjs.model.vo.MEM_ReadVO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.management_note.model.FilterVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagementNoteDetailActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8024;
    public static final String OrderManagement_detail = "OrderManagement_detail";

    private ActivityManagementNoteDetailBinding binding;
    private LED_VO ledVO;
    private FilterVO filterVO;

    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat;
    private String[] mCategory;
    private ArrayList<MEM_ReadVO> mListEmployee;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManagementNoteDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        ledVO = (LED_VO) intent.getExtras().get("ledVO");

        initLayout();
        initialize();

    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.btnSave.setOnClickListener(this::onClickUpdate);
        binding.btnDelete.setOnClickListener(this::onClickDelete);
        binding.btnAnswer2.setOnClickListener(this::onClickComplete);


    }



    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();

        binding.tvClientName2.setText(ledVO.LED_04);

        if(ledVO.LED_11.equals("0")){
            binding.btnAnswer2.setText("완료");
        }else{
            binding.btnAnswer2.setText("미완료");
        }

    }


    private void onClickUpdate(View view) {
        requestSaveLED("UPDATE");
    }

    private void checkValidation() {
        String strContent = binding.tvClientName2.getText().toString().trim();


        if (TextUtils.isEmpty(strContent)) {

            toast("내용을 적어주세요.");
            return;

        }


    }

    private void onClickComplete(View view) {

        if(binding.btnAnswer2.getText().toString()=="완료"){

            ledVO.LED_11 = "1";

            binding.btnAnswer2.setText("미완료");
            
        }else if(binding.btnAnswer2.getText().toString()=="미완료"){

            ledVO.LED_11 = "0";

            binding.btnAnswer2.setText("완료");
            
        }

    }
    private void onClickDelete(View view) {


            if(mUser.Value.MEM_01.equals(ledVO.LED_12)){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                alertDialogBuilder.setTitle("쪽지 삭제");
                alertDialogBuilder.setMessage("삭제하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("삭제",
                                new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestSaveLED("DELETE");
                                    }
                                }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }else{
                toast("작성자만 삭제할수 있습니다");
            }





    }


    public void requestSaveLED(String GUBUN) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();
        Http.led(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).MMO_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                GUBUN,
                mUser.Value.MEM_CID,
                "MMO",
                ledVO.LED_02,
                "",
                binding.tvClientName2.getText().toString(),
                ledVO.LED_11,
                ledVO.LED_12,
                ledVO.LED_21,
                ledVO.LED_98
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
                        if(GUBUN=="UPDATE"){
                            toast("쪽지를 수정하였습니다.");
                        }else{
                            toast("쪽지를 삭제하였습니다.");
                        }
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
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


}

