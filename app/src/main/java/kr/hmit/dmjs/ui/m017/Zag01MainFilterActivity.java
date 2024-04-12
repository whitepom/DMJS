package kr.hmit.dmjs.ui.m017;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import kr.hmit.base.base_activity.BaseActivity;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ActivityPurchaseMainFilterBinding;
import kr.hmit.dmjs.databinding.ActivityZag01MainBinding;
import kr.hmit.dmjs.databinding.ActivityZag01MainFilterBinding;
import kr.hmit.dmjs.model.response.NGG_Model;
import kr.hmit.dmjs.model.vo.NGG_VO;
import kr.hmit.dmjs.network.Http;
import kr.hmit.dmjs.ui.PurchaseAbalone.vo.FilterVO;
import kr.hmit.dmjs.ui.m017.filter.ZagFilterVO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Zag01MainFilterActivity extends BaseActivity{
    public static final int REQUEST_CODE = 2022;
    public static final String CATEGORY_INFO = "ZAG_01";

    private ActivityZag01MainFilterBinding binding;
    private ZagFilterVO filterVO = new ZagFilterVO();

    //콤보박스 관련
    //완료구분
    private LinkedHashMap<String, Object> mapZag10 =  new LinkedHashMap();
    private String[] mZag10 = {"- 전체 -", "미완료", "완료"};
    private String selectZag10 ="";

    public void setZag10Map(){
        mapZag10.put("" ,mZag10[0].toString());
        mapZag10.put("N",mZag10[1].toString());
        mapZag10.put("Y",mZag10[2].toString());
    }

    //달력선택 관련
    private DatePickerDialog datePickerDialog;
    private int DateFlag;
    private Calendar mCalRequest;
    private SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityZag01MainFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialize();
        initLayout();
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void initialize() {
        setZag10Map();

        sdfFormat = new SimpleDateFormat("yyyy-MM-dd");
        mCalRequest = Calendar.getInstance();

        Intent intent = getIntent();
        filterVO = (ZagFilterVO) intent.getExtras().get("ZagFilterVO");

        binding.zag02St.setText(filterVO.getZAG_02_ST());
        binding.zag02Ed.setText(filterVO.getZAG_02_ED());
    }

    @Override
    protected void initLayout() {

        binding.imgBack.setOnClickListener(v -> finish());
        binding.zag02St.setOnClickListener(v -> onClicktvDate((TextView) v , 1));
        binding.zag02Ed.setOnClickListener(v -> onClicktvDate((TextView) v , 2));
        binding.zag10.setOnClickListener(v -> onClickComboZag((TextView) v , mZag10 , "1"));

        binding.btnSearch.setOnClickListener(this::onClickSearch);
    }

    private void onClicktvDate(TextView v , int paramDateFlog) {

        DateFlag = paramDateFlog;

        datePickerDialog = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        mCalRequest.set(Calendar.YEAR, year);
                        mCalRequest.set(Calendar.MONTH, month);
                        mCalRequest.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        if (DateFlag == 1) {
                            binding.zag02St.setText(sdfFormat.format(mCalRequest.getTime()));
                            filterVO.setZAG_02_ST(sdfFormat.format(mCalRequest.getTime()));

                            binding.zag02Ed.performClick();
                        }else if (DateFlag == 2) {
                            binding.zag02Ed.setText(sdfFormat.format(mCalRequest.getTime()));
                            filterVO.setZAG_02_ED(sdfFormat.format(mCalRequest.getTime()));
                        }
                    }
                },
                mCalRequest.get(Calendar.YEAR),
                mCalRequest.get(Calendar.MONTH),
                mCalRequest.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void onClickComboZag(TextView v , String[] paramArry , String comboDiv) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        String title ="";

        if(comboDiv.equals("1")){
            title ="완료구분";
        }

        builder.setTitle(title).setCancelable(true)
                .setItems(paramArry, (dialog, which) -> {
                    setArry(which , comboDiv);

                }).setCancelable(false).create();

        builder.show();
    }

    private void setArry(int which , String comboDiv) {

        int index = 0 ;
        if(comboDiv.equals("1")){
            for(Map.Entry<String, Object> elem : mapZag10.entrySet()){
                if(which == index){
                    selectZag10 = elem.getKey();
                    break;
                }
                index++;
            }

            binding.zag10.setText(mZag10[which]);
            filterVO.setZAG_10(mZag10[which]);
        }
    }

    private void onClickSearch(View view) {

        Intent intent = new Intent();
        intent.putExtra(CATEGORY_INFO, filterVO);
        setResult(RESULT_OK, intent);
        finish();
    }
}
