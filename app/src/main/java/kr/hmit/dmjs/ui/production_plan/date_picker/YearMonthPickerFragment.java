package kr.hmit.dmjs.ui.production_plan.date_picker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import kr.hmit.dmjs.databinding.DialogDatePickerBinding;

public class YearMonthPickerFragment extends DialogFragment {
    private static final String SELECTED_MONTH = "SELECTED_MONTH";
    private static final int MAX_YEAR = 2025;
    private static final int MIN_YEAR = 2020;

    //============================
    // region // interface
    //============================

    public interface OnDateSetListener {
        void onDateSet(int year, int month);
    }

    //============================
    // endregion // interface
    //============================
    private OnDateSetListener mListener;
    public Calendar mCalSelected = Calendar.getInstance();

    public void setListener(OnDateSetListener listener) {
        this.mListener = listener;
    }

    //============================
    // region // view
    //============================

    private DialogDatePickerBinding mBinding;

    //============================
    // endregion // view
    //============================

    public static YearMonthPickerFragment newInstance(Calendar calendar) {
        Bundle b = new Bundle();
        b.putSerializable(SELECTED_MONTH, calendar);

        YearMonthPickerFragment fragment = new YearMonthPickerFragment();
        fragment.setArguments(b);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DialogDatePickerBinding.inflate(getLayoutInflater(), container, false);

        initLayout();

        initialize();

        return mBinding.getRoot();
    }

    private void initLayout() {
        mBinding.btnCancel.setOnClickListener(v -> dismiss());
        mBinding.btnConfirm.setOnClickListener(v -> {
            mListener.onDateSet(mBinding.pickerYear.getValue(), mBinding.pickerMonth.getValue());
            dismiss();
        });
    }

    /**
     * 초기화
     */
    private void initialize() {
        Calendar calTemp = (Calendar) getArguments().getSerializable(SELECTED_MONTH);
        if (calTemp != null)
            mCalSelected.setTime(calTemp.getTime());

        mBinding.pickerYear.setMinValue(MIN_YEAR);
        mBinding.pickerYear.setMaxValue(MAX_YEAR);
        mBinding.pickerYear.setValue(mCalSelected.get(Calendar.YEAR));

        mBinding.pickerMonth.setMinValue(1);
        mBinding.pickerMonth.setMaxValue(12);
        mBinding.pickerMonth.setValue(mCalSelected.get(Calendar.MONTH) + 1);
    }
}
