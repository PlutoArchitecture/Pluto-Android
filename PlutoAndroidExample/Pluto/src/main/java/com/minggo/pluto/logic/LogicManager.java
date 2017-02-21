package com.minggo.pluto.logic;


import android.os.Handler;

import com.minggo.pluto.api.PlutoApiEngine;
import com.minggo.pluto.common.CommonAsyncTask;
import com.minggo.pluto.model.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by minggo on 2017/2/21.
 */

public class LogicManager extends CommonAsyncTask {

    private int what;
    private int arg1;
    private int arg2;
    private Handler handler;
    private Object instance;
    private LogicType logicType;
    private NetworkRequestType networkRequestType;

    //根据实际情况后续添加
    public enum LogicType{
        CACHE_ADVANCE_AND_NETWORK_RETURN,CACHE_ADVANCE_AND_NETWORK_SAVED,ONLY_NETWORK,ONLY_RESULT
    }
    public enum NetworkRequestType{
        POST,GET
    }

    public <T> LogicManager(Handler handler, Class<T> t){
        this.handler = handler;
        try {
            instance = t.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public <T> LogicManager(Handler handler, List<Class<T>> t){
        this.handler = handler;
        instance = new ArrayList<Class<T>>();
    }


    @Override
    protected Object doInBackground(Object[] params) {

        Map<String,Object> param = getRequestParam();

        innerDoInBackgroundPre();

        if (instance instanceof List){

        } else if(instance instanceof Result){

        } else {

        }

        innerDoInBackgroundEnd();
        return null;
    }

    private <T> List<T> startEngine(Map<String,Object> param){

        

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

    protected void innerDoInBackgroundPre(){

    }

    protected void innerDoInBackgroundEnd(){

    }
}
