package com.versionupdate.demo;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by kangdongpu on 2016/12/23.
 */
public class SPUtils {

    public static final String DEMO_INFO = "demo_info";
    public static final String DOWN_APK_ID = "down_apk_id";


    private static SPUtils instance;
    private Context mContext;
    private SharedPreferences sp;

    private SharedPreferences.Editor editor;


    public static SPUtils getInstance() {
        return instance == null ? instance = new SPUtils() : instance;
    }

    public void init(Context context) {
        this.mContext = context;
        sp = mContext.getSharedPreferences(DEMO_INFO, Context.MODE_PRIVATE);
    }

    public void setApkId(long apkId) {
        editor = sp.edit();
        editor.putLong(DOWN_APK_ID, apkId);
        editor.commit();
    }

    public long getApkId() {
        return sp.getLong(DOWN_APK_ID, -1L);
    }

    public void clear() {
        editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
