package kr.hmit.dmjs.ui.client_management;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityClientManagementAddBinding;
import kr.hmit.dmjs.ui.client_management.adapter.ClientManagementAddListAdapter;
import kr.hmit.dmjs.ui.client_management.vo.managerVO;
import kr.hmit.dmjs.ui.sample.AddressActivity;
import kr.hmit.dmjs.ui.sample.AddressModel;
import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.ClsNetworkCheck;


public class ClientManagementAddActivity extends BaseActivity {
    public static final int REQUEST_CODE = 2001;
    public static final String CLIENT_ADD_INFO = "CLIENT_ADD_INFO";

    private ClientManagementAddListAdapter mAdapter;
    private ActivityClientManagementAddBinding binding;
    private String[] mCategory1;
    private ArrayList<managerVO> managerList;
    private AddressModel addressData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClientManagementAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
      //  binding.tvFilter1.setOnClickListener(this::onClickCategory1);
     //   binding.addManager.setOnClickListener(this::onClickAddManager);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        binding.btnSave.setOnClickListener(this::onClickSave);
        binding.addClientAddress.setOnClickListener(this::onClickAddClientAddress);

    }

    @Override
    protected void initialize() {
        mCategory1 = new String[]{"매입매출", "매입", "매출", "농가", "기타", "고객"};
        managerList = new ArrayList<>();

        mAdapter = new ClientManagementAddListAdapter(mContext, managerList);
     //   mAdapter.setOnItemClickListener(this::onItemClickGoInfo);
        binding.recyclerView.setAdapter(mAdapter);

    }

    private void onClickAddClientAddress(View v) {
        Intent intent = new Intent(mContext, AddressActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mActivity.startActivityForResult(intent, AddressActivity.REQUEST_CODE);
    }

    private void onClickSave(View view) {

        if (binding.tvFilter1.getText().toString().equals("거래구분")) {
            toast("거래구분을 선택하세요.");
        } else if (binding.tvClientName.getText().toString().isEmpty()) {
            toast("상호를 입력하세요.");
        } else {
            requestSaveCLT();
        }
    }
/*
    private void onClickCategory1(View v) {
        dropdownCategory("거래구분", mCategory1, binding.tvFilter1);
    }

    private void onClickAddManager(View v) {
        openCustomDialog(new managerVO());
    }


    private void onItemClickGoInfo(View view, int position) {
        openCustomDialog(managerList.get(position));
    }
*/
    public void requestSaveCLT() {
        if (!ClsNetworkCheck.isConnectable(mContext)) {
            BaseAlert.show(mContext, R.string.network_error_1);
            return;
        }

     /*   openLoadingBar();
        Http.clt(HttpBaseService.TYPE.POST, BaseConst.URL_HOST).CLT_U(
                BaseConst.URL_HOST,
                mUser.Value.MEM_CID,
                mUser.Value.MEM_01,
                mUser.Value.TKN_03,
                "M_INSERT",
                mUser.Value.MEM_CID,
                "",
                getCategory(binding.tvFilter1.getText().toString()),
                binding.tvClientName.getText().toString(),
                binding.tvClientNum.getText().toString(),
                binding.tvClientOwnerName.getText().toString(),
                binding.tvClientstate.getText().toString(),
                "",
                binding.tvClientPostalCode.getText().toString(),
                binding.tvClientAddress.getText().toString(),
                binding.etClientAddress.getText().toString(),
                binding.tvClientNumber.getText().toString(),
                binding.tvClientFax.getText().toString(),
                managerList.size() > 0 ? managerList.get(0).name : "",
                managerList.size() > 0 ? managerList.get(0).mail : "",
                managerList.size() > 0 ? managerList.get(0).phone.replaceAll("-","") : "",
                managerList.size() > 1 ? managerList.get(1).name : "",
                managerList.size() > 1 ? managerList.get(1).mail : "",
                managerList.size() > 1 ? managerList.get(1).phone.replaceAll("-","") : "",
                managerList.size() > 2 ? managerList.get(2).name : "",
                managerList.size() > 2 ? managerList.get(2).mail : "",
                managerList.size() > 2 ? managerList.get(2).phone.replaceAll("-","") : "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
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
                        toast("거래처를 추가하였습니다.");
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

    public static String getCategory(String src) {
        switch (src) {
            case "매입매출":
                return "A";
            case "매입":
                return "B";
            case "매출":
                return "C";
            case "농가":
                return "N";
            case "기타":
                return "Z";
            case "고객":
                return "G";
        }
        return "";
    }

    private void dropdownCategory(String dialogTitle, String[] category, TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(dialogTitle)
                .setItems(category, (dialog, which) -> {
                    textView.setText(category[which]);
                }).setCancelable(false).create();

        builder.show();
    }


    private void openCustomDialog(managerVO vo) {

        final Dialog dlg = new Dialog(mContext);

        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dlg.setContentView(R.layout.dialog_client_management_add_manager);

        dlg.show();

        final EditText nameText = dlg.findViewById(R.id.nameText);
        final EditText mailText = dlg.findViewById(R.id.mailText);
        final EditText phoneText = dlg.findViewById(R.id.phoneText);

        final Button okButton = dlg.findViewById(R.id.okButton);
        final Button deleteButton = dlg.findViewById(R.id.deleteButton);
        final Button cancelButton = dlg.findViewById(R.id.cancelButton);

        if (vo.name == null) {
            deleteButton.setVisibility(View.INVISIBLE);
        } else {
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

                if (vo.name == null) {
                    if (name.isEmpty()) {
                        Toast.makeText(mContext, "담당자 성명을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    } else if (managerList.size() > 2) {
                        Toast.makeText(mContext, "정보는 3명까지만 입력가능합니다.", Toast.LENGTH_SHORT).show();
                    }else if(!UtilBox.isValidEmail(mail)&&!mail.isEmpty()){
                        Toast.makeText(mContext, "메일 형식을 확인해주세요", Toast.LENGTH_SHORT).show();
                    } else {
                        int number = managerList.size();
                        managerList.add(new managerVO(number, name, mail, phone));
                        dlg.dismiss();
                        mAdapter.update(managerList);
                    }
                } else {
                    int number = vo.number;

                    if (name.isEmpty()) {
                        Toast.makeText(mContext, "담당자 성명을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    }else if(!UtilBox.isValidEmail(mail)&&!mail.isEmpty()){
                        Toast.makeText(mContext, "메일 형식을 확인해주세요", Toast.LENGTH_SHORT).show();
                    }
                    managerVO tempVO = new managerVO(number, name, mail, phone);

                    managerList.set(number, tempVO);
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

*/
    }
}
