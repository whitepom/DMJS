package kr.hmit.dmjs.ui.menu;

import android.os.Bundle;

import kr.hmit.dmjs.R;
import kr.hmit.base.base_activity.BaseActivity;

public class MenuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initLayout();

        initialize();
    }

    @Override
    protected void initLayout() {

    }

    @Override
    protected void initialize() {

    }
}
