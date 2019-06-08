package com.minggo.pluto.logic;


import android.os.Handler;

import com.minggo.pluto.api.PlutoApiEngine;
import com.minggo.pluto.common.CommonAsyncTask;
import com.minggo.pluto.model.Result;
import com.minggo.pluto.util.LogUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.minggo.pluto.logic.LogicManager.LogicType.*;
import static com.minggo.pluto.logic.LogicManager.NetworkRequestType.*;
import static com.minggo.pluto.logic.LogicManager.ReturnDataType.*;


/**
 * Created by minggo on 2017/2/21.
 */

public class LogicManager extends CommonAsyncTask<Object, Void, Object> {

    public static final String TAG = "LOGICMANAGER";

    private int what;
    private int arg1;
    private int arg2;
    private String url;
    private String cacheKey;
    private int limitedTime;
    private Handler handler;
    private Class clazz;
    private ReturnDataType returnDataType;
    private LogicType logicType;
    private NetworkRequestType networkRequestType;
    private Map<String,Object> requestParam;
    private Map<String,File> files;


    //根据实际情况后续添加
    public enum LogicType{
        CACHE_ADVANCE_AND_NETWORK_RETURN,CACHE_EXPIRED_AND_NETWORK_RETURN,ONLY_NETWORK,UPLOALD_FILE;
    }
    public enum NetworkRequestType{
        POST,GET;
    }

    public enum ReturnDataType{
        MODEL,LIST;
    }

    public enum LogicManagerType{

        GET__MODEL__CACHE_ADVANCE_AND_NETWORK_RETURN,
        GET__MODEL__CACHE_EXPIRED_AND_NETWORK_RETURN,
        GET__MODEL__ONLY_NETWORK,

        POST__MODEL__CACHE_ADVANCE_AND_NETWORK_RETURN,
        POST__MODEL__CACHE_EXPIRED_AND_NETWORK_RETURN,
        POST__MODEL__ONLY_NETWORK,
        POST__MODEL__UPLOALD_FILE,

        GET__LIST__CACHE_ADVANCE_AND_NETWORK_RETURN,
        GET__LIST__CACHE_EXPIRED_AND_NETWORK_RETURN,
        GET__LIST__ONLY_NETWORK,

        POST__LIST__CACHE_ADVANCE_AND_NETWORK_RETURN,
        POST__LIST__CACHE_EXPIRED_AND_NETWORK_RETURN,
        POST__LIST__ONLY_NETWORK;

    }

    /**
     * 业务逻辑父类构造方法
     * @param handler
     * @param clazz List<Clazz<T>>中的Class<T>；或者直接是Class<T>
     * @param logicManagerType 12中枚举类型
     * @param <T>
     */
    public <T> LogicManager(Handler handler, Class<T> clazz,LogicManagerType logicManagerType){
        this.handler = handler;
        this.clazz = clazz;
        splitEnum(logicManagerType);
        requestParam = new HashMap<String,Object>();
    }

    private void splitEnum(LogicManagerType logicManagerType){
        String[] logicManagerTypeList = logicManagerType.toString().split("__");
        networkRequestType = NetworkRequestType.valueOf(logicManagerTypeList[0]);
        returnDataType = ReturnDataType.valueOf(logicManagerTypeList[1]);
        logicType = LogicType.valueOf(logicManagerTypeList[2]);
    }


    public LogicManager setParamClass(Class clazz){

        try {
            what = clazz.getDeclaredField("WHAT").getInt(clazz);
            LogUtils.info(TAG,">>>>what="+what);
            url = clazz.getDeclaredField("URL").get(clazz).toString();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return this;
    }

    private LogicManager setWhat(int what){
        this.what = what;
        return this;
    }

    public LogicManager setArg1(int arg1){
        this.arg1 = arg1;
        return this;
    }

    public LogicManager setArg2(int arg2){
        this.arg2 = arg2;
        return this;
    }

    private LogicManager setUrl(String url){
        this.url = url;
        return this;
    }

    public LogicManager setCacheKey(String cacheKey){
        this.cacheKey = cacheKey;
        return this;
    }

    public LogicManager setLimitedTime(int limitedTime){
        this.limitedTime = limitedTime;
        return this;
    }

    public LogicManager setParam(String key,Object object){
        requestParam.put(key,object);
        return this;
    }
    public LogicManager setFiles(Map<String,File> files){
        this.files = files;
        return this;
    }

    public LogicManager setParam(Map<String,Object> param){
        requestParam.putAll(param);
        return this;
    }

    @Override
    protected Object doInBackground(Object[] params) {

        Map<String,Object> param = getRequestParam();
        if (!requestParam.isEmpty()){
            param.putAll(requestParam);
        }

        Object object = null;
        innerDoInBackgroundPre();
        object = startEngine(param);
        innerDoInBackgroundEnd(object);
        return object;
    }

    private <T> T startEngine(Map<String,Object> param){

        if (networkRequestType == GET){
            if (returnDataType==LIST) {
                if (logicType == CACHE_ADVANCE_AND_NETWORK_RETURN) {
                    return (T) PlutoApiEngine.getListByCacheAdvance(url, param, cacheKey, handler, what, clazz);
                } else if (logicType == ONLY_NETWORK) {
                    return (T) PlutoApiEngine.getListByNoCache(url, param, clazz);
                } else if (logicType == CACHE_EXPIRED_AND_NETWORK_RETURN) {
                    return (T) PlutoApiEngine.getListByLimitTime(url, param, cacheKey, handler, what, limitedTime, clazz);
                }
            }else if (returnDataType == MODEL){

                if (clazz == Result.class){
                    Result result = PlutoApiEngine.getResultOnly(url, param);
                    //LogUtils.info(TAG,">>>>>>>>result"+result);
                    return (T) result;
                }else {

                    if (logicType == CACHE_ADVANCE_AND_NETWORK_RETURN) {
                        return (T) PlutoApiEngine.getModelByCacheAdvance(url, param, cacheKey, handler, what, clazz);
                    } else if (logicType == ONLY_NETWORK) {
                        return (T) PlutoApiEngine.getModelByNoCache(url, param, clazz);
                    } else if (logicType == CACHE_EXPIRED_AND_NETWORK_RETURN) {
                        return (T) PlutoApiEngine.getModelByGetLimitTime(url, param, cacheKey, handler, what, limitedTime, clazz);
                    }
                }
            }
        }else if (networkRequestType == POST){
            if (returnDataType==LIST) {
                //TODO:以后根据需求添加post
                LogUtils.error(TAG,"通过post获取List暂不支持，可以自行扩展");
            }else if (returnDataType == MODEL){
                if (clazz == Result.class){
                    if (logicType == UPLOALD_FILE){
                        return (T) PlutoApiEngine.postFilesResult(url,param,files);
                    }else{
                        return (T) PlutoApiEngine.postResult(url,param);
                    }
                }else {
                    if (logicType == CACHE_ADVANCE_AND_NETWORK_RETURN) {
                        //TODO:以后根据需求添加post
                        LogUtils.error(TAG,"通过post获取CACHE_ADVANCE_AND_NETWORK_RETURN暂不支持，可以自行扩展");
                    } else if (logicType == ONLY_NETWORK) {
                        return (T) PlutoApiEngine.postModelByNoCache(url, param, clazz);
                    } else if (logicType == CACHE_EXPIRED_AND_NETWORK_RETURN) {
                        //TODO:以后根据需求添加post
                        LogUtils.error(TAG,"通过post获取CACHE_EXPIRED_AND_NETWORK_RETURN暂不支持，可以自行扩展");
                    }else if (logicType == UPLOALD_FILE){
                        return (T) PlutoApiEngine.postModelUploadFiles(url, param,files, clazz);
                    }
                }
            }

        }
        return null;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        handler.obtainMessage(what,arg1,arg2,o).sendToTarget();
    }

    private Map<String,Object> getRequestParam(){
        Map<String,Object> param = null;
        switch (networkRequestType) {
            case GET:
                param = PlutoApiEngine.addRequiredParam();
                break;
            case POST:
                param = PlutoApiEngine.addRequiredParamByPost();
                break;
            default:
                break;
        }
        return param;
    }

    /**
     * 提供给子类做扩展
     */
    protected void innerDoInBackgroundPre(){

    }

    /**
     * 提供给子类做扩展
     */
    protected void innerDoInBackgroundEnd(Object object){

    }
}
