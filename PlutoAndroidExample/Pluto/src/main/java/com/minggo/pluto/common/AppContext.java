package com.minggo.pluto.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.minggo.pluto.util.LogUtils;
import com.minggo.pluto.util.NetworkUtils;
import com.minggo.pluto.util.StringUtils;


/**
 * 应用上下文
 * 
 * @author minggo
 * @time 2014-12-2下午2:09:33
 */
public class AppContext {

	private static AppContext appContext;


	private AsyncTaskManager asyncTaskManager;
	private String mPlatformId;
	public Context context;

	public AppContext() {
	}
	public static void initAppContent(Context context){
		AppContext appContext = getInstance();
		appContext.context = context;
	}
	public static AppContext getInstance() {
		if (null == appContext) {
			appContext = new AppContext();


		}
		return appContext;
	}

	public AsyncTaskManager getAsyncTaskManager() {

		if (asyncTaskManager == null) {
			asyncTaskManager = new AsyncTaskManager();
		}

		return asyncTaskManager;
	}

	/**
	 * 检测网络是否可用
	 */
	public boolean isNetworkConnected() {
		return NetworkUtils.isNetworkConnected(context);
	}

	public static boolean isNetworkConnected(Context context) {
		return NetworkUtils.isNetworkConnected(context);
	}

	/**
	 * 获取当前网络类型
	 *
	 * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
	 */
	public int getNetworkType() {
		return NetworkUtils.getNetworkType(context);
	}

	/**
	 * 获取手机唯一识别码
	 * 
	 * @return
	 */
	public String getAndroidId(int bookid) {
//		String android_id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
//
//		if (android_id == null || android_id == "") {
//			android_id = android.os.Build.VERSION.RELEASE + android.os.Build.MODEL + bookid;
//		}
//		if (StringUtils.isEmpty(android_id)) {
//			android_id = "";
//		}
//		return android_id;
		return "idonotneeddeviceid";
	}

	/**
	 * 获取手机的IMEI
	 * 
	 * @return
	 */
	public String getAndroidIMEI() {
		//TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		//String imei = telephonyManager.getDeviceId();

		return "idonotneedimei";
	}

	/**
	 * 获取手机型号
	 * 
	 * @return
	 */
	public String GetMobileVersion() {
		return android.os.Build.MODEL;
	}

	/**
	 * 获取系统的版本号
	 * 
	 * @return
	 */
	public String GetAndroidVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 获取App安装包信息
	 * 
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			info = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null) {
			info = new PackageInfo();
		}
		return info;
	}

}
