package com.minggo.pluto;

import android.content.Context;
import android.os.Environment;

import com.minggo.pluto.api.ApiUrl;
import com.minggo.pluto.common.AppContext;

/**
 * Pluto 框架配置
 * Created by minggo on 2017/1/3.
 */
public class PlutoConfig {
    /**
     * 域名
     */
    public static final String URL_DOMAIN = "http://m8en.com:8877/";
    //public static final String URL_DOMAIN = "http://10.10.2.14:8080/";
    /**
     * 项目缓存目录
     */
    public static final String APP_CACHE_FILE = "com.minggo.charmword";
    /**
     * 调试信息打印
     */
    public static final boolean LOG_SHOW = true;
    /**
     * 主程序SD卡目录
     */
    public static final String SDPATH = Environment.getExternalStorageDirectory().getPath() + "/"+APP_CACHE_FILE+"/";
    /**
     * 便于统一finalbitmap修改保存路径
     */
    public static final String FINAL_BIMAP_SAVE_PATH = APP_CACHE_FILE+"/bookpic";
    /**
     * 下载原图路径   便于统一finalbitmap修改保存路径
     */
    public static final String FINAL_ORIGINAL_BIMAP_SAVE_PATH = FINAL_BIMAP_SAVE_PATH + "/original";

    /**
     * 数据库的配置
     */
    public final class DBConfig {
        // 数据库名称
        public static final String NAME = "minggo.charmword";
        // 数据库版本
        public static final int VERSION = 2;
    }

    //必须在自己Application类中先初始化
    public static void initPluto(Context context){
        AppContext.getInstance().context = context;
        ApiUrl.URL_DOMAIN = URL_DOMAIN;
    }
}
