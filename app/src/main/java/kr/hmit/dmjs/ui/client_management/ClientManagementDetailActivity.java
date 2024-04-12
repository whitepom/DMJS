package kr.hmit.dmjs.ui.client_management;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityClientManagementDetailBinding;
import kr.hmit.dmjs.model.vo.CLT_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.UtilBox;
import kr.hmit.dmjs.ui.client_management.adapter.ClientManagementAddListAdapter;
import kr.hmit.dmjs.ui.client_management.vo.managerVO;
import kr.hmit.dmjs.ui.sample.AddressActivity;
import kr.hmit.dmjs.ui.sample.AddressModel;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientManagementDetailActivity extends BaseActivity {

    public static final int REQUEST_CODE = 2002;
    public static final String CLIENT_DETAIL_INFO = "CLIENT_DETAIL_INFO";

    private ClientManagementAddListAdapter mAdapter;
    private ActivityClientManagementDetailBinding binding;
    private ArrayList<managerVO> managerList;
    private String[] mCategory1;

    private AddressModel addressData;
    private CLT_VO vo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClientManagementDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
        binding.btnDelete.setOnClickListener(this::onClickDelete);
        binding.btnUpdate.setOnClickListener(this::onClickUpdate);
        binding.tvFilter1.setOnClickListener(this::onClickCategory1);
        binding.addManager.setOnClickListener(this::onClickAddManager);
        binding.addClientAddress.setOnClickListener(this::onClickAddClientAddress);
    }

    @Override
    protected void initialize() {
        managerList = new ArrayList<>();
        mCategory1= new String[]{"매입매출","매입","매출","농가","기타","고객"};


        Intent intent = getIntent();
        vo = (CLT_VO)intent.getExtras().get("CLTData");

        binding.tvFilter1.setText(getCategory(vo.CLT_29));
        binding.tvClientName.setText(vo.CLT_02);
        binding.tvClientNum.setText(vo.CLT_04);
        binding.tvClientOwnerName.setText(vo.CLT_03);
        binding.tvClientstate.setText(vo.CLT_06);
        binding.tvClientPostalCode.setText(vo.CLT_1001);
        binding.tvClientAddress.setText(vo.CLT_1002);
        binding.etClientAddress.setText(vo.CLT_1003);
        binding.tvClientNumber.setText(UtilBox.phone(vo.CLT_08));
        binding.tvClientFax.setText(UtilBox.phone(vo.CLT_09));
        if(!vo.CLT_24.isEmpty()){
            managerList.add(new managerVO(0,vo.CLT_24,vo.CLT_25,vo.CLT_13));
        }
        if(!vo.CLT_50.isEmpty()){
            managerList.add(new managerVO(1,vo.CLT_50,vo.CLT_51,vo.CLT_52));

        }
        if(!vo.CLT_53.isEmpty()){
            managerList.add(new managerVO(2,vo.CLT_53,vo.CLT_54,vo.CLT_55));
        }


        mAdapter = new ClientManagementAddListAdapter(mContext,managerList);
        mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);


    }
    private void onClickAddClientAddress(View v) {
        Intent intent = new Intent(mContext, AddressActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, AddressActivity.REQUEST_CODE);
    }
    private void onItemClickGoInfo(View view, int position) {
        openCustomDialog(managerList.get(position));
    }

    private void onClickAddManager(View v) {openCustomDialog(new managerVO());}

    private void onClickCategory1(View v) {
        dropdownCategory("거래구분",mCategory1,binding.tvFilter1);
    }

    private void onClickDelete(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("거래처 삭제");
        alertDialogBuilder.setMessage("삭제하시겠습니까?")
                .setCancelable(false)
                .setPositiveButton("삭제",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestSaveCLT("M_DELETE");
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
    private void onClickUpdate(View view) {
        if(binding.tvFilter1.getText().toString().equals("거래구분")){
            toast("거래구분을 선택하세요.");
        }else if (binding.tvClientName.getText().toString().isEmpty()){
            toast("상호를 입력하세요.");
        }else{
            requestSaveCLT("M_UPDATE");
        }

    }
    public void requestSaveCLT(String GUBUN) {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

        openLoadingBar();
        Http.clt(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).CLT_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                GUBUN,
                mUser.Value.MEM_CID,
                vo.CLT_01,
                getCategory2(binding.tvFilter1.getText().toString()),
                binding.tvClientName.getText().toString(),
                binding.tvClientNum.getText().toString(),
                binding.tvClientOwnerName.getText().toString(),
                binding.tvClientstate.getText().toString(),
                vo.CLT_07,
                binding.tvClientPostalCode.getText().toString(),
                binding.tvClientAddress.getText().toString(),
                binding.etClientAddress.getText().toString(),
                binding.tvClientNumber.getText().toString(),
                binding.tvClientFax.getText().toString(),
                managerList.size()>0?managerList.get(0).name:"",
                managerList.size()>0?managerList.get(0).mail:"",
                managerList.size()>0?managerList.get(0).phone.replaceAll("-",""):"",
                managerList.size()>1?managerList.get(1).name:"",
                managerList.size()>1?managerList.get(1).mail:"",
                managerList.size()>1?managerList.get(1).phone.replaceAll("-",""):"",
                managerList.size()>2? managerList.get(2).name:"",
                managerList.size()>2?managerList.get(2).mail:"",
                managerList.size()>2?managerList.get(2).phone.replaceAll("-",""):"",
                vo.CLT_05,
                vo.CLT_11,
                vo.CLT_12,
                vo.CLT_14,
                vo.CLT_97,
                vo.CLT_972,
                vo.CLT_15,
                vo.CLT_16,
                vo.CLT_17,
                vo.CLT_18,
                vo.CLT_99,
                vo.CLT_19,
                vo.CLT_20,
                vo.CLT_21,
                vo.CLT_22,
                vo.CLT_23,
                vo.CLT_26,
                vo.CLT_27,
                vo.CLT_28,
                vo.CLT_30,
                vo.CLT_31,
                vo.CLT_32,
                vo.CLT_33,
                vo.CLT_40
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
                        Intent intent = new Intent();
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                        if(GUBUN=="M_UPDATE"){
                            toast("거래처를 수정하였습니다.");
                        }else{
                            toast("거래처를 삭제하였습니다.");
                        }
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

    private void dropdownCategory(String dialogTitle, String[] category, TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(dialogTitle)
                .setItems(category, (dialog, which) -> {
                    textView.setText(category[which]);
                }).setCancelable(false).create();

        builder.show();
    }
    public static String getCategory(String src){
        switch (src.trim()){
            case "A" : return "매입매출";
            case "B" : return "매입";
            case "C" : return "매출";
            case "N" : return "농가";
            case "Z" : return "기타";
            case "G" : return "고객";
        }
        return "";
    }
    public static String getCategory2(String src){
        switch (src.trim()){
            case "매입매출" : return "A";
            case "매입" : return "B";
            case "매출" : return "C";
            case "농가" : return "N";
            case "기타" : return "Z";
            case "고객" : return "G";
        }
        return "";
    }

    private void openCustomDialog(managerVO vo) {

        final Dialog dlg = new Dialog(mContext);

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dlg.setContentView(R.layout.dialog_client_management_add_manager);

        dlg.show();

        final EditText nameText =  dlg.findViewById(R.id.nameText);
        final EditText mailText =  dlg.findViewById(R.id.mailText);
        final EditText phoneText =  dlg.findViewById(R.id.phoneText);

        final Button okButton =  dlg.findViewById(R.id.okButton);
        final Button deleteButton =  dlg.findViewById(R.id.deleteButton);
        final Button cancelButton =  dlg.findViewById(R.id.cancelButton);

        if(vo.name==null){
            deleteButton.setVisibility(View.INVISIBLE);
        }else{
            nameText.setText(vo.name);
            mailText.setText(vo.mail);
            phoneText.setText(vo.phone);

            okButton.setText("수정");
            cancelButton.setText("닫기");
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameText.getText().toString();
                String mail = mailText.getText().toString();
                String phone = phoneText.getText().toString();

                if(vo.name==null){
                    if(name.isEmpty()){
                        Toast.makeText(mContext, "담당자 성명을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    }else if (managerList.size()>2){
                        Toast.makeText(mContext, "정보는 3명까지만 입력가능합니다.", Toast.LENGTH_SHORT).show();
                    }else if(!UtilBox.isValidEmail(mail)&&!mail.isEmpty()){
                        Toast.makeText(mContext, "메일 형식을 확인해주세요", Toast.LENGTH_SHORT).show();
                    }else{
                        int number = managerList.size();
                        managerList.add(new managerVO(number,name,mail,phone));
                        dlg.dismiss();
                        mAdapter.update(managerList);
                    }
                }else{
                    int number = vo.number;
                    if (name.isEmpty()) {
                        Toast.makeText(mContext, "담당자 성명을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    }else if(!UtilBox.isValidEmail(mail)&&!mail.isEmpty()){
                        Toast.makeText(mContext, "메일 형식을 확인해주세요", Toast.LENGTH_SHORT).show();
                    }
                    managerVO tempVO = new managerVO(number,name,mail,phone);

                    managerList.set(number,tempVO);
                    Toast.makeText(mContext, "수정 했습니다.", Toast.LENGTH_SHORT).show();
                    dlg.dismiss();
                    mAdapter.update(managerList);

                }

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "취소 했습니다.", Toast.LENGTH_SHORT).show();
                dlg.dismiss();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int number = vo.number;

                managerList.remove(number);
                Toast.makeText(mContext, "삭제 했습니다.", Toast.LENGTH_SHORT).show();
                dlg.dismiss();
                mAdapter.update(managerList);

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddressActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            addressData = (AddressModel) data.getSerializableExtra("addressData");
            binding.tvClientPostalCode.setText(addressData.Zipcode);
            binding.tvClientAddress.setText(addressData.Address);
        }


    }
}