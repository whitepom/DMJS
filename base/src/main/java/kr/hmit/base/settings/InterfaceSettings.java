package kr.hmit.base.settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import kr.hmit.base.BuildConfig;

public class InterfaceSettings {
    public static InterfaceSettings instance;

    public SettingsValue Value;

    //=====================================
    // Variable
    //=====================================

    private Context mContext;

    /**
     * SharedPreferences 데이터를 수정하고 싶을 때 사용
     */
    private static SharedPreferences.Editor editor;

    /**
     * Android 내부저장소
     */
    private static SharedPreferences preferences;


    public InterfaceSettings(Context context) {
        if (context == null)
            return;

        mContext = context;
        preferences = context.getSharedPreferences("smfactory", Activity.MODE_PRIVATE);
        editor = preferences.edit();
        Value = new SettingsValue();
        loadSettings();
    }

    private void loadSettings() {
        Value.LoginCID = getStringItem(SettingsKey.LoginCID, "");
        Value.LoginID = getStringItem(SettingsKey.LoginID, "");
        Value.LoginPW = getStringItem(SettingsKey.LoginPW, "");
        Value.AutoLogin = getBooleanItem(SettingsKey.AutoLogin, false);
        Value.SaveID = getBooleanItem(SettingsKey.SaveId,false);

        Value.MEM_01 = getStringItem(SettingsKey.MEM_01, "");
        Value.MEM_02 = getStringItem(SettingsKey.MEM_02, "");
        Value.MEM_51 = getStringItem(SettingsKey.MEM_51, "");
        Value.TKN_03 = getStringItem(SettingsKey.TKN_03, "");
        Value.MEM_99 = getStringItem(SettingsKey.MEM_99, "");


//        if(BuildConfig.DEBUG) {
//            Value.MEM_CID = "HUMAN";
//            Value.MEM_01 = "dyjung";
////            Value.TKN_03 = "0xB2AD575298093F903016CFBE231AC94D2E13175F";
//            Value.Host = "http://app.smfactory.kr";
//            Value.WebUrl = "http://nhm.smfactory.kr";
//        }

        Value.FileProviderPath = mContext.getPackageName() + ".fileprovider";

        // 기능별 사용여부
        // 고객관리
        Value.UseWareHousing = true;
        // A/S관리
        Value.UseProduction = true;
        // 자재주문요청
        Value.UseRelease = true;
        // 설비
        Value.UseFacilities = false;
        // 품질
        Value.UseQuality = false;
        // 재고
        Value.UseStock = true;



        Value.UseQR = false;
    }

    public static synchronized InterfaceSettings getInstance(Context context) {
        if (null == instance
                || null == preferences
                || null == editor) {
            instance = new InterfaceSettings(context);
        }

        return instance;
    }

    /************************ Method ***********************/

    public void putStringItem(String key, String value) {
        if (editor == null)
            return;

        editor.putString(key, value);
        editor.commit();
    }


    public String getStringItem(String key, String defaultValue) {
        if (preferences == null)
            return defaultValue;

        return preferences.getString(key, defaultValue);
    }


    public void putBooleanItem(String key, boolean value) {
        if (editor == null)
            return;

        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBooleanItem(String key, boolean defaultValue) {
        if (preferences == null)
            return defaultValue;

        return preferences.getBoolean(key, defaultValue);
    }


    public void putFloatItem(String key, float value) {
        if (editor == null)
            return;

        editor.putFloat(key, value);
        editor.commit();
    }

    public float getFloatItem(String key, float defaultValue) {
        if (preferences == null)
            return defaultValue;

        return preferences.getFloat(key, defaultValue);
    }

    public void putIntItem(String key, int value) {
        if (editor == null)
            return;

        editor.putInt(key, value);
        editor.commit();
    }


    public int getIntItem(String key, int defaultValue) {
        if (preferences == null)
            return defaultValue;

        return preferences.getInt(key, defaultValue);
    }


    public void putLongItem(String key, long value) {
        if (editor == null)
            return;

        editor.putLong(key, value);
        editor.commit();
    }


    public long getIntItem(String key, long defaultValue) {
        if (preferences == null)
            return defaultValue;

        return preferences.getLong(key, defaultValue);
    }
}
