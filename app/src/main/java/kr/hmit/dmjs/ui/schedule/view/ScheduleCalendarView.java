package kr.hmit.dmjs.ui.schedule.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import kr.hmit.dmjs.databinding.ViewScheduleCalendarBinding;

public class ScheduleCalendarView extends LinearLayout {
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

    //=================================
    // region // interface
    //=================================
    private final OnClickListener onClickListener = new OnClickListener() {
        @SuppressLint("SimpleDateFormat")
        @Override
        public void onClick(View v) {
            if (!v.isEnabled())
                return;

            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 7; j++) {
                    if (tvArrDays[i][j].getId() == v.getId()) {
                        tvArrDays[i][j].setSelected(true);

                        try {
                            mCalSelectedDate.setTime(Objects.requireNonNull(sdf.parse((String) tvArrDays[i][j].getTag())));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (mOnDayClickListener != null)
                            mOnDayClickListener.onClick((String) tvArrDays[i][j].getTag());
                    } else {
                        tvArrDays[i][j].setSelected(false);
                    }
                }
            }
        }
    };

    public OnDayClickListener mOnDayClickListener;

    /**
     * 캘린더에 데이터 표시
     *
     * @param list
     */
    public void setData(ArrayList<String> list) {
        for (int i = 0; i < TOTAL_WEEK; i++) {
            for (int j = 0; j < TOTAL_DAY_OF_WEEK; j++) {
                tvArrDays[i][j].setExistSchedule(tvArrDays[i][j].getTag() != null && list.contains(tvArrDays[i][j].getTag().toString().replace("-", "")));
            }
        }
    }

    public interface OnDayClickListener {
        void onClick(String date);

        void onChangedMonth();
    }

    public void setOnDayClickListener(OnDayClickListener l) {
        mOnDayClickListener = l;
    }

    //=================================
    // endregion // interface
    //=================================

    //=====================================
    // region // view
    //=====================================

    ViewScheduleCalendarBinding binding;

    /**
     * 주 / 일
     */
    private CalendarTextView[][] tvArrDays;

    //=====================================
    // endregion // view
    //=====================================

    //=====================================
    // region // variable
    //=====================================

    private final Context mContext;
    private final Calendar mCalCurrentMonth = Calendar.getInstance();
    private final Calendar mCalSelectedDate = Calendar.getInstance();
    private final Calendar mCalToday = Calendar.getInstance();
    private SimpleDateFormat sdf;

    //=====================================
    // endregion // variable
    //=====================================


    //=====================================
    // region // initialize
    //=====================================

    //=====================================
    // endregion // initialize
    //=====================================


    public ScheduleCalendarView(Context context) {
        super(context);

        mContext = context;
    }

    public ScheduleCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        initLayout();

        initialize();
    }

    private void initLayout() {
        binding = ViewScheduleCalendarBinding.inflate(LayoutInflater.from(mContext), this, true);
        binding.imgPrev.setOnClickListener(v -> setMonth(-1));
        binding.imgNext.setOnClickListener(v -> setMonth(1));

        tvArrDays = new CalendarTextView[TOTAL_WEEK][TOTAL_DAY_OF_WEEK];
        for (int i = 0; i < TOTAL_WEEK; i++) {
            for (int j = 0; j < TOTAL_DAY_OF_WEEK; j++) {
                int resId = mContext.getResources().getIdentifier("tvDay" + i + j, "id", mContext.getPackageName());
                tvArrDays[i][j] = binding.getRoot().findViewById(resId);
                tvArrDays[i][j].setOnClickListener(onClickListener);
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void initialize() {
        sdf = new SimpleDateFormat("yyyy-MM-dd");

        mCalCurrentMonth.add(Calendar.MONTH, 1);
        mCalCurrentMonth.set(Calendar.DAY_OF_MONTH, 1);
        mCalCurrentMonth.add(Calendar.DAY_OF_MONTH, -1);
        mCalCurrentMonth.set(Calendar.HOUR_OF_DAY, 23);
        mCalCurrentMonth.set(Calendar.MINUTE, 59);
        mCalCurrentMonth.set(Calendar.SECOND, 59);
        setDate();
    }

    /**
     * 월 이동
     */
    private void setMonth(int amount) {
        mCalCurrentMonth.add(Calendar.MONTH, amount);

        setDate();

        if (mOnDayClickListener != null)
            mOnDayClickListener.onChangedMonth();
    }

    /**
     * 현재 선택한 달을 표시
     */
    @SuppressLint("SimpleDateFormat")
    private void setDate() {
        initCalendarDay();

        binding.tvDate.setText(new SimpleDateFormat("yyyy-MM").format(mCalCurrentMonth.getTime()));
    }

    /**
     * 달력 초기화
     */
    private void initCalendarDay() {
        // 초기화
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
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
        String strSelectedDate = sdf.format(mCalSelectedDate.getTime());

        for (int i = 0; i < nWeekCount; i++) {
            for (int j = 0; j < TOTAL_DAY_OF_WEEK; j++) {
                try {
                    nDisplayDay = calTempDate.get(Calendar.DAY_OF_MONTH);

                    String strTempDate = sdf.format(calTempDate.getTime());
                    int nTempMonth = calTempDate.get(Calendar.MONTH);

                    tvArrDays[i][j].setTag(strTempDate);
                    tvArrDays[i][j].setText(String.valueOf(nDisplayDay));

                    // 현재 날짜를 선택
                    if (tvArrDays[i][j].getTag().equals(sdf.format(mCalToday.getTime()))) {
                        tvArrDays[i][j].setIsToday(true);
                    }

                    if (strTempDate.equals(strSelectedDate))
                        tvArrDays[i][j].setSelected(true);

                    // 마지막날 체크
                    if (strLastDay.equals(strTempDate)) {
                    }

                    tvArrDays[i][j].setEnabled(nThisMonth == nTempMonth);

                    calTempDate.add(Calendar.DATE, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 선택된 날짜를 설정한다.
     */
    @SuppressLint("SimpleDateFormat")
    public String getSelectedDate() {
        return new SimpleDateFormat("yyyyMMdd").format(mCalSelectedDate.getTime());
    }

    @SuppressLint("SimpleDateFormat")
    public String getCurrentMonth() {
        return new SimpleDateFormat("yyyyMM").format(mCalCurrentMonth.getTime());
    }
}
