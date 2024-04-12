package kr.hmit.base;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;

public class BaseApplication extends Application {

    private static BaseApplication baseApplication;
    private ProgressDialog progressDialog;

    public static BaseApplication getInstance() {
        return baseApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        baseApplication = this;
    }

    public void openLoading(Activity activity, String message) {
        if (activity == null || activity.isFinishing()) {
            return;
        }

        if (progressDialog != null && progressDialog.isShowing()) {

        } else {
            progressDialog = ProgressDialog.show(activity, "", "Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public void closeLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
