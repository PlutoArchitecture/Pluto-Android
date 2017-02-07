/**
 * Copyright (c) 2012-2013, Michael Yang 杨福海 (www.yangfuhai.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.minggo.pluto.bitmap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.ImageView;


import com.minggo.pluto.Pluto;
import com.minggo.pluto.util.LogUtils;
import com.minggo.pluto.util.PhotoUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class FinalBitmap {

	private FinalBitmapConfig mConfig;
	private static BitmapCache mImageCache;

	private boolean mExitTasksEarly = false;
	private boolean mPauseWork = false;
	private final Object mPauseWorkLock = new Object();
	private Context mContext;
	private float memerySize;
	private static ExecutorService bitmapLoadAndDisplayExecutor;

	private static FinalBitmap mFinalBitmap;

	// //////////////////////// config method
	// start////////////////////////////////////
	private FinalBitmap(Context context) {
		mContext = context;
		mConfig = new FinalBitmapConfig(context);

		configDiskCachePath(BitmapCommonUtils.getDiskCacheDir(context, Pluto.FINAL_BIMAP_SAVE_PATH).getAbsolutePath());// 配置缓存路径
		configDisplayer(new SimpleDisplayer());// 配置显示器
		configDownlader(new SimpleHttpDownloader());// 配置下载器
	}

	/**
	 * 创建finalbitmap
	 * 
	 * @param ctx
	 * @return
	 */
	public static FinalBitmap create(Context ctx) {
		if (mFinalBitmap == null) {
			mFinalBitmap = new FinalBitmap(ctx.getApplicationContext());
			mFinalBitmap.init();
		}
		return mFinalBitmap;
	}

	/**
	 * 创建finalBitmap
	 * 
	 * @param ctx
	 * @param diskCachePath
	 *            磁盘缓存路径
	 * @return
	 */
	public static FinalBitmap create(Context ctx, String diskCachePath) {
		if (mFinalBitmap == null) {
			mFinalBitmap = new FinalBitmap(ctx.getApplicationContext());
			mFinalBitmap.configDiskCachePath(diskCachePath);
			mFinalBitmap.init();
		}
		return mFinalBitmap;

	}

	/**
	 * 创建finalBitmap
	 * 
	 * @param ctx
	 * @param diskCachePath
	 *            磁盘缓存路径
	 * @param memoryCacheSizePercent
	 *            缓存大小在当前进程的百分比（0.05-0.8之间）
	 * @return
	 */
	public static FinalBitmap create(Context ctx, String diskCachePath, float memoryCacheSizePercent) {
		if (mFinalBitmap == null) {
			mFinalBitmap = new FinalBitmap(ctx.getApplicationContext());
			mFinalBitmap.configDiskCachePath(diskCachePath);
			mFinalBitmap.configMemoryCachePercent(memoryCacheSizePercent);
			mFinalBitmap.init();
		}

		return mFinalBitmap;
	}

	/**
	 * 创建finalBitmap
	 * 
	 * @param ctx
	 * @param diskCachePath
	 *            磁盘缓存路径
	 * @param memoryCacheSize
	 *            内存缓存大小
	 * @return
	 */
	public static FinalBitmap create(Context ctx, String diskCachePath, int memoryCacheSize) {
		if (mFinalBitmap == null) {
			mFinalBitmap = new FinalBitmap(ctx.getApplicationContext());
			mFinalBitmap.configDiskCachePath(diskCachePath);
			mFinalBitmap.configMemoryCacheSize(memoryCacheSize);
			mFinalBitmap.init();
		}

		return mFinalBitmap;
	}

	/**
	 * 创建finalBitmap
	 * 
	 * @param ctx
	 * @param diskCachePath
	 *            磁盘缓存路径
	 * @param memoryCacheSizePercent
	 *            缓存大小在当前进程的百分比（0.05-0.8之间）
	 * @param threadSize
	 *            线程并发数量
	 * @return
	 */
	public static FinalBitmap create(Context ctx, String diskCachePath, float memoryCacheSizePercent, int threadSize) {
		if (mFinalBitmap == null) {
			mFinalBitmap = new FinalBitmap(ctx.getApplicationContext());
			mFinalBitmap.configDiskCachePath(diskCachePath);
			mFinalBitmap.configBitmapLoadThreadSize(threadSize);
			mFinalBitmap.configMemoryCachePercent(memoryCacheSizePercent);
			mFinalBitmap.init();
		}

		return mFinalBitmap;
	}

	/**
	 * 创建finalBitmap
	 * 
	 * @param ctx
	 * @param diskCachePath
	 *            磁盘缓存路径
	 * @param memoryCacheSize
	 *            内存缓存大小
	 * @param threadSize
	 *            线程并发数量
	 * @return
	 */
	public static FinalBitmap create(Context ctx, String diskCachePath, int memoryCacheSize, int threadSize) {
		if (mFinalBitmap == null) {
			mFinalBitmap = new FinalBitmap(ctx.getApplicationContext());
			mFinalBitmap.configDiskCachePath(diskCachePath);
			mFinalBitmap.configBitmapLoadThreadSize(threadSize);
			mFinalBitmap.configMemoryCacheSize(memoryCacheSize);
			mFinalBitmap.init();
		}

		return mFinalBitmap;
	}

	/**
	 * 创建finalBitmap
	 * 
	 * @param ctx
	 * @param diskCachePath
	 *            磁盘缓存路径
	 * @param memoryCacheSizePercent
	 *            缓存大小在当前进程的百分比（0.05-0.8之间）
	 * @param diskCacheSize
	 *            磁盘缓存大小
	 * @param threadSize
	 *            线程并发数量
	 * @return
	 */
	public static FinalBitmap create(Context ctx, String diskCachePath, float memoryCacheSizePercent, int diskCacheSize, int threadSize) {
		if (mFinalBitmap == null) {
			mFinalBitmap = new FinalBitmap(ctx.getApplicationContext());
			mFinalBitmap.configDiskCachePath(diskCachePath);
			mFinalBitmap.configBitmapLoadThreadSize(threadSize);
			mFinalBitmap.configMemoryCachePercent(memoryCacheSizePercent);
			mFinalBitmap.configDiskCacheSize(diskCacheSize);
			mFinalBitmap.init();
		}

		return mFinalBitmap;
	}

	/**
	 * 创建finalBitmap
	 * 
	 * @param ctx
	 * @param diskCachePath
	 *            磁盘缓存路径
	 * @param memoryCacheSize
	 *            内存缓存大小
	 * @param diskCacheSize
	 *            磁盘缓存大小
	 * @param threadSize
	 *            线程并发数量
	 * @return
	 */
	public static FinalBitmap create(Context ctx, String diskCachePath, int memoryCacheSize, int diskCacheSize, int threadSize) {
		if (mFinalBitmap == null) {
			mFinalBitmap = new FinalBitmap(ctx.getApplicationContext());
			mFinalBitmap.configDiskCachePath(diskCachePath);
			mFinalBitmap.configBitmapLoadThreadSize(threadSize);
			mFinalBitmap.configMemoryCacheSize(memoryCacheSize);
			mFinalBitmap.configDiskCacheSize(diskCacheSize);
			mFinalBitmap.init();
		}

		return mFinalBitmap;
	}

	/**
	 * 设置图片正在加载的时候显示的图片
	 * 
	 * @param bitmap
	 */
	public FinalBitmap configLoadingImage(Bitmap bitmap) {
		mConfig.defaultDisplayConfig.setLoadingBitmap(bitmap);
		return this;
	}

	/**
	 * 设置图片正在加载的时候显示的图片
	 */
	public FinalBitmap configLoadingImage(int resId) {
		mConfig.defaultDisplayConfig.setLoadingBitmap(BitmapFactory.decodeResource(mContext.getResources(), resId));
		return this;
	}

	/**
	 * 设置图片加载失败时候显示的图片
	 * 
	 * @param bitmap
	 */
	public FinalBitmap configLoadfailImage(Bitmap bitmap) {
		mConfig.defaultDisplayConfig.setLoadfailBitmap(bitmap);
		return this;
	}

	/**
	 * 设置图片加载失败时候显示的图片
	 * 
	 * @param resId
	 */
	public FinalBitmap configLoadfailImage(int resId) {
		mConfig.defaultDisplayConfig.setLoadfailBitmap(BitmapFactory.decodeResource(mContext.getResources(), resId));
		return this;
	}

	/**
	 * 配置默认图片的小的高度
	 * 
	 * @param bitmapHeight
	 */
	public FinalBitmap configBitmapMaxHeight(int bitmapHeight) {
		mConfig.defaultDisplayConfig.setBitmapHeight(bitmapHeight);
		return this;
	}

	/**
	 * 配置默认图片的小的宽度
	 */
	public FinalBitmap configBitmapMaxWidth(int bitmapWidth) {
		mConfig.defaultDisplayConfig.setBitmapWidth(bitmapWidth);
		return this;
	}

	/**
	 * 设置下载器，比如通过ftp或者其他协议去网络读取图片的时候可以设置这项
	 * 
	 * @param downlader
	 * @return
	 */
	public FinalBitmap configDownlader(Downloader downlader) {
		mConfig.downloader = downlader;
		return this;
	}

	/**
	 * 设置显示器，比如在显示的过程中显示动画等
	 * 
	 * @param displayer
	 * @return
	 */
	public FinalBitmap configDisplayer(Displayer displayer) {
		mConfig.displayer = displayer;
		return this;
	}

	/**
	 * 配置磁盘缓存路径
	 * 
	 * @param strPath
	 * @return
	 */
	private FinalBitmap configDiskCachePath(String strPath) {
		if (!TextUtils.isEmpty(strPath)) {
			mConfig.cachePath = strPath;
		}
		return this;
	}

	/**
	 * 配置内存缓存大小 大于2MB以上有效
	 * 
	 * @param size
	 *            缓存大小
	 */
	private FinalBitmap configMemoryCacheSize(int size) {
		mConfig.memCacheSize = size;
		return this;
	}

	/**
	 * 设置应缓存的在APK总内存的百分比，优先级大于configMemoryCacheSize
	 * 
	 * @param percent
	 *            百分比，值的范围是在 0.05 到 0.8之间
	 */
	private FinalBitmap configMemoryCachePercent(float percent) {
		mConfig.memCacheSizePercent = percent;
		return this;
	}

	/**
	 * 设置磁盘缓存大小 5MB 以上有效
	 * 
	 * @param size
	 */
	private FinalBitmap configDiskCacheSize(int size) {
		mConfig.diskCacheSize = size;
		return this;
	}

	/**
	 * 设置加载图片的线程并发数量
	 * 
	 * @param size
	 */
	private FinalBitmap configBitmapLoadThreadSize(int size) {
		if (size >= 1)
			mConfig.poolSize = size;
		return this;
	}

	/**
	 * 这个方法必须被调用后 FinalBitmap 配置才能有效
	 * 
	 * @return
	 */
	private FinalBitmap init() {

		mConfig.init();

		BitmapCache.ImageCacheParams imageCacheParams = new BitmapCache.ImageCacheParams(mConfig.cachePath);
		if (mConfig.memCacheSizePercent > 0.05 && mConfig.memCacheSizePercent < 0.8) {
			imageCacheParams.setMemCacheSizePercent(mContext, mConfig.memCacheSizePercent);
		} else {
			if (mConfig.memCacheSize > 1024 * 1024 * 2) {
				imageCacheParams.setMemCacheSize(mConfig.memCacheSize);
			} else {
				// 设置默认的内存缓存大小
				imageCacheParams.setMemCacheSizePercent(mContext, 0.3f);
			}
		}
		if (mConfig.diskCacheSize > 1024 * 1024 * 5)
			imageCacheParams.setDiskCacheSize(mConfig.diskCacheSize);
		mImageCache = new BitmapCache(imageCacheParams);

		bitmapLoadAndDisplayExecutor = Executors.newFixedThreadPool(mConfig.poolSize, new ThreadFactory() {
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				// 设置线程的优先级别，让线程先后顺序执行（级别越高，抢到cpu执行的时间越多）
				t.setPriority(Thread.NORM_PRIORITY - 1);
				return t;
			}
		});

		new CacheExecutecTask().execute(CacheExecutecTask.MESSAGE_INIT_DISK_CACHE);

		return this;
	}

	// //////////////////////// config method
	// end////////////////////////////////////

	public void display(ImageView imageView, String uri) {
		// Log.i("fb", "原来图片地址-->" + uri);

		if (uri != null) {

			doDisplay(imageView, uri, null, false, false);

		}

	}

	public void displayRound(ImageView imageView, String uri, BitmapDisplayConfig displayConfig, boolean round) {
		doDisplay(imageView, uri, displayConfig, false, round);
	}

	/**
	 * WIFI状态下加载大图片 uri小图片 bigUri大图片
	 * 
	 * @param imageView
	 * @param uri
	 * @param bigUrl
	 */
	public void display(ImageView imageView, String uri, String bigUrl) {

		String path = BitmapCommonUtils.getDiskCacheDir(mContext, Pluto.FINAL_BIMAP_SAVE_PATH).getAbsolutePath() + "/" + FileNameGenerator.generator(bigUrl) + ".0";

		doDisplay(imageView, uri, null, false, false);

	}
	/**
	 * 判断内容是否少于1G
	 * @return
	 */
	private boolean isLessMemory() {
		
		if (memerySize==0) {
			String str1 = "/proc/meminfo";// 系统内存信息文件
			String str2;
			String[] arrayOfString;
			long initial_memory = 0;
			try {
				FileReader localFileReader = new FileReader(str1);
				BufferedReader localBufferedReader = new BufferedReader(
						localFileReader, 8192);
				str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

				arrayOfString = str2.split("\\s+");

				initial_memory = Math.abs(Integer.valueOf(arrayOfString[1]).intValue() * 1024);// 获得系统总内存，单位是KB，乘以1024转换为Byte
				localBufferedReader.close();
				//Log.i("memery", "menery--->"+(float)initial_memory/1024/1024/1024);
				memerySize = initial_memory;
				if ((float)memerySize/1024/1024/1024>1) {
					return false;
				}else{
					return true;
				}
				
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}else{
			if ((float)memerySize/1024/1024/1024>1) {
				return false;
			}else{
				return true;
			}
		}
		
		
	}
	
	/**
	 * 等比例缩放
	 * 
	 * @param imageView
	 * @param uri
	 * @param scale
	 */
	public void displayScale(ImageView imageView, String uri, boolean scale) {
		doDisplay(imageView, uri, null, scale, false);
	}

	/**
	 * 等比例缩放
	 * 
	 * @param imageView
	 * @param uri
	 * @param scale
	 */
	public void displayScale(ImageView imageView, String uri, BitmapDisplayConfig displayConfig, boolean scale) {
		doDisplay(imageView, uri, displayConfig, scale, false);
	}

	public void display(ImageView imageView, String uri, int imageWidth, int imageHeight) {
		BitmapDisplayConfig displayConfig = configMap.get(imageWidth + "_" + imageHeight);
		if (displayConfig == null) {
			displayConfig = getDisplayConfig();
			displayConfig.setBitmapHeight(imageHeight);
			displayConfig.setBitmapWidth(imageWidth);
			configMap.put(imageWidth + "_" + imageHeight, displayConfig);
		}

		doDisplay(imageView, uri, displayConfig, false, false);
	}

	public void display(ImageView imageView, String uri, Bitmap loadingBitmap) {
		BitmapDisplayConfig displayConfig = configMap.get(String.valueOf(loadingBitmap));
		if (displayConfig == null) {
			displayConfig = getDisplayConfig();
			displayConfig.setLoadingBitmap(loadingBitmap);
			configMap.put(String.valueOf(loadingBitmap), displayConfig);
		}

		doDisplay(imageView, uri, displayConfig, false, false);
	}

	public void display(ImageView imageView, String uri, Bitmap loadingBitmap, Bitmap laodfailBitmap) {
		BitmapDisplayConfig displayConfig = configMap.get(String.valueOf(loadingBitmap) + "_" + String.valueOf(laodfailBitmap));
		if (displayConfig == null) {
			displayConfig = getDisplayConfig();
			displayConfig.setLoadingBitmap(loadingBitmap);
			displayConfig.setLoadfailBitmap(laodfailBitmap);
			configMap.put(String.valueOf(loadingBitmap) + "_" + String.valueOf(laodfailBitmap), displayConfig);
		}

		doDisplay(imageView, uri, displayConfig, false, false);
	}

	public void display(ImageView imageView, String uri, int imageWidth, int imageHeight, Bitmap loadingBitmap, Bitmap laodfailBitmap) {
		BitmapDisplayConfig displayConfig = configMap.get(imageWidth + "_" + imageHeight + "_" + String.valueOf(loadingBitmap) + "_" + String.valueOf(laodfailBitmap));
		if (displayConfig == null) {
			displayConfig = getDisplayConfig();
			displayConfig.setBitmapHeight(imageHeight);
			displayConfig.setBitmapWidth(imageWidth);
			displayConfig.setLoadingBitmap(loadingBitmap);
			displayConfig.setLoadfailBitmap(laodfailBitmap);
			configMap.put(imageWidth + "_" + imageHeight + "_" + String.valueOf(loadingBitmap) + "_" + String.valueOf(laodfailBitmap), displayConfig);
		}

		doDisplay(imageView, uri, displayConfig, true, false);
	}

	public void display(ImageView imageView, String uri, BitmapDisplayConfig config) {
		doDisplay(imageView, uri, config, false, false);
	}

	private void doDisplay(ImageView imageView, String uri, BitmapDisplayConfig displayConfig, boolean scale, boolean round) {
		if (TextUtils.isEmpty(uri) || imageView == null) {
			return;
		}

		if (displayConfig == null)
			displayConfig = mConfig.defaultDisplayConfig;

		Bitmap bitmap = null;

		if (mImageCache != null) {
			bitmap = mImageCache.getBitmapFromMemCache(uri);
		}

		if (bitmap != null) {
			if (round) {
				bitmap = PhotoUtil.toRoundBitmap(bitmap);
			}
			imageView.setImageBitmap(bitmap);
		} else if (checkImageTask(uri, imageView)) {

			final BitmapLoadAndDisplayTask task = new BitmapLoadAndDisplayTask(imageView, displayConfig);
			// 设置默认图片
			final AsyncDrawable asyncDrawable = new AsyncDrawable(mContext.getResources(), displayConfig.getLoadingBitmap(), task);
			imageView.setImageDrawable(asyncDrawable);
			task.executeOnExecutor(bitmapLoadAndDisplayExecutor, uri, scale, round);
		}
	}

	private HashMap<String, BitmapDisplayConfig> configMap = new HashMap<String, BitmapDisplayConfig>();

	private BitmapDisplayConfig getDisplayConfig() {
		BitmapDisplayConfig config = new BitmapDisplayConfig();
		config.setAnimation(mConfig.defaultDisplayConfig.getAnimation());
		config.setAnimationType(mConfig.defaultDisplayConfig.getAnimationType());
		config.setBitmapHeight(mConfig.defaultDisplayConfig.getBitmapHeight());
		config.setBitmapWidth(mConfig.defaultDisplayConfig.getBitmapWidth());
		config.setLoadfailBitmap(mConfig.defaultDisplayConfig.getLoadfailBitmap());
		config.setLoadingBitmap(mConfig.defaultDisplayConfig.getLoadingBitmap());
		return config;
	}

	private void initDiskCacheInternal() {
		if (mImageCache != null) {
			mImageCache.initDiskCache();
		}
		if (mConfig != null && mConfig.bitmapProcess != null) {
			mConfig.bitmapProcess.initHttpDiskCache();
		}
	}

	private void clearCacheInternal() {
		if (mImageCache != null) {
			mImageCache.clearCache();
		}
		if (mConfig != null && mConfig.bitmapProcess != null) {
			mConfig.bitmapProcess.clearCacheInternal();
		}
	}

	private void clearMemoryCache() {
		if (mImageCache != null) {
			mImageCache.clearMemoryCache();
		}
	}

	private void flushCacheInternal() {
		if (mImageCache != null) {
			mImageCache.flush();
		}
		if (mConfig != null && mConfig.bitmapProcess != null) {
			mConfig.bitmapProcess.flushCacheInternal();
		}
	}

	private void closeCacheInternal() {
		if (mImageCache != null) {
			mImageCache.close();
			mImageCache = null;
		}
		if (mConfig != null && mConfig.bitmapProcess != null) {
			mConfig.bitmapProcess.clearCacheInternal();
		}
	}

	/**
	 * 网络加载bitmap
	 * 
	 * @return
	 */
	private Bitmap processBitmap(String uri, BitmapDisplayConfig config) {
		if (mConfig != null && mConfig.bitmapProcess != null) {
			return mConfig.bitmapProcess.processBitmap(uri, config);
		}
		return null;
	}

	public void setExitTasksEarly(boolean exitTasksEarly) {
		mExitTasksEarly = exitTasksEarly;
	}

	/**
	 * activity onResume的时候调用这个方法，让加载图片线程继续
	 */
	public void onResume() {
		setExitTasksEarly(false);
	}

	/**
	 * activity onPause的时候调用这个方法，让线程暂停
	 */
	public void onPause() {
		setExitTasksEarly(true);
		flushCache();
	}

	/**
	 * activity onDestroy的时候调用这个方法，释放缓存
	 */
	public void onDestroy() {
		closeCache();
	}

	/**
	 * 清除缓存
	 */
	public void clearAllCache() {
		new CacheExecutecTask().execute(CacheExecutecTask.MESSAGE_CLEAR);
	}

	/**
	 * 清除缓存
	 */
	public void clearMemeoryCache() {
		new CacheExecutecTask().execute(CacheExecutecTask.MESSAGE_CLEAR_MEMORY);
	}

	/**
	 * 刷新缓存
	 */
	public void flushCache() {
		new CacheExecutecTask().execute(CacheExecutecTask.MESSAGE_FLUSH);
	}

	/**
	 * 关闭缓存
	 */
	public void closeCache() {
		new CacheExecutecTask().execute(CacheExecutecTask.MESSAGE_CLOSE);
	}

	/**
	 * 退出正在加载的线程，程序退出的时候调用词方法
	 * 
	 * @param exitTasksEarly
	 */
	public void exitTasksEarly(boolean exitTasksEarly) {
		mExitTasksEarly = exitTasksEarly;
		if (exitTasksEarly)
			pauseWork(false);// 让暂停的线程结束
	}

	/**
	 * 暂停正在加载的线程，监听listview或者gridview正在滑动的时候条用词方法
	 * 
	 * @param pauseWork
	 *            true停止暂停线程，false继续线程
	 */
	public void pauseWork(boolean pauseWork) {
		synchronized (mPauseWorkLock) {
			mPauseWork = pauseWork;
			if (!mPauseWork) {
				mPauseWorkLock.notifyAll();
			}
		}
	}

	private static BitmapLoadAndDisplayTask getBitmapTaskFromImageView(ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

	/**
	 * 检测 imageView中是否已经有线程在运行
	 * 
	 * @param data
	 * @param imageView
	 * @return true 没有 false 有线程在运行了
	 */
	public static boolean checkImageTask(Object data, ImageView imageView) {
		final BitmapLoadAndDisplayTask bitmapWorkerTask = getBitmapTaskFromImageView(imageView);

		if (bitmapWorkerTask != null) {
			final Object bitmapData = bitmapWorkerTask.data;
			if (bitmapData == null || !bitmapData.equals(data)) {
				bitmapWorkerTask.cancel(true);
			} else {
				// 同一个线程已经在执行
				return false;
			}
		}
		return true;
	}
	
	public static BitmapCache getBitmapCache(){
		return mImageCache;
	}

	private static class AsyncDrawable extends BitmapDrawable {
		private final WeakReference<BitmapLoadAndDisplayTask> bitmapWorkerTaskReference;

		public AsyncDrawable(Resources res, Bitmap bitmap, BitmapLoadAndDisplayTask bitmapWorkerTask) {
			super(res, bitmap);
			bitmapWorkerTaskReference = new WeakReference<BitmapLoadAndDisplayTask>(bitmapWorkerTask);
		}

		public BitmapLoadAndDisplayTask getBitmapWorkerTask() {
			return bitmapWorkerTaskReference.get();
		}
	}

	/**
	 * @author michael Young (www.YangFuhai.com)
	 * @version 1.0
	 * @title 缓存操作的异步任务
	 * @description 操作缓存
	 * @company 探索者网络工作室(www.tsz.net)
	 * @created 2012-10-28
	 */
	private class CacheExecutecTask extends AsyncTask<Object, Void, Void> {
		public static final int MESSAGE_CLEAR = 0;
		public static final int MESSAGE_INIT_DISK_CACHE = 1;
		public static final int MESSAGE_FLUSH = 2;
		public static final int MESSAGE_CLOSE = 3;
		public static final int MESSAGE_CLEAR_MEMORY = 4;

		@Override
		protected Void doInBackground(Object... params) {
			switch ((Integer) params[0]) {
			case MESSAGE_CLEAR:
				clearCacheInternal();
				break;
			case MESSAGE_INIT_DISK_CACHE:
				initDiskCacheInternal();
				break;
			case MESSAGE_FLUSH:
				clearMemoryCache();
				flushCacheInternal();
				break;
			case MESSAGE_CLOSE:
				clearMemoryCache();
				closeCacheInternal();
				break;
			case MESSAGE_CLEAR_MEMORY:
				clearMemoryCache();
				break;
			}
			return null;
		}
	}

	/**
	 * bitmap下载显示的线程
	 * 
	 * @author michael yang
	 */
	private class BitmapLoadAndDisplayTask extends AsyncTask<Object, Void, Bitmap> {
		private Object data;
		private final WeakReference<ImageView> imageViewReference;
		private final BitmapDisplayConfig displayConfig;
		boolean scale = false;
		boolean round = false;

		public BitmapLoadAndDisplayTask(ImageView imageView, BitmapDisplayConfig config) {
			imageViewReference = new WeakReference<ImageView>(imageView);
			displayConfig = config;
		}

		@Override
		protected Bitmap doInBackground(Object... params) {
			data = params[0];
			final String dataString = String.valueOf(data);
			Bitmap bitmap = null;

			if (params.length > 1) {
				scale = (Boolean) params[1];
			}
			if (params.length > 2) {
				round = (Boolean) params[2];
			}
			synchronized (mPauseWorkLock) {
				while (mPauseWork && !isCancelled()) {
					try {
						mPauseWorkLock.wait();
					} catch (InterruptedException e) {
                        e.printStackTrace();
					}
				}
			}

			if (mImageCache != null && !isCancelled() && getAttachedImageView() != null && !mExitTasksEarly) {
				bitmap = mImageCache.getBitmapFromDiskCache(dataString);
                LogUtils.info("finalbitmap", "mImageCache != null && !isCancelled()");
			}

			if (bitmap == null && !isCancelled() && getAttachedImageView() != null && !mExitTasksEarly) {
				bitmap = processBitmap(dataString, displayConfig);
				if (bitmap != null) {
					if (scale) {
						int widthScrean = mContext.getApplicationContext().getResources().getDisplayMetrics().widthPixels;

						int imageOrigWidth = bitmap.getWidth();
						int imageOrigHeigh = bitmap.getHeight();

                        LogUtils.info("finalbitmap", "宽度-->" + imageOrigWidth + ",--->" + imageOrigHeigh);
						int reSizeHeigh = (int) (widthScrean * ((double) imageOrigHeigh / (double) imageOrigWidth));
                        LogUtils.info("finalbitmap", "1宽度-->" + widthScrean + ",---1>" + reSizeHeigh);
						bitmap = Bitmap.createScaledBitmap(bitmap, widthScrean, reSizeHeigh, true);
					}

				}
			}

			if (bitmap != null && mImageCache != null) {
				mImageCache.addBitmapToCache(dataString, bitmap);
			}

			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (isCancelled() || mExitTasksEarly) {
				bitmap = null;
			}

			// 判断线程和当前的imageview是否是匹配
			final ImageView imageView = getAttachedImageView();
			if (bitmap != null && imageView != null) {
				if (round) {
					bitmap = PhotoUtil.toRoundBitmap(bitmap);
				}
				mConfig.displayer.loadCompletedisplay(imageView, bitmap, displayConfig);
			} else if (bitmap == null && imageView != null) {
				mConfig.displayer.loadFailDisplay(imageView, displayConfig.getLoadfailBitmap());
			}
		}

		@Override
		protected void onCancelled(Bitmap bitmap) {
			super.onCancelled(bitmap);
			synchronized (mPauseWorkLock) {
				mPauseWorkLock.notifyAll();
			}
		}

		/**
		 * 获取线程匹配的imageView,防止出现闪动的现象
		 * 
		 * @return
		 */
		private ImageView getAttachedImageView() {
			final ImageView imageView = imageViewReference.get();
			final BitmapLoadAndDisplayTask bitmapWorkerTask = getBitmapTaskFromImageView(imageView);

			if (this == bitmapWorkerTask) {
				return imageView;
			}

			return null;
		}
	}

	private LoadFinishListener loadFinishListener;

	public interface LoadFinishListener {
		public void loadFinish(ImageView imageView);
	}

	public void display(ImageView imageView, String uri, LoadFinishListener listener) {
		if (listener != null) {
			loadFinishListener = listener;
		}

		if (uri != null) {
			doDisplay(imageView, uri, null, false, false);
		}

	}

	/**
	 * @author michael Young (www.YangFuhai.com)
	 * @version 1.0
	 * @title 配置信息
	 * @description FinalBitmap的配置信息
	 * @company 探索者网络工作室(www.tsz.net)
	 * @created 2012-10-28
	 */
	private class FinalBitmapConfig {

		public String cachePath;

		public Displayer displayer;
		public Downloader downloader;
		public BitmapProcess bitmapProcess;
		public BitmapDisplayConfig defaultDisplayConfig;
		public float memCacheSizePercent;// 缓存百分比，android系统分配给每个apk内存的大小
		public int memCacheSize;// 内存缓存百分比
		public int diskCacheSize;// 磁盘百分比
		public int poolSize = 3;// 默认的线程池线程并发数量
		public int originalDiskCacheSize = 30 * 1024 * 1024;// 50MB

		public FinalBitmapConfig(Context context) {
			defaultDisplayConfig = new BitmapDisplayConfig();

			defaultDisplayConfig.setAnimation(null);
			defaultDisplayConfig.setAnimationType(BitmapDisplayConfig.AnimationType.fadeIn);

			// 设置图片的显示最大尺寸（为屏幕的大小,默认为屏幕宽度的1/3）
			DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
			int defaultWidth = (int) Math.floor(displayMetrics.widthPixels / 4);
			defaultDisplayConfig.setBitmapHeight(defaultWidth);
			defaultDisplayConfig.setBitmapWidth(defaultWidth);

		}

		public void init() {
			if (downloader == null)
				downloader = new SimpleHttpDownloader();

			if (displayer == null)
				displayer = new SimpleDisplayer();

			bitmapProcess = new BitmapProcess(downloader, cachePath, originalDiskCacheSize);
		}

	}

}
