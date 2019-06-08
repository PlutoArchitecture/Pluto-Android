package com.minggo.pluto.api;

import android.os.Handler;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.minggo.pluto.common.AppContext;
import com.minggo.pluto.common.PlutoException;
import com.minggo.pluto.db.manager.DataManagerProxy;
import com.minggo.pluto.db.manager.DataManagerProxy.DataType;
import com.minggo.pluto.util.SharePreferenceUtils;
import com.minggo.pluto.model.Result;
import com.minggo.pluto.util.EncryptUtils;
import com.minggo.pluto.util.LogUtils;
import com.minggo.pluto.util.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 通用http接口成参数和响应处理工具
 *
 * @author minggo
 * @time 2014-12-2下午1:41:52
 */
public class PlutoApiEngine {

    /**
     * /**
     * 添加必传参数
     * 用于提交方式为GET的接口
     * mid  唯一识别码
     * pid  渠道号
     * imei 手机imei码
     * m_ver 手机型号
     * s_ver 系统版本号
     * versionName   包的版本名称
     * versionCode   包的版本号
     * pass 参数验证码  认证签名md5(pid+imei+versionCode+timestamp+ApiUrl.MD5KEY)
     *
     * @return params
     */
    public static Map<String, Object> addRequiredParam() {
        Map<String, Object> params = new HashMap<>();
        AppContext appContext = AppContext.getInstance();
        int versionCode = appContext.getPackageInfo().versionCode;
        String mid = appContext.getAndroidId(0);
        String imei = appContext.getAndroidIMEI();
        String m_ver = appContext.GetMobileVersion();

        String s_ver = appContext.GetAndroidVersion();
        String versionName = appContext.getPackageInfo().versionName;

        String systemTime = String.valueOf(System.currentTimeMillis());
        String timestamp;
        if (systemTime.length() < 10) {
            timestamp = systemTime;
        } else {
            timestamp = systemTime.substring(0, 10);
        }

        String imeiTime;
        DataManagerProxy dataManagerProxy = new DataManagerProxy(DataType.SHAREPREFERENCE);

        if (TextUtils.isEmpty(imeiTime = dataManagerProxy.queryByNameAndKey(SharePreferenceUtils.USER_CONFING, ApiUrl.IMEI_TIME,String.class))) {
            imeiTime = timestamp;
            dataManagerProxy.saveByNameAndKey(SharePreferenceUtils.USER_CONFING, ApiUrl.IMEI_TIME,imeiTime);
        }
        LogUtils.info("imeiTime-->" + imeiTime);
        try {

            String md5pass = new EncryptUtils().getMD5Str(imei + versionCode + timestamp + ApiUrl.MD5KEY);
            params.put(ApiUrl.PASS, md5pass);
            params.put(ApiUrl.MID, mid);
            params.put(ApiUrl.IMEI, imei);
            params.put(ApiUrl.TIMESTAMP, timestamp);
            params.put(ApiUrl.IMEI_TIME, imeiTime);
            params.put(ApiUrl.VERSIONCODE, versionCode);
            params.put(ApiUrl.VERSIONNAME, versionName);
            params.put(ApiUrl.M_VER, URLEncoder.encode(m_ver, "utf-8"));
            params.put(ApiUrl.S_VER, URLEncoder.encode(s_ver, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return params;
    }

    /**
     * 添加必传参数
     * 用于提交方式为POST的接口
     * mid  唯一识别码
     * pid  渠道号
     * imei 手机imei码
     * m_ver 手机型号
     * s_ver 系统版本号
     * versionName   包的版本名称
     * versionCode   包的版本号
     * pass 参数验证码  md5(pid+imei+versionCode+timestamp+ApiUrl.MD5KEY)
     *
     * @return params
     */
    public static Map<String, Object> addRequiredParamByPost() {
        Map<String, Object> params = new HashMap<>();
        AppContext appContext = AppContext.getInstance();
        int versionCode = appContext.getPackageInfo().versionCode;
        String mid = appContext.getAndroidId(0);
        String imei = appContext.getAndroidIMEI();
        String m_ver = appContext.GetMobileVersion();

        String s_ver = appContext.GetAndroidVersion();
        String versionName = appContext.getPackageInfo().versionName;
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);

        String imeiTime;
        DataManagerProxy dataManagerProxy = new DataManagerProxy(DataType.SHAREPREFERENCE);
        if (TextUtils.isEmpty(imeiTime = dataManagerProxy.queryByNameAndKey(SharePreferenceUtils.USER_CONFING, ApiUrl.IMEI_TIME,String.class))) {
            imeiTime = timestamp;
            dataManagerProxy.saveByNameAndKey(SharePreferenceUtils.USER_CONFING, ApiUrl.IMEI_TIME,imeiTime);
        }
        LogUtils.info("imeiTime-->" + imeiTime);
        try {

            String md5pass = new EncryptUtils().getMD5Str(imei + versionCode + timestamp + ApiUrl.MD5KEY);
            params.put(ApiUrl.PASS, md5pass);
            params.put(ApiUrl.MID, mid);
            params.put(ApiUrl.IMEI, imei);
            params.put(ApiUrl.TIMESTAMP, timestamp);
            params.put(ApiUrl.IMEI_TIME, imeiTime);
            params.put(ApiUrl.VERSIONCODE, versionCode);
            params.put(ApiUrl.VERSIONNAME, versionName);
            params.put(ApiUrl.M_VER, URLEncoder.encode(m_ver, "utf-8"));
            params.put(ApiUrl.S_VER, URLEncoder.encode(s_ver, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return params;
    }

    /**
     * 先显示缓存结果，请求网络后再刷新即时的数据
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param key     缓存key
     * @param handler 页面刷新handler
     * @param msgWhat 优先返回缓存的message的what
     * @return
     */
    public static <T> List<T> getListByCacheAdvance(String url, Map<String, Object> params, String key, Handler handler, int msgWhat, Class<T> clazz) {

        DataManagerProxy dataManagerProxy = new DataManagerProxy(DataType.FILECACHE);

        //PlutoFileCache cacheUtils = PlutoFileCache.getInstance();
        Gson gson = new Gson();
        String cacheContent = null;
        List<T> list = new ArrayList<T>();

        try {
            cacheContent = dataManagerProxy.queryData(key,String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!StringUtils.isEmpty(cacheContent) && cacheContent.contains("[") && cacheContent.contains("]")) {
            try {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(cacheContent);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        list.add(gson.fromJson(jsonArray.getString(i), clazz));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (list != null) {
                    handler.obtainMessage(msgWhat, list).sendToTarget();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!AppContext.isNetworkConnected(AppContext.getInstance().context)) {
            return null;
        }

        Result<List<T>> result = null;
        try {
            result = ApiClient.httpGetList(url, params);
        } catch (PlutoException e) {
            e.printStackTrace();
        }

        if (result == null) {
            return null;
        }
        if (!result.success || result.content == null || result.content.equals("")) {
            return null;
        }

        if (result.content != null) {
            //这样TypeToken没有明确的指定类是没办法解析的
            //Result<List<T>> result = gson.fromJson(json, new TypeToken<Result<List<T>>>(){}.getType());
            if (clazz == String.class || clazz == int.class || clazz == float.class || clazz == long.class) {//instanof 会有语法问题
                list = (List<T>) result.content;
            } else {

                String jsonlist = gson.toJson(result.content);
                List<T> tList = new ArrayList<>();
                if (!StringUtils.isEmpty(jsonlist) && jsonlist.contains("[") && jsonlist.contains("]")) {
                    try {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(jsonlist);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                tList.add(gson.fromJson(jsonArray.getString(i), clazz));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (tList != null) {
                            list = tList;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        // 添加SD卡缓存开始
        try {
            dataManagerProxy.saveData(key,gson.toJson(result.content));
            //cacheUtils.setDiskCache(key, gson.toJson(result.content));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 缓存没有过期，先显示缓存结果，没有缓存和过期后请求网络后再刷新即时的数据
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param key     缓存key
     * @param handler 页面刷新handler
     * @param msgWhat 优先返回缓存的message的what
     * @return
     */
    public static <T> List<T> getListByLimitTime(String url, Map<String, Object> params, String key, Handler handler, int msgWhat, int hour, Class<T> clazz) {
        DataManagerProxy dataManagerProxy = new DataManagerProxy(DataType.FILECACHE);

        //PlutoFileCache cacheUtils = PlutoFileCache.getInstance();
        Gson gson = new Gson();
        String cacheContent = null;
        List<T> list = new ArrayList<>();

        try {
            //获取缓存
            //cacheContent = cacheUtils.getDiskCache(key);
            cacheContent = dataManagerProxy.queryData(key,String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!StringUtils.isEmpty(cacheContent) && cacheContent.contains("[") && cacheContent.contains("]")) {

            try {
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(cacheContent);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        list.add(gson.fromJson(jsonArray.getString(i), clazz));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //缓存不是空数组
                if (!list.isEmpty()) {
                    if (!dataManagerProxy.isExpiredFile(key, 60 * hour)) {
                        handler.obtainMessage(msgWhat, list).sendToTarget();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(StringUtils.isEmpty(cacheContent)||!cacheContent.contains("[") && !cacheContent.contains("]")||dataManagerProxy.isExpiredFile(key, 60 * hour)){
            if (!AppContext.isNetworkConnected(AppContext.getInstance().context)) {
                return null;
            }

            Result result = null;
            try {
                //网络请求
                result = ApiClient.httpGetList(url, params);
            } catch (PlutoException e) {
                e.printStackTrace();
            }

            if (result == null) {
                return null;
            }
            if (!result.success || result.content == null || result.content.equals("")) {
                return null;
            }
            if (result.content != null) {
                //这样TypeToken没有明确的指定类是没办法解析的
                //Result<List<T>> result = gson.fromJson(json, new TypeToken<Result<List<T>>>(){}.getType());
                if (clazz == String.class || clazz == int.class || clazz == float.class || clazz == long.class) {
                    list = (List<T>) result.content;
                } else {

                    String jsonlist = gson.toJson(result.content);
                    List<T> tList = new ArrayList<>();
                    if (!StringUtils.isEmpty(jsonlist) && jsonlist.contains("[") && jsonlist.contains("]")) {
                        try {
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = new JSONArray(jsonlist);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    tList.add(gson.fromJson(jsonArray.getString(i), clazz));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (tList != null) {
                                list = tList;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            // 添加SD卡缓存开始
            try {
                dataManagerProxy.saveData(key,gson.toJson(result.content));
                //cacheUtils.setDiskCache(key, gson.toJson(result.content));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return list;
    }

    /**
     * 实时网络请求，不涉及缓存
     *
     * @param <T>    解析成列表的对象类型
     * @param url    请求地址
     * @param params 请求参数
     * @return 正常返回对象列表，其他情况返回长度0的对象列表
     */
    public static <T> List<T> getListByNoCache(String url, Map<String, Object> params, Class<T> clazz) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<>();

        if (!AppContext.isNetworkConnected(AppContext.getInstance().context)) {
            return list;
        }

        Result<List<T>> result = null;
        try {
            result = ApiClient.httpGetList(url, params);
        } catch (PlutoException e) {
            e.printStackTrace();
        }
        //返回空列表
        if (result == null) {
            return list;
        }
        if (!result.success || result.content == null || result.content.equals("")) {
            return list;
        }
        if (result.content != null) {
            //这样TypeToken没有明确的指定类是没办法解析的
            //Result<List<T>> result = gson.fromJson(json, new TypeToken<Result<List<T>>>(){}.getType());
            if (clazz == String.class || clazz == int.class || clazz == float.class || clazz == long.class) {
                list = result.content;
            } else {

                String jsonlist = gson.toJson(result.content);
                List<T> tList = new ArrayList<>();
                if (!StringUtils.isEmpty(jsonlist) && jsonlist.contains("[") && jsonlist.contains("]")) {
                    try {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(jsonlist);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                tList.add(gson.fromJson(jsonArray.getString(i), clazz));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (tList != null) {
                            list = tList;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        return list;
    }

    /**
     * 先显示缓存结果，请求网络后再刷新即时的数据
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param key     缓存key
     * @param handler 页面刷新handler
     * @param msgWhat 优先返回缓存的message的what
     * @return
     */
    public static <T> T getModelByCacheAdvance(String url, Map<String, Object> params, String key, Handler handler, int msgWhat, Class<T> clazz) {

        DataManagerProxy dataManagerProxy = new DataManagerProxy(DataType.FILECACHE);

        //PlutoFileCache cacheUtils = PlutoFileCache.getInstance();
        Gson gson = new Gson();
        String cacheContent = null;
        T t = null;

        try {
            cacheContent = dataManagerProxy.queryData(key,String.class);
            //cacheContent = cacheUtils.getDiskCache(key);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!StringUtils.isEmpty(cacheContent)) {
            try {

                t = gson.fromJson(cacheContent, clazz);
                if (t != null) {
                    handler.obtainMessage(msgWhat, t).sendToTarget();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!AppContext.isNetworkConnected(AppContext.getInstance().context)) {
            return null;
        }

        Result<T> result = null;
        try {
            result = ApiClient.httpGetModel(url, params, true);
        } catch (PlutoException e) {
            e.printStackTrace();
        }

        if (result == null) {
            return null;
        }
        if (!result.success || result.content == null || result.content.equals("")) {
            return null;
        }
        try {
            t = gson.fromJson(gson.toJson(result.content), clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 添加SD卡缓存开始
        try {
            dataManagerProxy.saveData(key,gson.toJson(result.content));
            //cacheUtils.setDiskCache(key, gson.toJson(result.content));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }


    /**
     * 缓存没过期先显示缓存,请求网络数据再刷新
     *
     * @param url     地址
     * @param params  参数
     * @param key     缓存key
     * @param handler 优先传回的handler
     * @param msgWhat 消息类型
     * @param hour    缓存时间
     * @param clazz   返回类型
     * @param <T>     返回泛型
     * @return
     */
    public static <T> T getModelByGetLimitTime(String url, Map<String, Object> params, String key, Handler handler, int msgWhat, int hour, Class<T> clazz) {
        //PlutoFileCache cacheUtils = PlutoFileCache.getInstance();
        DataManagerProxy dataManagerProxy = new DataManagerProxy(DataType.FILECACHE);
        Gson gson = new Gson();
        String cacheContent = null;
        T t = null;

        try {
            //cacheContent = cacheUtils.getDiskCache(key);
            cacheContent = dataManagerProxy.queryData(key,String.class);
            t = StringUtils.isEmpty(cacheContent) ? null : gson.fromJson(cacheContent, clazz);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (t != null) {
            if (!dataManagerProxy.isExpiredFile(key, hour * 60)) {
                handler.obtainMessage(msgWhat, t).sendToTarget();
            }
        }else if(t == null||dataManagerProxy.isExpiredFile(key, hour * 60)){
            if (!AppContext.isNetworkConnected(AppContext.getInstance().context)) {
                return t;
            }

            Result<T> result = null;
            try {
                result = (Result<T>) ApiClient.httpGetModel(url, params, true);
            } catch (PlutoException e) {
                e.printStackTrace();
            }

            if (result == null) {
                return t;
            }
            if (!result.success || result.content == null || result.content.equals("")) {
                return t;
            }
            try {
                t = gson.fromJson(gson.toJson(result.content), clazz);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 添加SD卡缓存开始
            try {
                dataManagerProxy.saveData(key,gson.toJson(result.content));
                //cacheUtils.setDiskCache(key, gson.toJson(result.content));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return t;
    }


    /**
     * 紧紧获取网络实时对象
     *
     * @param url
     * @param params
     * @param clazz
     * @return
     */
    public static <T> T getModelByNoCache(String url, Map<String, Object> params, Class<T> clazz) {
        Gson gson = new Gson();
        T t = null;

        if (!AppContext.isNetworkConnected(AppContext.getInstance().context)) {
            return t;
        }

        Result<T> result = null;
        try {
            result = (Result<T>) ApiClient.httpGetModel(url, params, true);
        } catch (PlutoException e) {
            e.printStackTrace();
        }

        if (result == null) {
            return t;
        }
        if (!result.success || result.content == null || result.content.equals("")) {
            return t;
        }
        try {
            t = gson.fromJson(gson.toJson(result.content), clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }


    /**
     * 只获取网络实时|Result
     *
     * @param url
     * @param params
     * @return
     */
    public static Result getResultOnly(String url, Map<String, Object> params) {
        Result result = null;
        if (!AppContext.isNetworkConnected(AppContext.getInstance().context)) {
            return null;
        }
        try {
            result = ApiClient.httpGetModel(url, params, true);
            params = null;
        } catch (PlutoException e) {
            e.printStackTrace();
        }
        if (result == null) {
            return null;
        }
        return result;
    }


    /**
     * 发送文件
     *
     * @param url
     * @param params
     * @param files
     * @return
     */
    public static Result postFilesResult(String url, Map<String, Object> params, Map<String, File> files) {
        Result result = null;

        if (!AppContext.isNetworkConnected(AppContext.getInstance().context)) {
            return null;
        }
        try {
            result = ApiClient.httpPostModel(url, params, files, false);
            params = null;
            files = null;
        } catch (PlutoException e) {
            e.printStackTrace();
        }
        if (result == null) {
            return null;
        }

        if (!result.success || result.content == null || result.content.equals("")) {
            return null;
        }

        return result;
    }

    /**
     * 紧紧获取网络实时对象
     *
     * @param url
     * @param params
     * @param clazz
     * @return
     */
    public static <T> T postModelUploadFiles(String url, Map<String, Object> params,Map<String, File> files, Class<T> clazz) {
        Gson gson = new Gson();
        T t = null;

        if (!AppContext.isNetworkConnected(AppContext.getInstance().context)) {
            return t;
        }

        Result<T> result = null;
        try {
            result = (Result<T>) ApiClient.httpPostModel(url, params,files, false);
        } catch (PlutoException e) {
            e.printStackTrace();
        }

        if (result == null) {
            return t;
        }
        if (!result.success || result.content == null) {
            return t;
        }
        try {
            t = gson.fromJson(gson.toJson(result.content), clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }

    /**
     * 发送纯粹post
     *
     * @param url
     * @param params
     * @return
     */
    public static Result postResult(String url, Map<String, Object> params) {
        Result result = null;

        if (!AppContext.isNetworkConnected(AppContext.getInstance().context)) {
            return null;
        }
        try {
            result = ApiClient.httpPostModel(url, params, null, false);
            params = null;
        } catch (PlutoException e) {
            e.printStackTrace();
        }
        if (result == null) {
            return null;
        }

        if (!result.success) {
            return null;
        }

        return result;
    }

    /**
     * 紧紧获取网络实时对象
     *
     * @param url
     * @param params
     * @param clazz
     * @return
     */
    public static <T> T postModelByNoCache(String url, Map<String, Object> params, Class<T> clazz) {
        Gson gson = new Gson();
        T t = null;

        if (!AppContext.isNetworkConnected(AppContext.getInstance().context)) {
            return t;
        }

        Result<T> result = null;
        try {
            result = (Result<T>) ApiClient.httpPostModel(url, params,null, false);
        } catch (PlutoException e) {
            e.printStackTrace();
        }

        if (result == null) {
            return t;
        }
        if (!result.success || result.content == null) {
            return t;
        }
        try {
            t = gson.fromJson(gson.toJson(result.content), clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }



}
