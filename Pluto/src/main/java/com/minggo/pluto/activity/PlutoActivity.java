package com.minggo.pluto.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.minggo.pluto.common.AppManager;
import com.minggo.pluto.common.CommonAsyncTask;
import com.minggo.pluto.dialog.PlutoDialog;
import com.minggo.pluto.util.LogUtils;
import com.minggo.pluto.util.NetworkUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Timer;
import static com.minggo.pluto.logic.LogicManager.LogicManagerType.*;
/**
 * Activity的基类添加到应用管理堆栈和异步任务统一管理取消 添加handle处理统一使用[2015-1-27]
 * 
 * @author minggo
 * @time 2014-12-2下午3:15:02
 */
public abstract class PlutoActivity extends AppCompatActivity implements IActivity {

	protected Handler mUiHandler = new UiHandler(this);
	private final String pageName = getClass().getSimpleName();

	private Toast toast = null;
	/** 定时器 */
	protected Timer timer;
	/** 定时器周期，一般为1秒一次，单位毫秒 */
	protected int timerPeriod = 1000;

	protected PlutoDialog loadingDialog;

	private static class UiHandler extends Handler {
		private final WeakReference<PlutoActivity> mActivityReference;

		public UiHandler(PlutoActivity activity) {
			mActivityReference = new WeakReference<PlutoActivity>(activity);
		}

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (mActivityReference.get() != null) {
				mActivityReference.get().handleUiMessage(msg);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 添加Activity到堆栈
		AppManager.getAppManager().addActivity(this);

		showHomeAsUp();
		initLoadingDialog();
	}

	private void initLoadingDialog(){
		loadingDialog = new PlutoDialog(this,PlutoDialog.LOADING);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		stopTimer();
		// 结束Activity&从堆栈中移除
		AppManager.getAppManager().finishActivity(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		LogUtils.umengOnResume(pageName);

	}

	@Override
	protected void onPause() {
		super.onPause();

		LogUtils.umengOnPause(pageName);

	}

	/**
	 * 取消指定异步任务
	 * 
	 * @param asyncTasks
	 */
	protected void cancelAsyncTask(CommonAsyncTask<?, ?, ?>... asyncTasks) {
		for (CommonAsyncTask<?, ?, ?> asyncTask : asyncTasks) {
			if (asyncTask != null && !asyncTask.isCancelled()) {
				asyncTask.cancel(true);
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void setupActions(ArrayList<String> actions) {

	}

	@Override
	public void handleUiMessage(Message msg) {
	}

	protected void sendUiMessageDelayed(Message msg, long delayMillis) {
	}

	/**
	 * 发送UI更新操作
	 * 
	 * @param what
	 */
	protected void sendEmptyUiMessage(int what) {
		mUiHandler.sendEmptyMessage(what);
	}

	protected void sendEmptyUiMessageDelayed(int what, long delayMillis) {
		mUiHandler.sendEmptyMessageDelayed(what, delayMillis);
	}

	protected void removeUiMessages(int what) {
		mUiHandler.removeMessages(what);
	}

	protected Message obtainUiMessage() {
		return mUiHandler.obtainMessage();
	}

	/**
	 * 显示{@link Toast}通知
	 *
	 * @param strResId 字符串资源id
	 */
	public void showToast(final int strResId) {
		String text = getString(strResId);
		showToast(text);
	}

	/**
	 * 显示{@link Toast}通知
	 *
	 * @param showText 字符串资源id
	 */
	public void showToast(final String showText) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (toast == null) {
					toast = Toast.makeText(PlutoActivity.this, showText, Toast.LENGTH_SHORT);
				} else {
					toast.setText(showText);
					toast.setDuration(Toast.LENGTH_SHORT);
				}
				toast.show();
			}
		});
	}




	/**
	 * 隐藏软键盘
	 */
	protected void hideSoftInput(Context context) {
		InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		// manager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
		if (getCurrentFocus() != null) {
			manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}

	}

	/**
	 * 显示软键盘
	 */
	protected void showSoftInput() {
		InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		manager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}

	/**
	 * 检测网络是否可用
	 */
	public boolean isNetworkConnected() {
		return NetworkUtils.isNetworkConnected(this);
	}

	public Handler getmUiHandler() {
		return mUiHandler;
	}

	@Override
	public void handleBroadcast(Context context, Intent intent) {
		
	}

	/** ActionBar显示返回图标 */
	protected void showHomeAsUp() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	/** ActionBar隐藏返回图标 */
	protected void hideHomeAsUp() {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(false);
		}
	}

	/** 设置ActionBar左上角图标 */
	protected void setHomeAsUpIndicator(@DrawableRes int resId) {
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setHomeAsUpIndicator(resId);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				homeMenuOnClick();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * ActionBar返回图标点击事件，默认实现为onBackPressed();
	 */
	protected void homeMenuOnClick() {
		onBackPressed();
	}

	/**
	 * 停止定时器
	 */
	public void stopTimer(){
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

}
