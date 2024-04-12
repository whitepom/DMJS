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

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityAbaloneReleaseMultiAddBinding;
import kr.hmit.dmjs.databinding.ActivityAbaloneReleaseMultiAddBinding;
import kr.hmit.dmjs.model.response.NGOC_Model;

import kr.hmit.dmjs.model.response.NGO_Model;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.model.vo.NGO_VO;

import kr.hmit.dmjs.model.vo.NGO_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.PurchaseAbalone.adapter.AbaloneReleaseMultiAddListAdapter;
import kr.hmit.dmjs.ui.order_request.ClientNameListActivity;
import kr.hmit.dmjs.ui.order_request.OrderRequestMultiAddProductUpdateActivity;
import kr.hmit.dmjs.ui.order_request.model.MultiItemVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbaloneReleaseMultiAddActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8023;
    public static final String ORDER_REQUEST_ADD = "ORDER_REQUEST_ADD";

    
    private ActivityAbaloneReleaseMultiAddBinding binding;

    private AbaloneReleaseMultiAddListAdapter mAdapter;
    private boolean flag = true;
    private String[] mCategory;
    private String[] mCategoryCode;
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat;
    private DatePickerDialog datePickerDialog;
    private CLT_VO clt_vo;
    private String NGO_08="";
    private ArrayList<NGO_VO> itemList;
    private int index;
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mCalRequest.set(Calendar.YEAR, year);
            mCalRequest.set(Calendar.MONTH, month);
            mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            binding.tvNGO02.setText(sdfFormat.format(mCalRequest.getTime()));
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAbaloneReleaseMultiAddBinding.inflate(getLayoutInflater());
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
        binding.tvNGO02.setText(sdfFormat.format(mCalRequest.getTime()));
     

        mAdapter = new AbaloneReleaseMultiAddListAdapter(mContext,itemList);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);
        //requestCombo_Read();

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
        if (binding.tvNGO08.getText().toString().equals("출고구분")) {
            toast("출고구분을 선택해주세요.");
        } else if (binding.tvNGO03.getText().toString().isEmpty()) {
            toast("출고처를 선택해주세요.");
        }else if (itemList.size()<1) {
            toast("품명을 선택해주세요.");
        }else {
            for(NGO_VO vo : itemList){        ////이부분 설계가 덜 된 것 같으나 일단 MES기준으로 작성함.&&&&&&&&&&&&

                requestSaveNGOC(vo);
            }
            toast("주문접수를 완료하였습니다.");
            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            finish();

        }
    }
    private void onClickAddProduct(View v) {
        Intent intent = new Intent(mContext, AbaloneReleaseMultiAddProductActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, AbaloneReleaseMultiAddProductActivity.REQUEST_CODE);
    }

    private void dropdownCategory() {
        bindingCategory();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle("출고구분")
                .setItems(mCategory, (dialog, which) -> {
                    binding.tvNGO08.setText(mCategory[which]);
                    NGO_08=mCategoryCode[which];
                }).setCancelable(false).create();

        builder.show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 담당자 추가
        if (requestCode == ClientNameListActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            clt_vo = (CLT_VO) data.getSerializableExtra(ClientNameListActivity.CLIENT_LIST);
            binding.tvNGO03.setText(clt_vo.CLT_02);
            
        }else if (requestCode == AbaloneReleaseMultiAddProductActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            itemList.add((NGO_VO) data.getSerializableExtra("productInfo"));
            mAdapter.update(itemList);
        }else if (requestCode == OrderRequestMultiAddProductUpdateActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            NGO_VO tempVO =(NGO_VO) data.getSerializableExtra("productInfo");
            if(tempVO.DAH_01==null){
                itemList.remove(index);
            }else{
                itemList.set(index,tempVO);
            }
            mAdapter.update(itemList);
        }
    }
  /*  private void requestCombo_Read() {
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
    public void requestSaveNGOC(NGO_VO vo) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();
        Http.ngo(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).NGOC_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_INSERT",
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01, // 아이디
               0,
                vo.NGOC_03,
                Float.parseFloat(vo.NGOC_04),
                Float.parseFloat(vo.NGOC_05),
                Float.parseFloat(vo.NGOC_06),
               0,

               vo.NGOC_97,
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
                        toast("품목을 추가하였습니다.");
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
