
package com.minggo.pluto.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Message;

import java.util.ArrayList;

/**
 * 描述:Activity抽象接口
 * 
 * @author minggo
 * @since 2014-3-20 上午12:49:35
 */
public interface IActivity {

    /**
     * 设置广播action
     * 
     * @return
     */
    void setupActions(ArrayList<String> actions);

    /**
     * 刷新界面
     * 
     * @param msg
     */
    void handleUiMessage(Message msg);

    /**
     * 处理广播
     * 
     * @param context
     * @param intent
     */
    void handleBroadcast(Context context, Intent intent);

}
