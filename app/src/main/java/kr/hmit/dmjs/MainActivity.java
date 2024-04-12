package kr.hmit.dmjs;

import android.os.Bundle;
import android.util.Log;

import kr.hmit.dmjs.model.response.MEM_ReadModel;
import kr.hmit.dmjs.model.response.WKS_Model;
import kr.hmit.dmjs.network.RequestAPI;
import kr.hmit.base.base_activity.BaseActivity;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();
    }

    @Override
    protected void initLayout() {

        RequestAPI.MEM_Read(mActivity, restResult -> {
            if (restResult != null) {
                MEM_ReadModel model = (MEM_ReadModel) restResult.mData;

                Log.d("Test", "MEM_Read-" + model.Total + " - " + model.Data.get(0).MEM_32_NM);
            } else {

            }
        });

    }

    @Override
    protected void initialize() {

    }

}
