package kr.hmit.dmjs.ui.production_plan.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import kr.hmit.dmjs.databinding.ViewProductionCalendarBinding;

public class ProductionCalendarView extends LinearLayout {
    //=====================================
    // region // static, final
    //=====================================
    // 최대 주 갯수
    private static final int TOTAL_WEEK = 6;
    // 한주의 일 갯수
    private static final int TOTAL_DAY_OF_WEEK = 7;

    //=====================================
    // endregion // static, final
    //=====================================


    //=====================================
    // region // view
    //=====================================

    private ViewProductionCalendarBinding binding;
    private ProductionDayView[][] tvArrDays;

    //=====================================
    // endregion // view
    //=====================================


    //=====================================
    // region // variable
    //=====================================

    private final Context mContext;
    private final Calendar mCalCurrentMonth = Calendar.getInstance();
    private SimpleDateFormat sdf;

    //=====================================
    // endregion // variable
    //=====================================


    //=====================================
    // region // initialize
    //=====================================

    public ProductionCalendarView(Context context) {
        super(context);

        mContext = context;
    }

    public ProductionCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        initLayout();

        initialize();
    }

    private void initLayout() {
        binding = ViewProductionCalendarBinding.inflate(LayoutInflater.from(mContext), this, true);

        tvArrDays = new ProductionDayView[TOTAL_WEEK][TOTAL_DAY_OF_WEEK];
        for (int i = 0; i < TOTAL_WEEK; i++) {
            for (int j = 0; j < TOTAL_DAY_OF_WEEK; j++) {
                int resId = mContext.getResources().getIdentifier("tvDay" + i + j, "id", mContext.getPackageName());
                tvArrDays[i][j] = binding.getRoot().findViewById(resId);
            }
        }
  }

    private void initialize() {
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        mCalCurrentMonth.add(Calendar.MONTH, 1);
        mCalCurrentMonth.set(Calendar.DAY_OF_MONTH, 1);
        mCalCurrentMonth.add(Calendar.DAY_OF_MONTH, -1);
        mCalCurrentMonth.set(Calendar.HOUR_OF_DAY, 23);
        mCalCurrentMonth.set(Calendar.MINUTE, 59);
        mCalCurrentMonth.set(Calendar.SECOND, 59);
        initCalendarDay();
    }

    //=====================================
    // endregion // initialize
    //=====================================

    /**
     * 달력 초기화
     */
    private void initCalendarDay() {
        // 초기화
        for (int i = 0; i < TOTAL_WEEK; i++) {
            for (int j = 0; j < TOTAL_DAY_OF_WEEK; j++) {
                tvArrDays[i][j].initType();
            }
        }

        Calendar calTempDate = Calendar.getInstance();
        calTempDate.setTime(mCalCurrentMonth.getTime());

        // 해당월에 주가 몇개있는지 확인하기 위해 마지막 날로 이동
        calTempDate.add(Calendar.MONTH, 1);
        calTempDate.set(Calendar.DATE, 1);
        calTempDate.add(Calendar.DATE, -1);

        int nWeekCount = calTempDate.get(Calendar.WEEK_OF_MONTH);
        String strLastDay = sdf.format(calTempDate.getTime());
        int nThisMonth = calTempDate.get(Calendar.MONTH);

        binding.layoutWeek5.setVisibility(View.GONE);
        binding.layoutWeek6.setVisibility(View.GONE);

        if (nWeekCount > 4)
            binding.layoutWeek5.setVisibility(View.VISIBLE);
        else
            binding.layoutWeek5.setVisibility(View.GONE);

        if (nWeekCount > 5)
            binding.layoutWeek6.setVisibility(View.VISIBLE);
        else
            binding.layoutWeek6.setVisibility(View.GONE);

        // 날짜를 그린다.
        calTempDate.set(Calendar.DATE, 1);
        calTempDate.add(Calendar.DATE, -calTempDate.get(Calendar.DAY_OF_WEEK) + 1);

        int nDisplayDay;

        for (int i = 0; i < nWeekCount; i++) {
            for (int j = 0; j < TOTAL_DAY_OF_WEEK; j++) {
                try {
                    nDisplayDay = calTempDate.get(Calendar.DAY_OF_MONTH);

                    String strTempDate = sdf.format(calTempDate.getTime());
                    int nTempMonth = calTempDate.get(Calendar.MONTH);

                    if (nThisMonth != nTempMonth) {
                        tvArrDays[i][j].setVisibility(INVISIBLE);
                    } else {
                        tvArrDays[i][j].setTag(strTempDate);
                        tvArrDays[i][j].setText(String.valueOf(nDisplayDay));
                    }

                    calTempDate.add(Calendar.DATE, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setDate(Calendar selectedMonth) {
        mCalCurrentMonth.setTime(selectedMonth.getTime());

        initCalendarDay();
    }

    /**
     * 입력된 데이터를 가져온다.
     */
    public ArrayList<String> getData() {
        ArrayList<String> list = new ArrayList<>();
        int Sum = 0;
        for (int i = 0; i < TOTAL_WEEK; i++) {
            for (int j = 0; j < TOTAL_DAY_OF_WEEK; j++) {
                if (tvArrDays[i][j].getVisibility() == INVISIBLE)
                    continue;
                String day = tvArrDays[i][j].getQuantity();
                try {
                    Sum += Integer.parseInt(day);
                }catch (NumberFormatException e){
                    day.equals("0");
                }
                list.add(day);
                setTotal(list);
            }
        }
        return list;
    }

    public int setTotal(ArrayList<String> list) {
        int Sum = 0;
        for (int i = 0; i < list.size(); i++) {
            try {
                Sum += Integer.parseInt(list.get(i));
            }catch (NumberFormatException e){
                list.get(i).equals("0");
            }
        }
        return Sum;
    }

    /**
     * 데이터 설정
     *
     * @param list
     */
    public void setProductionPlan(ArrayList<String> list) {
        int index = 0;

        for (int i = 0; i < TOTAL_WEEK; i++) {
            for (int j = 0; j < TOTAL_DAY_OF_WEEK; j++) {
                if (tvArrDays[i][j].getVisibility() == INVISIBLE)
                    continue;

                if (list.size() <= index)
                    break;

                tvArrDays[i][j].setValue(list.get(index++));
            }
        }
    }
}
