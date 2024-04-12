package kr.hmit.dmjs.ui.qr_scan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityStockstatusDialog2Binding;


public class StockstatusDialog2Activity extends BaseActivity {
    public static final int REQUEST_CODE = 8552;
    public static final int Dialog_CODE = 8553;
    private ActivityStockstatusDialog2Binding binding;
    Button btnSave;
    ImageView imgBack, imgPlus, imgMinus;
    TextView tvTitle;
    EditText etQuantity;
    String State, Quantity;
    Integer result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStockstatusDialog2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_stockstatus_dialog2);

        Intent intent = getIntent();
        State = (String) intent.getExtras().get("state");
        toast(State);

        btnSave = findViewById(R.id.btnSave);
        imgBack = findViewById(R.id.imgBack);
        tvTitle = findViewById(R.id.tvTitle);
        imgPlus = findViewById(R.id.imgPlus);
        imgMinus = findViewById(R.id.imgMinus);
        etQuantity = findViewById(R.id.etQuantity);

        initLayout();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StockstatusDialog2Activity.this,ScanQR.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast(State+" 처리되었습니다.");
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num1 = etQuantity.getText().toString();

                if (num1.equals("")) {
                    etQuantity.setText("0");
                }
                else {
                    int a = Integer.parseInt(etQuantity.getText().toString());
                    result = a + 1;
                    etQuantity.setText(result+"");
                };
            }
        });
        imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num1 = etQuantity.getText().toString();

                if (num1.equals("")) {
                    etQuantity.setText("0");
                }
                else {
                    int a = Integer.parseInt(etQuantity.getText().toString());
                    result = a - 1;
                    etQuantity.setText(result+"");
                };
            }
        });


    }

    @Override
    protected void initLayout() {
        Quantity = "";
        btnSave.setText(State);
        tvTitle.setText(State+" 수량을 선택하세요");
    }

    @Override
    protected void initialize() {

    }

}
