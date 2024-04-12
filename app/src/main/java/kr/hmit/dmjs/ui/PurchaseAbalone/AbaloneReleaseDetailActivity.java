package kr.hmit.dmjs.ui.PurchaseAbalone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityAbaloneReleaseDetailBinding;

import kr.hmit.dmjs.model.response.NGO_Model;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.model.vo.NGO_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.order_request.ClientNameListActivity;
import kr.hmit.dmjs.ui.order_request.OderRequestFindProductActivity;
import kr.hmit.dmjs.ui.product_info.model.ProductionInfoVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbaloneReleaseDetailActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8024;
    public static final String ORDER_REQUEST_DETAIL = "ORDER_REQUEST_DETAIL";

    private ActivityAbaloneReleaseDetailBinding binding;

    private String[] mCategory;
    private String[] mCategoryCode;
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private CLT_VO clt_vo;
    private ProductionInfoVO dah_vo;
    private String NGO_08="";
    private NGO_VO vo;

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            binding.tvNGO02.setText(sdfFormat.format(mCalRequest.getTime()));
        }
    };

    private DatePickerDialog.OnDateSetListener datePickerListener2 = new DatePickerDialog.OnDateSetListener() {
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
        binding = ActivityAbaloneReleaseDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.tvNGO02.setOnClickListener(this::onClickOrderDate);
       
        binding.tvNGO08.setOnClickListener(this::onClickCategory);
        binding.addClientName.setOnClickListener(this::onClickAddClientName);
        binding.btnUpdate.setOnClickListener(this::onClickUpdate);
        binding.btnDelete.setOnClickListener(this::onClickDelete);
        binding.addProductNum.setOnClickListener(this::onClickAddProduct);

        binding.tvNGO06.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });
        binding.tvNGO05.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        });

    }

    private void calculate(){

        int NGO_05 = Integer.parseInt(binding.tvNGO05.getText().toString().isEmpty()?"0":binding.tvNGO05.getText().toString());
        int NGO_06 = Integer.parseInt(binding.tvNGO06.getText().toString().isEmpty()?"0":binding.tvNGO06.getText().toString());


        binding.tvNGO07.setText("" + NGO_05*NGO_06);


        //  int NGO_06 = Integer.parseInt(binding.tvNGO06.getText().toString());
        //  int NGO_05 = Integer.parseInt(binding.tvNGO05.getText().toString());


    }

    @Override
    protected void initialize() {
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();

        Intent intent = getIntent();
        vo = (NGO_VO)intent.getExtras().get("NGOData");

        NGO_08=(String)vo.NGO_08;


        binding.tvNGO08.setText(vo.NGO_08_NM);
        binding.tvNGO02.setText(datePatternChange(vo.NGO_02));

        binding.tvCLT.setText(vo.NGO_03_NM);
        binding.tvDAH.setText(vo.NGO_04_NM);
        binding.tvNGO05.setText(vo.NGO_05);
        binding.tvNGO06.setText(vo.NGO_06);
        binding.tvNGO07.setText(vo.NGO_07);

        binding.tvNGO97.setText(vo.NGO_97);


    }
    private void onClickOrderDate(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    private void onClickDeliveryDate(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener2, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void onClickCategory(View v) {
        dropdownCategory();

    }
    private void onClickAddProduct(View v) {
        Intent intent = new Intent(mContext, OderRequestFindProductActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, OderRequestFindProductActivity.REQUEST_CODE);
    }

    private void onClickAddClientName(View v) {
        Intent intent = new Intent(mContext, ClientNameListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, ClientNameListActivity.REQUEST_CODE);
    }
    private void onClickUpdate(View view) {
        if (binding.tvNGO08.getText().toString().equals("출고구분") || NGO_08 =="") {
            toast("출고구분을 선택해주세요.");
        } else if (binding.tvCLT.getText().toString().isEmpty()) {
            toast("출고처를 선택해주세요.");
        }else if (binding.tvDAH.getText().toString().isEmpty()) {
            toast("품명을 선택해주세요.");
        }else if (Integer.parseInt(binding.tvNGO06.getText().toString())==0) {
            toast("수량은 최소 1이상 입니다.");
        }else {
            requestSaveNGO("M_UPDATE");
        }
    }
    private void onClickDelete(View view) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("출고정보 삭제");
        alertDialogBuilder.setMessage("삭제하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("삭제",
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                requestSaveNGO("M_DELETE");
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
    }
    private void dropdownCategory() {
        bindingCategory();
        //requestCombo_Read();
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

    private void bindingCategory() {

        mCategory = new String[3];
        mCategoryCode =  new String[3];

        mCategory[0]=  "도매";
        mCategoryCode[0] ="W";
        mCategory[1]=  "판매";
        mCategoryCode[1] ="D";
        mCategory[2]=  "가공";
        mCategoryCode[2] ="P";

    }
   /* private void bindingCategory(ArrayList<NGO_VO> data) {


        if (data.size()==0 ){

            mCategory = new String[1];
            mCategoryCode =  new String[1];
            mCategory[0]=  vo.NGO_08_NM;
            mCategoryCode[0] =vo.NGO_08;
        }
        else {

            mCategory = new String[data.size()];
            mCategoryCode = new String[data.size()];
            for (int i = 0; i < data.size(); i++) {
                mCategory[i] = data.get(i).NGO_08_NM;
                mCategoryCode[i] = data.get(i).NGO_08;
            }
        }

    }*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 담당자 추가
        if (requestCode == ClientNameListActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            clt_vo = (CLT_VO) data.getSerializableExtra(ClientNameListActivity.CLIENT_LIST);
            vo.NGO_03=clt_vo.CLT_01;
            binding.tvCLT.setText(clt_vo.CLT_02);
        }else if (requestCode == OderRequestFindProductActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            dah_vo=(ProductionInfoVO) data.getSerializableExtra(OderRequestFindProductActivity.PRODUCT_LIST);
            vo.NGO_04=dah_vo.DAH_01;
            binding.tvNGO05.setText(dah_vo.DAH_11);
            binding.tvDAH.setText(dah_vo.DAH_02);
        }
    }

    /*private void requestCombo_Read() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();

        Http.ngo(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).NGO_Read(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_COMBO",
                mUser.Value.MEM_CID,
                "","","","","",""
        ).enqueue(new Callback<NGO_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<NGO_Model> call, Response<NGO_Model> response) {
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

                            Response<NGO_Model> response = (Response<NGO_Model>) msg.obj;

                            if (response.isSuccessful()) {

                                if (response.body().Data.size()==0){

                                    bindingCategory(new ArrayList<NGO_VO>());
                                }
                                else {  bindingCategory(response.body().Data);}


                            } else {
                                bindingCategory(new ArrayList<NGO_VO>());
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<NGO_Model> call, Throwable t) {
                closeLoadingBar();
                call.cancel();
                BaseAlert.show(mContext, R.string.network_error_2);
            }
        });
    }*/



    public void requestSaveNGO(String GUBUN) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();
        Http.ngo(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).NGO_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                GUBUN,
                mUser.Value.MEM_CID,
                vo.NGO_01,
                binding.tvNGO02.getText().toString().replaceAll("-",""),
              vo.NGO_03,
                vo.NGO_04,
                Float.parseFloat(binding.tvNGO05.getText().toString()),
                Float.parseFloat(binding.tvNGO06.getText().toString()),
                Float.parseFloat(binding.tvNGO07.getText().toString()),
                NGO_08,
                "",
                0,
                binding.tvNGO97.getText().toString(),
                mUser.Value.MEM_01

        ).enqueue(new Callback<NGO_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<NGO_Model> call, Response<NGO_Model> response) {
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
                        if(GUBUN=="M_DELETE"){
                            toast("출고정보를 삭제했습니다.");
                        }
                        else if(GUBUN=="M_UPDATE"){

                            toast("출고정보를 수정했습니다.");
                        }

                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }.sendMessage(msg);
            }


            @Override
            public void onFailure(Call<NGO_Model> call, Throwable t) {
                call.cancel();
                closeLoadingBar();
            }
        });
    }

}
