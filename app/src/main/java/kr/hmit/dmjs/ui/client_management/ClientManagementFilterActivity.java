package kr.hmit.dmjs.ui.client_management;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import kr.hmit.dmjs.databinding.ActivityClientManagementFilterBinding;
import kr.hmit.base.base_activity.BaseActivity;

public class ClientManagementFilterActivity extends BaseActivity {
    public static final int REQUEST_CODE = 2003;

    private ActivityClientManagementFilterBinding binding;
    private String[] mCategory1;

    private String filterData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClientManagementFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLayout();
        initialize();
    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.tvFilter1.setOnClickListener(this::onClickCategory1);
        binding.btnSave.setOnClickListener(this::onClickSave);

    }

    @Override
    protected void initialize() {
        mCategory1= new String[]{"전체","매입매출","매입","매출","농가","기타","고객"};

        Intent intent = getIntent();
        filterData = intent.getExtras().get("filterData").toString();

        binding.tvFilter1.setText(textCheck(filterData));



    }
    private void onClickCategory1(View v) {
        dropdownCategory("거래구분",mCategory1,binding.tvFilter1);
    }
    private void onClickSave(View view) {

        Intent intent = new Intent();
        intent.putExtra("filterData",textCheck(binding.tvFilter1.getText().toString()));
        setResult(Activity.RESULT_OK, intent);
        finish();

    }

    private void dropdownCategory(String dialogTitle, String[] category, TextView textView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle(dialogTitle)
                .setItems(category, (dialog, which) -> {
                    textView.setText(category[which]);
                }).setCancelable(false).create();

        builder.show();
    }
    public static String textCheck(String src){
        switch (src.trim()){
            case "매입매출" : return "A";
            case "매입" : return "B";
            case "매출" : return "C";
            case "농가" : return "N";
            case "기타" : return "Z";
            case "고객" : return "G";
            case "A" : return "매입매출";
            case "B" : return "매입";
            case "C" : return "매출";
            case "N" : return "농가";
            case "Z" : return "기타";
            case "G" : return "고객";
            case "" : return "전체";
        }
        return "";
    }


}
