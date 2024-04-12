package kr.hmit.dmjs.ui.PurchaseAbalone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityPurchaseDetailMultiAddBinding;

import kr.hmit.dmjs.model.response.NGG_Model;
import kr.hmit.dmjs.model.vo.CLT_VO;

import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.PurchaseAbalone.adapter.PurchaseMainMultiAddListAdapter;
import kr.hmit.dmjs.ui.order_request.ClientNameListActivity;

import kr.hmit.dmjs.ui.PurchaseAbalone.vo.MultiItemVO;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;

import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseMainAddActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8023;
    public static final String ORDER_REQUEST_ADD = "ORDER_REQUEST_ADD";

    private ActivityPurchaseDetailMultiAddBinding binding;

    private PurchaseMainMultiAddListAdapter mAdapter;
    private boolean flag = true;
    private String[] mCategory;
    private String[] mCategoryCode;
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private CLT_VO clt_vo;
    private String CLT_29="";
    private ArrayList<MultiItemVO> itemList;
    private int index;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            binding.tvNGG02.setText(sdfFormat.format(mCalRequest.getTime()));

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPurchaseDetailMultiAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.tvNGG02.setOnClickListener(this::onClickPurchaseDate);

        binding.tvCLT29.setOnClickListener(this::onClickCategory);
        binding.addCLT.setOnClickListener(this::onClickClientAdd);
        binding.btnSave.setOnClickListener(this::onClickSave);
        binding.addDAH.setOnClickListener(this::onClickAddProduct);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

    }

    @Override
    protected void initialize() {
        index=-1;
        itemList=new ArrayList<>();
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
        binding.tvNGG02.setText(sdfFormat.format(mCalRequest.getTime()));

        mAdapter = new PurchaseMainMultiAddListAdapter(mContext,itemList);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);
        bindingCategory();

    }
    private void onItemClickGoInfo(View view, int position) {
        index=position;
        Intent intent = new Intent(mContext, PurchaseMultiAddProductUpdateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("productInfo",itemList.get(position));
        mActivity.startActivityForResult(intent, PurchaseMultiAddProductUpdateActivity.REQUEST_CODE);

    }

    private void onClickPurchaseDate(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        flag=true;
        datePickerDialog.show();
    }
    private void onClickDeliveryDate(View v) {
        datePickerDialog = new DatePickerDialog(mContext, datePickerListener, mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH), mCalRequest.get(Calendar.DAY_OF_MONTH));
        flag=false;
        datePickerDialog.show();
    }

    private void onClickCategory(View v) {
        dropdownCategory();

    }
    private void onClickClientAdd(View v) {
        Intent intent = new Intent(mContext, ClientNameListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, ClientNameListActivity.REQUEST_CODE);
    }
    private void onClickSave(View view) {
        if (binding.tvCLT29.getText().toString().equals("거래구분")) {
            toast("거래구분을 선택해주세요.");
        } else if (binding.tvCLT02.getText().toString().isEmpty()) {
            toast("거래처를 선택해주세요.");
        }else if (itemList.size()<1) {
            toast("품명을 선택해주세요.");
        }else {
            for(MultiItemVO vo : itemList){
                requestSaveNGG(vo);
            }
            toast("수매정보 등록을 완료하였습니다.");
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();

        }
    }
    private void onClickAddProduct(View v) {
        Intent intent = new Intent(mContext, PurchaseMultiAddProductActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, PurchaseMultiAddProductActivity.REQUEST_CODE);
    }

    private void dropdownCategory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle("거래구분")
                .setItems(mCategory, (dialog, which) -> {
                    binding.tvCLT29.setText(mCategory[which]);
                    CLT_29=mCategoryCode[which];
                }).setCancelable(false).create();

        builder.show();
    }



    private void bindingCategory() {

        mCategory = new String[2];
        mCategoryCode =  new String[2];

        mCategory[0]=  "매입매출";
        mCategoryCode[0] ="A";
        mCategory[1]=  "어장";
        mCategoryCode[1] ="E";
       /* if (data.size()==0 ){

            mCategory = new String[1];
            mCategoryCode =  new String[1];
            mCategory[0]=  "없음";
            mCategoryCode[0] ="";
        }
        else {

            mCategory = new String[data.size()];
            mCategoryCode = new String[data.size()];
            for (int i = 0; i < data.size(); i++) {
                mCategory[i] = data.get(i).CLT_29_NM;
                mCategoryCode[i] = data.get(i).CLT_29;
            }
        }
*/
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 담당자 추가
        if (requestCode == ClientNameListActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            clt_vo = (CLT_VO) data.getSerializableExtra(ClientNameListActivity.CLIENT_LIST);
            binding.tvCLT02.setText(clt_vo.CLT_02);
        }else if (requestCode == PurchaseMultiAddProductActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            itemList.add((MultiItemVO) data.getSerializableExtra("productInfo"));
            mAdapter.update(itemList);
        }else if (requestCode == PurchaseMultiAddProductUpdateActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            MultiItemVO tempVO =(MultiItemVO) data.getSerializableExtra("productInfo");
            if(tempVO.NGG_04==null){
                itemList.remove(index);
            }else{
                itemList.set(index,tempVO);
            }
            mAdapter.update(itemList);
        }
    }
    /*private void requestCombo_Read() {
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
                "M_COMBO",
                mUser.Value.MEM_CID,
                "","","","",""
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

                            Response<REQ_Model> response = (Response<REQ_Model>) msg.obj;

                            if (response.isSuccessful()) {

                                if (response.body().Data.size()==0){

                                    bindingCategory(new ArrayList<REQ_VO>());
                                }
                                else {  bindingCategory(response.body().Data);}


                            } else {
                                bindingCategory(new ArrayList<REQ_VO>());
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<REQ_Model> call, Throwable t) {
                closeLoadingBar();
                call.cancel();
                BaseAlert.show(mContext, R.string.network_error_2);
            }
        });
    }*/
    public void requestSaveNGG(MultiItemVO vo) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }


        openLoadingBar();
        Http.ngg(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).NGG_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_INSERT",
                mUser.Value.MEM_CID,
                "",
                binding.tvNGG02.getText().toString().replaceAll("-",""),
                clt_vo.CLT_01,
                vo.NGG_04,
                Double.parseDouble(vo.NGG_05),
                Double.parseDouble(vo.NGG_06),
                Double.parseDouble(vo.NGG_07),
                Double.parseDouble(vo.NGG_08),
                Double.parseDouble(vo.NGG_0901),
                Double.parseDouble(vo.NGG_0902),"",
                0,
                "","","","",vo.NGG_97,
                mUser.Value.MEM_01

        ).enqueue(new Callback<NGG_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<NGG_Model> call, Response<NGG_Model> response) {
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
                        toast("수주처를 추가하였습니다.");
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                }.sendMessage(msg);
            }


            @Override
            public void onFailure(Call<NGG_Model> call, Throwable t) {
                call.cancel();
                closeLoadingBar();
            }
        });
    }
}
