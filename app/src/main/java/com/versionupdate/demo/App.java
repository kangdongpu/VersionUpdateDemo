package com.versionupdate.demo;

import android.app.Application;

/**
 * Created by kangdongpu on 2017/2/16.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SPUtils.getInstance().init(this);
    }
}
