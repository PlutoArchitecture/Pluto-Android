package com.minggo.pluto;

import android.content.Context;
import android.os.Environment;

import com.minggo.pluto.api.ApiUrl;
import com.minggo.pluto.common.AppContext;
import com.minggo.pluto.common.PlutoException;
import com.minggo.pluto.util.LogUtils;

/**
 * Pluto 框架配置
 * Created by minggo on 2017/1/3.
 */
public class Pluto {
    /**
     * 域名
     */
    public static String URL_DOMAIN = "http://m8en.com:8877/";
    //public static final String URL_DOMAIN = "http://10.10.2.14:8080/";
    /**
     * 项目缓存目录
     */
    public static String APP_CACHE_FILE = "com.minggo.pluto";
    /**
     * 调试信息打印
     */
    public static boolean LOG_SHOW = true;
    /**
     * 主程序SD卡目录
     */
    public static String SDPATH = Environment.getExternalStorageDirectory().getPath() + "/" + APP_CACHE_FILE + "/";
    /**
     * 便于统一finalbitmap修改保存路径
     */
    public static String FINAL_BIMAP_SAVE_PATH = APP_CACHE_FILE + "/bookpic";
    /**
     * 下载原图路径   便于统一finalbitmap修改保存路径
     */
    public static String FINAL_ORIGINAL_BIMAP_SAVE_PATH = FINAL_BIMAP_SAVE_PATH + "/original";

    /**
     * 数据库的配置
     */
    public static class DBConfig {
        // 数据库名称
        public static String NAME = "com.minggo.pluto";
        // 数据库版本
        public static int VERSION = 2;
    }

    /**
     *  加密key
     */
    public static String MD5KEY = "minggo";

    //必须在自己Application类中先初始化
    public static void initPluto(Context context) {
        AppContext.getInstance().context = context;
        ApiUrl.URL_DOMAIN = URL_DOMAIN;
        ApiUrl.MD5KEY=MD5KEY;
        SDPATH = Environment.getExternalStorageDirectory().getPath() + "/" + APP_CACHE_FILE + "/";

        if (!BuildConfig.DEBUG) {
            LogUtils.info("plutoexception", ">>>>>init");
            //PlutoException.getAppExceptionHandler(context);
        }
    }
}
