package kr.hmit.dmjs.ui.production_plan.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.databinding.ViewProductionDayBinding;
import kr.hmit.dmjs.ui.production_plan.ProductionAddActivity;
import kr.hmit.dmjs.ui.production_plan.ProductionDetailActivity;

public class ProductionDayView extends ConstraintLayout {
    //=====================================
    // region // static, final
    //=====================================

    //=====================================
    // endregion // static, final
    //=====================================


    //=====================================
    // region // view
    //=====================================

    private ViewProductionDayBinding binding;

    //=====================================
    // endregion // view
    //=====================================


    //=====================================
    // region // variable
    //=====================================

    private final Context mContext;
    public TextView etQuantity;
    //=====================================
    // endregion // variable
    //=====================================
    public int beforeNum=0;
    public boolean flag = true;


    //=====================================
    // region // initialize
    //=====================================

    public ProductionDayView(Context context) {
        super(context);

        mContext = context;
    }

    public ProductionDayView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        initLayout();

        initialize();
    }

    private void initLayout() {
        binding = ViewProductionDayBinding.inflate(LayoutInflater.from(mContext), this, true);
        binding.etQuantity.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText etTemp = (EditText) v;
                if (!hasFocus) {
                    if (TextUtils.isEmpty(etTemp.getText().toString().trim()))
                        etTemp.setText("0");
                } else {
                    etTemp.setSelection(0, etTemp.getText().length());
                }

            }
        });
//        binding.etQuantity.setOnKeyListener(new OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                ArrayList<String> Day = new ArrayList<>();
//                TextView etTotalSum = (TextView) ((ProductionAddActivity)mContext).findViewById(R.id.etTotalSum);
//                binding.etQuantity.getText().toString().trim();
//                Day.add(binding.etQuantity.getText().toString().trim());
//                int Sum = 0;
//                for (int i = 0; i < Day.size(); i++) {
//                    Sum = Sum + Integer.parseInt(Day.get(i));
//                }
//                etTotalSum.setText(Integer.parseInt(etTotalSum.getText().toString())+Sum+"");
//                return false;
//            }
//        });
        binding.etQuantity.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if(flag){
                        ArrayList<String> Day = new ArrayList<>();
                        binding.etQuantity.getText().toString().trim();
                        Day.add(binding.etQuantity.getText().toString().trim());
                        int Sum = 0;
                        for (int i = 0; i < Day.size(); i++) {
                            try {
                                Sum = Sum + Integer.parseInt(Day.get(i));
                            }catch (NumberFormatException e){
                                Day.get(i).equals("0");
                            }
                        }
                        beforeNum=Sum;
                        flag=false;
                    }
                } else {
                    ArrayList<String> Day = new ArrayList<>();
                if (mContext instanceof ProductionAddActivity) {
                    TextView etTotalSum = (TextView) ((ProductionAddActivity) mContext).findViewById(R.id.etTotalSum);
                    binding.etQuantity.getText().toString().trim();
                    Day.add(binding.etQuantity.getText().toString().trim());
                    int Sum = 0;
                    for (int i = 0; i < Day.size(); i++) {
                        try {
                            Sum = Sum + Integer.parseInt(Day.get(i));
                        }catch (NumberFormatException e){
                            Day.get(i).equals("0");
                        }
                    }
                    int totalNum = etTotalSum.getText().toString().isEmpty() ? 0 : Integer.parseInt(etTotalSum.getText().toString());
                    etTotalSum.setText(totalNum - beforeNum + Sum + "");
                    flag = true;
                } else if (mContext instanceof ProductionDetailActivity){
                    TextView etTotalSum = (TextView) ((ProductionDetailActivity)mContext).findViewById(R.id.tvTotalSum);
                    binding.etQuantity.getText().toString().trim();
                    Day.add(binding.etQuantity.getText().toString().trim());
                    int Sum = 0;
                    for (int i = 0; i < Day.size(); i++) {
                        try {
                            Sum = Sum + Integer.parseInt(Day.get(i));
                        }catch (NumberFormatException e){
                            Day.get(i).equals("0");
                        }
                    }
                    int totalNum = etTotalSum.getText().toString().isEmpty()?0:Integer.parseInt(etTotalSum.getText().toString());
                    etTotalSum.setText(totalNum-beforeNum+Sum+"");
                    flag=true;
                }
                }
            }
        });


    }

    private void initialize() {

    }

    public void initType() {
        setText("");
        setTag(null);
        setSelected(false);
        setActivated(false);
        setEnabled(true);
        setVisibility(VISIBLE);
    }

    //=====================================
    // endregion // initialize
    //=====================================
    public String getDay() {
        return binding.tvDay.getText().toString();
    }

    public String getQuantity() {

        return binding.etQuantity.getText().toString().trim();

    }

    public void setText(String day) {
        binding.tvDay.setText(day);
    }

    public void setValue(String value) {
        binding.etQuantity.setText(value);
    }
}