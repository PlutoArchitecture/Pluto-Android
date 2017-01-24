package com.minggo.pluto.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期工具类
 * @author minggo
 * @time 2014-12-2下午2:14:11
 */
public class DateUtil {
	public static final String YYYY_MM_DD = "yyyy-MM-dd";
	public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS,Locale.getDefault());

	/**
	 * 将字符串转化为日期
	 *
	 * @param s
	 * @return
	 */
	public static Date stringToDateTime(String s) {
		if (null == s) {
			return new Date();
		}
		String pattern;
		if (s.matches("\\d{4}-\\d{2}-\\d{2}")) {
			pattern = YYYY_MM_DD;
		} else if (s.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
			pattern = YYYY_MM_DD_HH_MM_SS;
		} else {
			pattern = YYYY_MM_DD_HH_MM;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
		try {
			return sdf.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 年月日时分秒
	 * @param date
	 * @return
	 */
	public static String getTime(Date date) {
		TimeZone t = TimeZone.getTimeZone("GMT+08:00");// 获取东8区TimeZone
		Calendar calendar = Calendar.getInstance(t);
		if (date == null) {
			calendar.setTimeInMillis(System.currentTimeMillis());
		} else {
			calendar.setTime(date);
		}
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		int ss = calendar.get(Calendar.SECOND);
		int ms = calendar.get(Calendar.MILLISECOND);// 毫秒

		String time = year + "-" + (month < 10 ? "0" : "") + month + '-' + (day < 10 ? "0" : "") + day + ' ' + (hour < 10 ? "0" : "")
				+ hour + ':' + (min < 10 ? "0" : "") + min + ":" + (ss < 10 ? "0" : "") + ss ;
//				+ "." + (ms < 10 ? "00" : (ms < 100 ? "0" : "")) + ms;
		return time;
	}


	/**
	 * 年月日时分秒毫秒
	 * @param date
	 * @return
	 */
	public static String getTimes(Date date) {
		TimeZone t = TimeZone.getTimeZone("GMT+08:00");// 获取东8区TimeZone
		Calendar calendar = Calendar.getInstance(t);
		if (date == null) {
			calendar.setTimeInMillis(System.currentTimeMillis());
		} else {
			calendar.setTime(date);
		}
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		int ss = calendar.get(Calendar.SECOND);
		int ms = calendar.get(Calendar.MILLISECOND);// 毫秒

		String time = year + "-" + (month < 10 ? "0" : "") + month + '-' + (day < 10 ? "0" : "") + day + ' ' + (hour < 10 ? "0" : "")
				+ hour + ':' + (min < 10 ? "0" : "") + min + ":" + (ss < 10 ? "0" : "") + ss
				+ "." + (ms < 10 ? "00" : (ms < 100 ? "0" : "")) + ms;
		return time;
	}

	/**
	 * 年月日
	 * @param date
	 * @return
	 */
	public static String getDay(Date date) {
		TimeZone t = TimeZone.getTimeZone("GMT+08:00");// 获取东8区TimeZone
		Calendar calendar = Calendar.getInstance(t);
		if (date == null) {
			calendar.setTimeInMillis(System.currentTimeMillis());
		} else {
			calendar.setTime(date);
		}
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		String time = year + "-" + (month < 10 ? "0" : "") + month + '-' + (day < 10 ? "0" : "") + day;
		return time;
	}

	public static SimpleDateFormat getSimpleDateFormat(String template) {
		simpleDateFormat.applyPattern(template);
		return simpleDateFormat;
	}

	/**
	 * 格式化取当前时间
	 *
	 * @return
	 */
	public static String getThisDateTime() {
		return getSimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
	}

	/**
	 * 将yyyy-MM-dd HH:mm:ss格式的时间，同当前时间比对，格式化为：xx分钟前，xx小时前和日期
	 *
	 * @param datetime 需比对的时间
	 * @return
	 */
	public static String convert_before(String datetime) {
		if (TextUtils.isEmpty(datetime)) {
			return "";
		}

		try {
			long time = getSimpleDateFormat("yyyy-MM-dd HH:mm").parse(datetime).getTime();
			return convert_before(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 将对比后的时间，格式化为：xx分钟前，xx小时前和日期
	 *
	 * @param time 需比对的时间
	 * @return
	 */
	public static String convert_before(long time) {
		if (time < 0)
			return String.valueOf(time);

		int diffTime = (int) ((System.currentTimeMillis() - time) / 1000);
		if (diffTime < 86400 && diffTime > 0) {
			if (diffTime < 3600) {
				int min = (diffTime / 60);
				if (min == 0)
					return "刚刚";
				else
					return (diffTime / 60) + "分钟前";
			} else {
				return (diffTime / 3600) + "小时前";
			}
		} else {
			Calendar now = Calendar.getInstance();
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(time);
			if (c.get(Calendar.YEAR) == now.get(Calendar.YEAR) && c.get(Calendar.MONTH) == now.get(Calendar.MONTH)
					&& c.get(Calendar.DATE) == now.get(Calendar.DATE)) {
				return getSimpleDateFormat("HH:mm").format(c.getTime());
			}
			return dateInterval(now.getTime().getTime(), c.getTime().getTime()) + "天前";
		}
	}

	/**
	 * 计算出两个日期之间相差的天数
	 * 建议date1 大于 date2 这样计算的值为正数
	 *
	 * @param date1 日期1
	 * @param date2 日期2
	 * @return date1 - date2
	 */
	public static int dateInterval(long date1, long date2) {
		if (date2 > date1) {
			date2 = date2 + date1;
			date1 = date2 - date1;
			date2 = date2 - date1;
		}

		// Canlendar 该类是一个抽象类
		// 提供了丰富的日历字段
		// 本程序中使用到了
		// Calendar.YEAR    日期中的年份
		// Calendar.DAY_OF_YEAR     当前年中的天数
		// getActualMaximum(Calendar.DAY_OF_YEAR) 返回今年是 365 天还是366天
		Calendar calendar1 = Calendar.getInstance(); // 获得一个日历
		calendar1.setTimeInMillis(date1); // 用给定的 long 值设置此 Calendar 的当前时间值。

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(date2);
		// 先判断是否同年
		int y1 = calendar1.get(Calendar.YEAR);
		int y2 = calendar2.get(Calendar.YEAR);

		int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
		int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
		int maxDays = 0;
		int day = 0;
		if (y1 - y2 > 0) {
			day = numerical(maxDays, d1, d2, y1, y2, calendar2);
		} else {
			day = d1 - d2;
		}
		return day;
	}

	/**
	 * 日期间隔计算
	 * 计算公式(示例):
	 * 20121201- 20121212
	 * 取出20121201这一年过了多少天 d1 = 天数  取出20121212这一年过了多少天 d2 = 天数
	 * 如果2012年这一年有366天就要让间隔的天数+1，因为2月份有29日。
	 *
	 * @param maxDays  用于记录一年中有365天还是366天
	 * @param d1       表示在这年中过了多少天
	 * @param d2       表示在这年中过了多少天
	 * @param y1       当前为2012年
	 * @param y2       当前为2012年
	 * @param calendar 根据日历对象来获取一年中有多少天
	 * @return 计算后日期间隔的天数
	 */
	public static int numerical(int maxDays, int d1, int d2, int y1, int y2, Calendar calendar) {
		int day = d1 - d2;
		int betweenYears = y1 - y2;
		List<Integer> d366 = new ArrayList<>();

		if (calendar.getActualMaximum(Calendar.DAY_OF_YEAR) == 366) {
			System.out.println(calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
			day += 1;
		}

		for (int i = 0; i < betweenYears; i++) {
			// 当年 + 1 设置下一年中有多少天
			calendar.set(Calendar.YEAR, (calendar.get(Calendar.YEAR)) + 1);
			maxDays = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);
			// 第一个 366 天不用 + 1 将所有366记录，先不进行加入然后再少加一个
			if (maxDays != 366) {
				day += maxDays;
			} else {
				d366.add(maxDays);
			}
			// 如果最后一个 maxDays 等于366 day - 1
			if (i == betweenYears - 1 && betweenYears > 1 && maxDays == 366) {
				day -= 1;
			}
		}

		for (int i = 0; i < d366.size(); i++) {
			// 一个或一个以上的366天
			if (d366.size() >= 1) {
				day += d366.get(i);
			}
		}
		return day;
	}

	/**
	 * 获取当前的Unix时间戳（格式：2015/8/24 16:56:44）
	 */
	public static long getUnixTimestampByCurrentDate() {
		return getUnixTimestampByDate(new Date());
	}

	/**
	 * 获取date参数的Unix时间戳（格式：2015/8/24 16:56:44）
	 */
	public static long getUnixTimestampByDate(Date date) {
		return date.getTime() / 1000;
	}

	/**
	 * 根据时间戳获取date对象
	 */
	public static Date getDateByUnixTimestamp(long unixTimestamp) {
		return new Date(unixTimestamp * 1000);
	}
}