
package com.minggo.pluto.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Message;

import java.util.ArrayList;

/**
 * 描述:Fragment抽象接口
 */
public interface IFragment {

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
