package kr.hmit.dmjs.ui.order_request;

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
import kr.hmit.dmjs.databinding.ActivityOrderRequestMultiAddBinding;
import kr.hmit.dmjs.model.response.CDO_Model;
import kr.hmit.dmjs.model.response.REQ_Model;
import kr.hmit.dmjs.model.vo.CDO_VO;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.model.vo.REQ_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.order_request.adapter.OrderRequestMultiAddListAdapter;
import kr.hmit.dmjs.ui.order_request.model.MultiItemVO;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderRequestMultiAddActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8023;
    public static final String ORDER_REQUEST_ADD = "ORDER_REQUEST_ADD";

    private ActivityOrderRequestMultiAddBinding binding;

    private OrderRequestMultiAddListAdapter mAdapter;
    private boolean flag = true;
    private String[] mCategory;
    private String[] mCategoryCode;
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private CLT_VO clt_vo;
    private String req_09="";
    private ArrayList<MultiItemVO> itemList;
    private int index;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if(flag)
                binding.tvOrderDate.setText(sdfFormat.format(mCalRequest.getTime()));
            else
                binding.tvDeliveryDate.setText(sdfFormat.format(mCalRequest.getTime()));

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderRequestMultiAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.tvOrderDate.setOnClickListener(this::onClickOrderDate);
        binding.tvDeliveryDate.setOnClickListener(this::onClickDeliveryDate);
        binding.tvOrderType.setOnClickListener(this::onClickCategory);
        binding.addClientName.setOnClickListener(this::onClickAddClientName);
        binding.btnSave.setOnClickListener(this::onClickSave);
        binding.addProductNum.setOnClickListener(this::onClickAddProduct);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));

    }

    @Override
    protected void initialize() {
        index=-1;
        itemList=new ArrayList<>();
        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();
        binding.tvOrderDate.setText(sdfFormat.format(mCalRequest.getTime()));
        binding.tvDeliveryDate.setText(sdfFormat.format(mCalRequest.getTime()));

        mAdapter = new OrderRequestMultiAddListAdapter(mContext,itemList);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);
        requestCombo_Read();

    }
    private void onItemClickGoInfo(View view, int position) {
        index=position;
        Intent intent = new Intent(mContext, OrderRequestMultiAddProductUpdateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("productInfo",itemList.get(position));
        mActivity.startActivityForResult(intent, OrderRequestMultiAddProductUpdateActivity.REQUEST_CODE);

    }

    private void onClickOrderDate(View v) {
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
    private void onClickAddClientName(View v) {
        Intent intent = new Intent(mContext, ClientNameListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, ClientNameListActivity.REQUEST_CODE);
    }
    private void onClickSave(View view) {
        if (binding.tvOrderType.getText().toString().equals("판매유형")) {
            toast("판매유형을 선택해주세요.");
        } else if (binding.tvOrderNum.getText().toString().isEmpty()) {
            toast("주문자를 선택해주세요.");
        }else if (itemList.size()<1) {
            toast("품명을 선택해주세요.");
        }else {
            for(MultiItemVO vo : itemList){
                requestSaveREQ(vo);
            }
            toast("주문접수를 완료하였습니다.");
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();

        }
    }
    private void onClickAddProduct(View v) {
        Intent intent = new Intent(mContext, OrderRequestMultiAddProductActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, OrderRequestMultiAddProductActivity.REQUEST_CODE);
    }

    private void dropdownCategory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle("판매유형")
                .setItems(mCategory, (dialog, which) -> {
                    binding.tvOrderType.setText(mCategory[which]);
                    req_09=mCategoryCode[which];
                }).setCancelable(false).create();

        builder.show();
    }



    private void bindingCategory(ArrayList<REQ_VO> data) {


        if (data.size()==0 ){

            mCategory = new String[1];
            mCategoryCode =  new String[1];
            mCategory[0]=  "없음";
            mCategoryCode[0] ="";
        }
        else {

            mCategory = new String[data.size()];
            mCategoryCode = new String[data.size()];
            for (int i = 0; i < data.size(); i++) {
                mCategory[i] = data.get(i).REQ_09_NM;
                mCategoryCode[i] = data.get(i).REQ_09;
            }
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 담당자 추가
        if (requestCode == ClientNameListActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            clt_vo = (CLT_VO) data.getSerializableExtra(ClientNameListActivity.CLIENT_LIST);
            binding.tvOrderNum.setText(clt_vo.CLT_02);
        }else if (requestCode == OrderRequestMultiAddProductActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            itemList.add((MultiItemVO) data.getSerializableExtra("productInfo"));
            mAdapter.update(itemList);
        }else if (requestCode == OrderRequestMultiAddProductUpdateActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            MultiItemVO tempVO =(MultiItemVO) data.getSerializableExtra("productInfo");
            if(tempVO.DAH_01==null){
                itemList.remove(index);
            }else{
                itemList.set(index,tempVO);
            }
            mAdapter.update(itemList);
        }
    }
    private void requestCombo_Read() {
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
    }
    public void requestSaveREQ(MultiItemVO vo) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();
        Http.req(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).REQ_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_INSERT",
                mUser.Value.MEM_CID,
                "",
                binding.tvOrderDate.getText().toString().replaceAll("-",""),
                clt_vo.CLT_01,
                vo.DAH_01,
                (float)vo.DAH_11,
                Float.parseFloat(vo.quantity),
                Float.parseFloat(vo.amount),
                binding.tvDeliveryDate.getText().toString().replaceAll("-",""),
                req_09,
                "",
                "",
                binding.tvNGOC97.getText().toString(),
                mUser.Value.MEM_01

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
                        toast("수주처를 추가하였습니다.");
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
}
