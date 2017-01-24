package com.minggo.pluto.common;


import com.minggo.pluto.util.LogUtils;

import java.util.Observable;
import java.util.Observer;

/**
 * 异步任务管理器
 * @author minggo
 * @time 2014-12-2下午2:12:23
 */
public class AsyncTaskManager extends Observable {
	
	private static final String TAG = "AsyncTaskManager";

	public static final Integer CANCEL_ALL = 1;

	/**
	 * 该方法不要写在onDestroy()方法中，以免引起下个Activity的异步任务中断
	 */
	public void cancelAll() {
		LogUtils.debug(TAG, "All asynctask will Cancell.");
		setChanged();
		notifyObservers(CANCEL_ALL);
	}

	public void addTask(Observer task) {
		super.addObserver(task);
	}
}