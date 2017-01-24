package com.minggo.pluto.util;

import android.content.Context;

import java.util.Locale;

/**
 *
 */
public class DisplayUtil {
	public static final int SCREEN_WIDTH_PIXELS_480 = 480;
	public static final int SCREEN_WIDTH_PIXELS_720 = 720;
	public static final int SCREEN_WIDTH_PIXELS_1080 = 1080;

	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变（有精度损失）
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变（无精度损失）
	 */
	public static float px2dipByFloat(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (pxValue / scale);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变（有精度损失），类似Context.getDimensionPixelSize方法（四舍五入）
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变（无精度损失），类似Context.getDimension方法
	 */
	public static float dip2pxByFloat(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (dipValue * scale);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 屏幕宽度
	 */
	public static int getScreenWidthPixels(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * 屏幕高度
	 */
	public static int getScreenHeightPixels(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	public static boolean isTargetScreenWidth(Context context, int targetPixels) {
		return getScreenWidthPixels(context) == targetPixels;
	}

	public static boolean isTargetScreenHeight(Context context, int targetPixels) {
		return getScreenHeightPixels(context) == targetPixels;
	}

	public static String getDisplayInfo(Context context) {
		String infoFormat = "宽:%s,高:%s 宽Dip:%s,高Dip:%s\ndpi:%s,1dip=%sPixels";
		int screenWidthPixels = DisplayUtil.getScreenWidthPixels(context);
		int screenHeightPixels = DisplayUtil.getScreenHeightPixels(context);
		float density = context.getResources().getDisplayMetrics().density;
		Object[] infoFormatValue = {
				screenWidthPixels, screenHeightPixels,
				((int) (screenWidthPixels / density)), ((int) (screenHeightPixels / density)),
				context.getResources().getDisplayMetrics().densityDpi, density
		};
		return String.format(Locale.getDefault(), infoFormat, infoFormatValue);
	}
}
