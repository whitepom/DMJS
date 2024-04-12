
package kr.hmit.dmjs.ui.stockStatus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import java.text.DecimalFormat;
import java.util.ArrayList;

import kr.hmit.dmjs.databinding.ActivityInventoryHistoryDetailBinding;
import kr.hmit.dmjs.model.vo.OOK_VO;
import kr.hmit.base.base_activity.BaseActivity;

public class InventoryHistoryDetailActivity extends BaseActivity {
    public static final int REQUEST_CODE = 8013;

    private ActivityInventoryHistoryDetailBinding binding;
    private boolean flag = true;
    private ArrayList<OOK_VO> mListOOK;
    private OOK_VO ookVO;


    private ArrayList<OOK_VO> mListTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInventoryHistoryDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        ookVO = (OOK_VO) intent.getExtras().get("ookVO");
        mListTotal = new ArrayList<>();

        initLayout();
        initialize();

    }

    @Override
    protected void initLayout() {
        binding.imgBack.setOnClickListener(v -> finish());
        binding.tvOOK02.setText(ookVO.OOK_02); //일자
        binding.tvDAH01.setText(ookVO.DAH_01); //품번
        binding.tvDAH02.setText(ookVO.DAH_02); //품명
        binding.tvDAH07.setText(ookVO.DAH_07); //제품구분
        binding.tvOOK06.setText(ookVO.OOK_06); //입출구분
        binding.tvOOK07.setText(commaWithNumber(ookVO.OOK_07)); // 입출고수량
        binding.tvDAH14.setText(ookVO.DAH_14); //규격
        binding.tvDAH04.setText(ookVO.DAH_04); //단위
        binding.tvOOK01.setText(ookVO.OOK_01); //
        binding.tvCDO03.setText(ookVO.CDO_03); //판매유형
        binding.tvDAH11.setText(commaWithNumber(ookVO.DAH_11)); //단가

        binding.tvPrice.setText(commaWithNumber(ookVO.OOK_AMT)); //금액

    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {
        mListOOK = new ArrayList<>();
    }


    public String commaWithNumber(String str) {
        if (str.contains(",")) {
            str = str.replace(",", "");
        }
        double strToDouble = Double.parseDouble(str);

        return commaWithNumber(strToDouble);
    }
    public String commaWithNumber(double number) {
        DecimalFormat myFormatter = new DecimalFormat("#,###.###");

        return myFormatter.format(number);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

