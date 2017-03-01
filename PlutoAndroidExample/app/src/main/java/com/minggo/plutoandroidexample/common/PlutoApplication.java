package com.minggo.plutoandroidexample.common;

import android.app.Application;

import com.minggo.pluto.Pluto;
import com.minggo.pluto.Pluto.DBConfig;

/**
 * Created by minggo on 2017/1/25.
 */

public class PlutoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        bindPluto();
    }

    private void bindPluto(){
        Pluto.initPluto(this);

        //以下配置为可选
        Pluto.APP_CACHE_FILE = "com.pluto.example";
        Pluto.LOG_SHOW = true;
        Pluto.URL_DOMAIN = "https://m8en.com";
        DBConfig.NAME = "com.pluto.example.db";
        DBConfig.VERSION = 1;
    }
}
