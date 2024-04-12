package kr.hmit.base.base_fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import kr.hmit.base.BaseApplication;
import kr.hmit.base.settings.InterfaceSettings;

public class BaseFragment extends Fragment {
    protected Context mContext;
    protected Activity mActivity;
    protected InterfaceSettings mSettings;
//    protected InterfaceUser mUser;

    public BaseFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getContext();

        mActivity = getActivity();

        mSettings = InterfaceSettings.getInstance(mContext);
//        mUser = InterfaceUser.getInstance();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mContext = context;
            mActivity = (Activity) context;
            mSettings = InterfaceSettings.getInstance(mContext);
//            mUser = InterfaceUser.getInstance();
        }
    }

    //=========================
    // 로딩바 구현
    //=========================

    protected void openLoadingBar() {
        BaseApplication.getInstance().openLoading(getActivity(), null);
    }

    protected void closeLoadingBar() {
        BaseApplication.getInstance().closeLoading();
    }
}
