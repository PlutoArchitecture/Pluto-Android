package com.minggo.pluto.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.minggo.pluto.bitmap.BitmapDisplayConfig;
import com.minggo.pluto.bitmap.FinalBitmap;
import com.minggo.pluto.common.CommonAsyncTask;
import com.minggo.pluto.dialog.PlutoDialog;
import com.minggo.pluto.util.NetworkUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * fragment的基类
 * @author minggo
 * @time 2014-12-3下午3:59:14
 */
public abstract class PlutoFragment extends ProgressFragment implements IFragment,FragmentUserVisibleController.UserVisibleCallback{
	
	private boolean isInit; // 是否可以开始加载数据
	private boolean isCreated;
	private boolean isStatistics = true; // 是否统计
	protected String simpleName = getClass().getSimpleName();

	private FragmentUserVisibleController userVisibleController;

	private Toast toast = null;

	protected Handler mUiHandler = new UiHandler(this);
	protected FinalBitmap finalBitmap;
	protected BitmapDisplayConfig bitmapDisplayConfig;
	protected PlutoDialog loadingDialog;

	private static class UiHandler extends Handler {
		private final WeakReference<PlutoFragment> mFragmentReference;

		public UiHandler(PlutoFragment activity) {
			mFragmentReference = new WeakReference<PlutoFragment>(activity);
		}

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (mFragmentReference.get() != null) {
				mFragmentReference.get().handleUiMessage(msg);
			}
		}
	}

	public PlutoFragment() {
		userVisibleController = new FragmentUserVisibleController(this, this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isCreated = true;
		initLoadingDialog();
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		isInit = true;
		userVisibleController.activityCreated();
	}
	

	
	@Override
	public void onResume() {
		super.onResume();
		userVisibleController.resume();
		if (getUserVisibleHint()) {
			if (isInit && isCreated) {
				isInit = false;// 加载数据完成
				//System.out.println("应该去加载数据了");
				showData();
			}
		}

		if (getUserVisibleHint()) {
			statistics(true);
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		userVisibleController.setUserVisibleHint(isVisibleToUser);
		// 每次切换fragment时调用的方法
		if (isVisibleToUser) {
			if (isInit&&isCreated) {
				isInit = false;//加载数据完成
		//		System.out.println("应该去加载数据了");
				showData();
			}
		}
		if (isResumed()) {
			statistics(getUserVisibleHint());
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		userVisibleController.pause();
		if (getUserVisibleHint()) {
			statistics(false);
		}
	}

	/**
	 * 友盟统计
	 * @param isStart
     */
	public void statistics(boolean isStart){
		if (isStatistics()) {

		}
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

	protected abstract void showData();


	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void setupActions(ArrayList<String> actions) {
	}

	/**
	 * 处理更新UI任务
	 * 
	 * @param msg
	 */
	public void handleUiMessage(Message msg) {
	}

	@Override
	public void handleBroadcast(Context context, Intent intent) {
	}

	/**
	 * 发送UI更新操作
	 * 
	 * @param msg
	 */
	protected void sendUiMessage(Message msg) {
		mUiHandler.sendMessage(msg);
	}

	protected void sendUiMessageDelayed(Message msg, long delayMillis) {
		mUiHandler.sendMessageDelayed(msg, delayMillis);
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

	protected void removeUiMessage(int what) {
		mUiHandler.removeMessages(what);
	}

	protected Message obtainUiMessage() {
		return mUiHandler.obtainMessage();
	}

	/**
	 * 显示一个Toast类型的消息
	 *
	 * @param msg 显示的消息
	 */
	public void showToast(final String msg) {
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (toast == null) {
						toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
					} else {
						toast.setText(msg);
						toast.setDuration(Toast.LENGTH_SHORT);
					}
					toast.show();
				}
			});
		}
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
	 * 隐藏软键盘
	 */
	protected void hideSoftInput(Context context) {
		InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		// manager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
		if (getActivity().getCurrentFocus() != null) {
			manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}

	}

	public boolean isStatistics() {
		return isStatistics;
	}

	/**
	 * Fragment嵌套Fragment，父Fragment不进行统计<br><br>
	 * 要在onCreated方法之前设置才起作用，否则首次失效
	 * @param statistics  默认为true，为false不进行统计
     */
	public void setStatistics(boolean statistics) {
		isStatistics = statistics;
	}

	/**
	 * 显示软键盘
	 */
	protected void showSoftInput() {
		InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		manager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}

	@Override
	public void setWaitingShowToUser(boolean waitingShowToUser) {
		userVisibleController.setWaitingShowToUser(waitingShowToUser);
	}

	@Override
	public boolean isWaitingShowToUser() {
		return userVisibleController.isWaitingShowToUser();
	}

	@Override
	public boolean isVisibleToUser() {
		return userVisibleController.isVisibleToUser();
	}

	@Override
	public void callSuperSetUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public void onVisibleToUserChanged(boolean isVisibleToUser, boolean invokeInResumeOrPause) {

	}

	/**
	 * 检测网络是否可用
	 */
	public boolean isNetworkConnected() {
		return NetworkUtils.isNetworkConnected(this.getContext());
	}
	private void initLoadingDialog(){
		loadingDialog = new PlutoDialog(this.getContext(),PlutoDialog.LOADING);
	}
}
