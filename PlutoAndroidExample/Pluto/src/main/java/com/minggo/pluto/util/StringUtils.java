package com.minggo.pluto.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字符串操作工具类
 * @author minggo
 * @time 2014-12-2下午2:27:07
 */
public class StringUtils {
	private final static Pattern emailer = Pattern
			.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	// private final static SimpleDateFormat dateFormater = new
	// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// private final static SimpleDateFormat dateFormater2 = new
	private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
	private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
	private static final String regEx_head = "<head[^>]*?>[\\s\\S]*?<\\/head>"; // 定义style的正则表达式
	private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
	private static final String regEx_space = "\\s*|\t|\r|\n";// 定义空格回车换行符

	// SimpleDateFormat("yyyy-MM-dd");

	@SuppressLint("SimpleDateFormat")
	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@SuppressLint("SimpleDateFormat")
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	/**
	 * 将字符串转位日期类型
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormater.get().parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 将字符串转位年月日
	 * 
	 * @param sdate
	 * @return
	 */
	public static Date toDate2(String sdate) {
		try {
			return dateFormater2.get().parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 将字符串转位时分秒
	 * 
	 * @param sdate
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String toDate3(long sdate) {
		try {
			SimpleDateFormat df = new SimpleDateFormat("HH:mm");
			return df.format(sdate);
		} catch (Exception exception) {
			exception.printStackTrace();
			return null;
		}

	}

	/**
	 * 以友好的方式显示时间
	 * 
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(String sdate) {
		Date time = toDate(sdate);
		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000, 1)
						+ "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天";
		} else if (days > 2 && days <= 10) {
			ftime = days + "天前";
		} else if (days > 10) {
			ftime = dateFormater2.get().format(time);
		}
		return ftime;
	}

	/**
	 * 判断给定字符串时间是否为今日
	 * 
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate) {
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if (time != null) {
			String nowDate = dateFormater2.get().format(today);
			String timeDate = dateFormater2.get().format(time);
			if (nowDate.equals(timeDate)) {
				b = true;
			}
		}
		return b;
	}

	/**
	 * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是不是一个合法的电子邮件地址
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.trim().length() == 0)
			return false;
		return emailer.matcher(email).matches();
	}

	/**
	 * 字符串转整数
	 * 
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
		    e.printStackTrace();
        }

		return defValue;
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if (obj == null)
			return 0;
		return toInt(obj.toString(), 0);
	}

	/**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static long toLong(String obj) {
		try {
			return Long.parseLong(obj);
		} catch (Exception e) {
		    e.printStackTrace();
        }

		return 0;
	}

	/**
	 * 字符串转布尔值
	 * 
	 * @param b
	 * @return 转换异常返回 false
	 */
	public static boolean toBool(String b) {
		try {
			return Boolean.parseBoolean(b);
		} catch (Exception e) {
            e.printStackTrace();
		}
		return false;
	}

	/**
	 * 计算相差的天数
	 * 
	 * @param sdate
	 */
	public static int getCompareDay(String sdate) {
		Date time = toDate(sdate);
		if (time == null) {
			return 0;
		}

		Calendar cal = Calendar.getInstance();
		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		return days;
	}

	/**
	 * 判断字符长度
	 * @param min
	 * @param max
	 * @param str
	 * @return
	 */
	public static boolean check(int min ,int max,String str){

		int leng1 = str.length();
		int leng2 = str.getBytes().length;

		int result = (leng1+leng2)/2;
		if (min<=result&&max>=result) {
			return true;
		}
		return false;
	}

	/**
	 * @param htmlStr
	 * @return 删除Html标签
	 */
	public static String delHTMLTag(String htmlStr) {
		Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签

		Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
		Matcher m_space = p_space.matcher(htmlStr);
		htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
		return htmlStr.trim(); // 返回文本字符串
	}

	/**
	 * @param htmlStr
	 * @return 删除Html标签
	 */
	public static String delStyleScript(String htmlStr) {
		Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Pattern p_head = Pattern.compile(regEx_head, Pattern.CASE_INSENSITIVE);
		Matcher m_head = p_head.matcher(htmlStr);
		htmlStr = m_head.replaceAll(""); // 过滤style标签

		/*Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
		Matcher m_space = p_space.matcher(htmlStr);
		htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
*/
		return htmlStr.trim(); // 返回文本字符串
	}

	/**
	 * 保留空格回车去掉html
	 * @param htmlStr
	 * @return
	 */
	public static String delHTMLTagSimple(String htmlStr) {
		Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签
		return htmlStr.trim(); // 返回文本字符串
	}

	/**
	 * 过滤空格回车标签
	 * @param htmlStr
	 * @return
	 */
	public static String deleteBlank(String htmlStr) {
		Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
		Matcher m_space = p_space.matcher(htmlStr);
		htmlStr = m_space.replaceAll("");
		return htmlStr.trim(); // 返回文本字符串
	}

	public static String getTextFromHtml(String htmlStr) {
		htmlStr = delHTMLTag(htmlStr);
		htmlStr = htmlStr.replaceAll("&nbsp;", "");
		//htmlStr = htmlStr.substring(0, htmlStr.indexOf("。") + 1);
		return htmlStr;
	}

	/**
	 * 把每个段落的半角符号转化为全角符号
	 * @param input
	 * @return
	 */
	public static String halfToFull(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 32) //半角空格
			{
				c[i] = (char) 12288;
				continue;
			}

			//根据实际情况，过滤不需要转换的符号
			//if (c[i] == 46) //半角点号，不转换
			// continue;

			if (c[i] > 32 && c[i] < 127)    //其他符号都转换为全角
				c[i] = (char) (c[i] + 65248);
		}
		return new String(c);
	}


	/**
	 *
	 * 判断每行的尾端是否包含换行符
	 * @param line
	 * @return
	 */
	public static boolean needScale(String line) {
		if (line.length() == 0) {
			return false;
		} else {
			return line.charAt(line.length() - 1) != '\n';
		}
	}

	public static void testHtmlCutTag() {
		String str = "<div style=\"text-align:center;\"> 整治“四风”   清弊除垢<br/><span style=\"font-size:14px;\"> </span><span style=\"font-size:18px;\">公司召开党的群众路线教育实践活动动员大会</span><br/></div>";
		System.out.println("result-->"+getTextFromHtml(str));
	}

	/**
	 * 替换去除换行符为""
	 *
	 * @param strParagraph 要处理的文本
	 * @return 0坐标：替换过换行符后的文本，1坐标：匹配到的换行符
	 */
	public static String[] clearNewLineSign(String strParagraph) {
		String strReturn = "";
		if (strParagraph.contains("\r\n")) {
			strReturn = "\r\n";
			strParagraph = strParagraph.replace("\r\n", "");
		} else if (strParagraph.contains("\n")) {
			strReturn = "\n";
			strParagraph = strParagraph.replace("\n", "");
		} else if (strParagraph.contains("\\r\\n")) {
			strReturn = "\\r\\n";
			strParagraph = strParagraph.replace("\\r\\n", "");
		}
		return new String[]{strParagraph, strReturn};
	}

	/** 对字符串长度大于count，则取[0-(count-省略号长度))，并添加省略号 */
	public static String ellipsis(String content, int count) {
		String ellipsisSign = "……";
		if (content.length() > count) {
			content = content.substring(0, count - ellipsisSign.length()) + ellipsisSign;
		}
		return content;
	}
}