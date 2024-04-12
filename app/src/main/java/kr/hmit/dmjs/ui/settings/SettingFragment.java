package kr.hmit.dmjs.ui.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import kr.hmit.dmjs.R;
import kr.hmit.base.base_fragment.BaseFragment;
import kr.hmit.base.broadcast_action.ClsBroadCast;
import kr.hmit.base.settings.SettingsKey;

public class SettingFragment extends BaseFragment {
    //===========================
    // Layout
    //===========================
    private View view;
    private Button btnProfile;
    private Button btnLogout;
    private Button btnGoHomepage;

    //===========================
    // Initialize
    //===========================
    public SettingFragment() {
        // Required empty public constructor
    }

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_setting, container, false);

        initLayout();

        return view;
    }

    /**
     * 레이아웃 초기화
     */
    private void initLayout() {
        btnProfile = view.findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(v -> goProfileMain());

        btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> logout());

        btnGoHomepage = view.findViewById(R.id.btnGoHomepage);
        btnGoHomepage.setOnClickListener(v -> goHomepage());
    }

    /**
     * 홈페이지로 이동
     */
    private void goHomepage() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.naver.com"));
        mContext.startActivity(intent);
    }

    /**
     * 프로필 메인
     */
    private void goProfileMain() {
//        Intent intent = new Intent(mContext, ProfileMain.class);
//        mContext.startActivity(intent);
    }

    /**
     * 로그아웃
     */
    private void logout() {
        mSettings.Value.AutoLogin = false;
        mSettings.putBooleanItem(SettingsKey.AutoLogin, mSettings.Value.AutoLogin);

        mSettings.Value.LoginID = "";
        mSettings.putStringItem(SettingsKey.LoginID, mSettings.Value.LoginID);

        mSettings.Value.LoginPW = "";
        mSettings.putStringItem(SettingsKey.LoginPW, mSettings.Value.LoginPW);

        openLoadingBar();
        new Handler().postDelayed(() -> {
            closeLoadingBar();
            Intent intent = new Intent(ClsBroadCast.BROAD_CAST_ACTION_LOGOUT);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
        }, 1000);
    }
}
