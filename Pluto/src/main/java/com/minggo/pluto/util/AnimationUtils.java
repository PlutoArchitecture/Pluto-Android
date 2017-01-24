package com.minggo.pluto.util;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * 动画工具类
 * Created by zhengjinbin on 2016/8/11.
 */
public class AnimationUtils {
	public static final int DURATION_MILLIS_500 = 500;

	public static Animation getAlphaAnimation(float fromAlpha, float toAlpha, long durationMillis) {
		Animation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
		alphaAnimation.setDuration(durationMillis);
		return alphaAnimation;
	}
}
