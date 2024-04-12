package kr.hmit.dmjs.network;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.annimon.stream.function.Consumer;

import kr.hmit.dmjs.R;
import kr.hmit.dmjs.model.response.MEM_ReadModel;
import kr.hmit.dmjs.model.response.WKS_Model;
import kr.hmit.base.BaseApplication;
import kr.hmit.base.base_alret.BaseAlert;
import kr.hmit.base.model.BaseModel;
import kr.hmit.base.network.BaseConst;
import kr.hmit.base.network.ClsNetworkCheck;
import kr.hmit.base.network.HttpBaseService;
import kr.hmit.base.settings.InterfaceSettings;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestAPI {

    /**
     * 사용자리스트
     */
    public static void MEM_Read(Activity activity, Consumer<RestResult> callback) {
        InterfaceSettings settings = InterfaceSettings.getInstance(activity);

        if (!ClsNetworkCheck.isConnectable(activity)) {
            BaseAlert.show(activity, R.string.network_error_1);
            return;
        }

        BaseApplication.getInstance().openLoading(activity, null);

        Http.wks(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).MEM_Read(
                BaseConst.URL_HOST,
                settings.Value.MEM_CID,
                settings.Value.MEM_01,
                settings.Value.TKN_03
        ).enqueue(new Callback<MEM_ReadModel>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<MEM_ReadModel> call, Response<MEM_ReadModel> response) {
                Message msg = new Message();
                msg.obj = response;
                msg.what = 100;

                //=====================
                // response callback
                //=====================
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 100) {
                            BaseApplication.getInstance().closeLoading();

                            Response<MEM_ReadModel> response = (Response<MEM_ReadModel>) msg.obj;

                            if (response.isSuccessful()) {
                                try {
                                    callback.accept(RestResult.from(response.body()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                callback.accept(null);
                                BaseAlert.show(activity, activity.getString(R.string.network_error_2) + "-" + response.errorBody());
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<MEM_ReadModel> call, Throwable t) {
                BaseApplication.getInstance().closeLoading();
                call.cancel();
                callback.accept(null);
                BaseAlert.show(activity, R.string.network_error_2);
            }
        });
    }

    /**
     * 업무 분류
     */
    public static void WKS_05(Activity activity, Consumer<RestResult> callback) {
        InterfaceSettings settings = InterfaceSettings.getInstance(activity);

        if (!ClsNetworkCheck.isConnectable(activity)) {
            BaseAlert.show(activity, R.string.network_error_1);
            return;
        }

        BaseApplication.getInstance().openLoading(activity, null);

        Http.wks(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).WKS_05(
                BaseConst.URL_HOST,
                settings.Value.MEM_CID,
                settings.Value.MEM_01,
                settings.Value.TKN_03,
                settings.Value.MEM_CID
        ).enqueue(new Callback<WKS_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<WKS_Model> call, Response<WKS_Model> response) {
                Message msg = new Message();
                msg.obj = response;
                msg.what = 100;

                //=====================
                // response callback
                //=====================
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 100) {
                            BaseApplication.getInstance().closeLoading();

                            Response<WKS_Model> response = (Response<WKS_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                try {
                                    callback.accept(RestResult.from(response.body()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                callback.accept(null);
                                BaseAlert.show(activity, activity.getString(R.string.network_error_2) + "-" + response.errorBody());
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<WKS_Model> call, Throwable t) {
                BaseApplication.getInstance().closeLoading();
                call.cancel();
                callback.accept(null);
                BaseAlert.show(activity, R.string.network_error_2);
            }
        });
    }

    /**
     * 업무 상세
     *
     * @param wks_01 업무 ID
     */
    public static void WKS_Detail(Activity activity, Consumer<RestResult> callback, String wks_01) {
        InterfaceSettings settings = InterfaceSettings.getInstance(activity);
        String WKS_98="admin" ;
        int WKS_01='0';
        if (!ClsNetworkCheck.isConnectable(activity)) {
            BaseAlert.show(activity, R.string.network_error_1);
            return;
        }

        BaseApplication.getInstance().openLoading(activity, null);

        Http.wks(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).WKS_DETAIL(
                BaseConst.URL_HOST,
                settings.Value.MEM_CID,
                settings.Value.MEM_01,
                settings.Value.TKN_03,
                settings.Value.MEM_CID,
                String.valueOf(WKS_01),
                WKS_98
        ).enqueue(new Callback<WKS_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<WKS_Model> call, Response<WKS_Model> response) {
                Message msg = new Message();
                msg.obj = response;
                msg.what = 100;

                //=====================
                // response callback
                //=====================
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 100) {
                            BaseApplication.getInstance().closeLoading();

                            Response<WKS_Model> response = (Response<WKS_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                try {
                                    callback.accept(RestResult.from(response.body()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                callback.accept(null);
                                BaseAlert.show(activity, activity.getString(R.string.network_error_2) + "-" + response.errorBody());
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<WKS_Model> call, Throwable t) {
                BaseApplication.getInstance().closeLoading();
                call.cancel();
                callback.accept(null);
                BaseAlert.show(activity, R.string.network_error_2);
            }
        });
    }



    /**
     * 업무리스트
     */
   /* public static void WKS_Read(Activity activity, Consumer<RestResult> callback) {
        if (!ClsNetworkCheck.isConnectable(activity)) {
            BaseAlert.show(activity, R.string.network_error_1);
            return;
        }

        BaseApplication.getInstance().openLoading(activity, null);

        Http.wks(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).WKS_Read(
                BaseConst.URL_HOST,
                "SWENG",
                "admin",
                "0x34C3116EBF204EBF69455026A8CF5C2DCDBD244E",
                "M_LIST",
                "SWENG",
                "3","","",""
        ).enqueue(new Callback<WKS_Model>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<WKS_Model> call, Response<WKS_Model> response) {
                Message msg = new Message();
                msg.obj = response;
                msg.what = 100;

                //=====================
                // response callback
                //=====================
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 100) {
                            BaseApplication.getInstance().closeLoading();

                            Response<WKS_Model> response = (Response<WKS_Model>) msg.obj;

                            if (response.isSuccessful()) {
                                try {
                                    callback.accept(RestResult.from(response.body()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                callback.accept(null);
                                BaseAlert.show(activity, activity.getString(R.string.network_error_2) + "-" + response.errorBody());
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<WKS_Model> call, Throwable t) {
                BaseApplication.getInstance().closeLoading();
                call.cancel();
                callback.accept(null);
                BaseAlert.show(activity, R.string.network_error_2);
            }
        });
    }
*/
    /**
     * 업무관리 신규, 수정, 삭제
     *
     * @param activity
     * @param callback
     */
  /*  public static void WKS_U(Activity activity, Consumer<RestResult> callback) {
        if (!ClsNetworkCheck.isConnectable(activity)) {
            BaseAlert.show(activity, R.string.network_error_1);
            return;
        }

        BaseApplication.getInstance().openLoading(activity, null);

        Http.wks(HttpBaseService.TYPE.GET, BaseConst.URL_HOST).WKS_U(
                BaseConst.URL_HOST,
                "SWENG",
                "dyjung",
                "0x047210774B09793213F8BB8848BEA0E831294CA9",
                "M_INSERT",
                "HUMAN",
                "",
                "20200623",
                "2",
                "API테스트",
                "스마트공장",
                "",
                "TEST",
                "",
                "dyjung",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                0,
                "",
                "",
                "dyjung",
                ""
        ).enqueue(new Callback<BaseModel>() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                Message msg = new Message();
                msg.obj = response;
                msg.what = 100;

                //=====================
                // response callback
                //=====================
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 100) {
                            BaseApplication.getInstance().closeLoading();

                            Response<BaseModel> response = (Response<BaseModel>) msg.obj;

                            if (response.isSuccessful()) {
                                try {
                                    callback.accept(RestResult.from(response.body()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                callback.accept(null);
                                BaseAlert.show(activity, activity.getString(R.string.network_error_2) + "-" + response.errorBody());
                            }
                        }
                    }
                }.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                BaseApplication.getInstance().closeLoading();
                call.cancel();
                callback.accept(null);
                BaseAlert.show(activity, R.string.network_error_2);
            }
        });
    }*/
}
