package kr.hmit.dmjs.ui.schedule.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import kr.hmit.dmjs.R;

@SuppressLint("AppCompatCustomView")
public class CalendarTextView extends TextView {
    //=================================
    // final
    //=================================
    /**
     * 라운드 크기 조절용
     */
    private final int ADDED_WIDTH = 0;

    //=================================
    // variable
    //=================================
    private Context mContext;
    /**
     * 프로그램 상태여부
     */
    private boolean mExistSchedule = false;
    /**
     * 달력에서 오늘
     */
    private boolean mIsToday = false;
    /**
     * 일의 컬러
     */
    private ColorStateList mDayTextColor;

    //==============================
    // onDraw
    //==============================
    private Paint paint;
    /**
     * 원의 크기
     */
    private RectF mRectCircle;

    //=================================
    // initialize
    //=================================
    public CalendarTextView(Context context) {
        super(context);

        mContext = context;
    }

    public CalendarTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        initialize();
    }

    public CalendarTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;

        initialize();
    }

    private void initialize() {
        mDayTextColor = ContextCompat.getColorStateList(mContext, R.color.selector_calendar_day_grey);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * 초기화
     */
    public void initType() {
        setText("");
        setTag(null);
        setSelected(false);
        setActivated(false);
        setEnabled(true);

        setTextColor(mDayTextColor);

        this.mIsToday = false;
        this.mExistSchedule = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mRectCircle == null) {
            mRectCircle = new RectF((getWidth() / 2f) - (getTextSize() + ADDED_WIDTH),
                    (getHeight() / 2f) - (getTextSize() + ADDED_WIDTH),
                    (getWidth() / 2f) + (getTextSize() + ADDED_WIDTH),
                    (getHeight() / 2f) + (getTextSize() + ADDED_WIDTH));
        }

        if (isSelected() && isEnabled()) {
            paint.setColor(Color.parseColor("#a8c3e8"));
            canvas.drawCircle(mRectCircle.centerX(), mRectCircle.centerY(), mRectCircle.width() / 2.0f, paint);
        }

        if (mExistSchedule) {
            paint.setColor(Color.parseColor("#a8c3e8"));
            float fPoint = dpToPx(mContext, 3);
            canvas.drawCircle(mRectCircle.centerX(), fPoint * 3, fPoint, paint);
        }

        super.onDraw(canvas);
    }

    public int dpToPx(Context context, float dp) {
        int px = 0;
        context.getResources();

        px = (int) (dp * Resources.getSystem().getDisplayMetrics().density);
        return px;
    }

    /**
     * 스케쥴 존재여부
     * @param exist
     */
    public void setExistSchedule(boolean exist) {

        mExistSchedule = exist;
        invalidate();
    }

    //===============================
    // Methods
    //===============================

    /**
     * 오늘인지 여부
     *
     * @param isToday
     */
    public void setIsToday(boolean isToday) {
        this.mIsToday = isToday;

        if (isToday) {
            setTextColor(ContextCompat.getColorStateList(mContext, R.color.selector_calendar_day_today));
        }
    }

    /**
     * 텍스트 컬러값을 변경한다.
     *
     * @param colorStateList
     */
    public void setTextColorValue(ColorStateList colorStateList) {
        mDayTextColor = colorStateList;
    }
}
