package kr.hmit.dmjs.ui.qr_scan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.Nullable;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityStockstatusDialogBinding;


public class StockstatusDialogActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8552;
    private ActivityStockstatusDialogBinding binding;
    Button btnWarehousing, btnRelease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStockstatusDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_stockstatus_dialog);


        btnWarehousing = findViewById(R.id.btnWarehousing);
        btnRelease = findViewById(R.id.btnRelease);

        binding.tvQr.setText("");


        btnWarehousing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StockstatusDialog2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("state","입고");
                mActivity.startActivityForResult(intent, StockstatusDialog2Activity.REQUEST_CODE);
            }
        });
        btnRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, StockstatusDialog2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("state","출고");
                mActivity.startActivityForResult(intent, StockstatusDialog2Activity.REQUEST_CODE);
            }
        });

    }

    @Override
    protected void initLayout() {

    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == StockstatusDialog2Activity.REQUEST_CODE && resultCode == RESULT_OK) {
            finish();
        }
    }
}
