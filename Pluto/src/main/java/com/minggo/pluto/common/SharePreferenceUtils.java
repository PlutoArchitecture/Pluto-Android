package com.minggo.pluto.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author minggo
 * @time 2015-1-12上午10:00:41
 */
public class SharePreferenceUtils {
	/**
	 * 用户自定义配置的文件名
	 */
	public static final String USER_CONFING = "USER_CONFIG";
	/**
	 * 用户id字段
	 */
	public static final String USER_ENCRYPT_UID = "encryptId";
	/**
	 * 用户名称
	 */
	public static final String USER_NAME = "username";
	/** 是否记住用户名称 **/
	public static final String IS_REMEMBER_NAME = "IS_REMEMBER_NAME";
	/**
	 * 收藏新书
	 */
	public static final String BOOK_COLLECT_NEED_REFRESH= "refresh_collection";
	
	/**
	 * 用户选择的预读章节
	 */
	public static final String USER_CONFING_ADVANCE_READ = "ADVACNED_READ";
	/**
	 * 不再提示扣费，自动订阅标记
	 * <p>1：自动订阅 0：非自动订阅，服务器数据</p>
	 */
	public static final String USER_CONFIG_IS_AUTO_BUY = "IS_AUTO_BUY";
	/**
	 * 书架更新提醒
	 */
	public static final String USER_CONFIG_IS_BOOK_COLLECT_UPDATE_NOTIFICATION_BOOL = "IS_BOOK_COLLECT_UPDATE_NOTIFICATION";
	/**
	 * 作者账户的评论提醒
	 */
	public static final String USER_CONFIG_IS_AUTHOR_COMMENT_NOTIFICATION_ENCRYPT_ID_BOOL = "IS_AUTHOR_COMMENT_NOTIFICATION_BOOL";
	/**
	 * 作者账户的打赏提醒
	 */
	public static final String USER_CONFIG_IS_AUTHOR_REWARD_NOTIFICATION_ENCRYPT_ID_BOOL = "IS_AUTHOR_REWARD_NOTIFICATION_BOOL";
	/**
	 * 用户意见反馈回复未读提醒
	 */
	public static final String USER_FEEDBACK_UNREAD_BOOL = "USER_FEEDBACK_UNREAD_BOOL_";
	/**
	 * 应用最后退出时间，搭配用户encryptId使用
	 */
	public static final String LAST_EXIT_TIME_ENCRYPT_ID_STRING = "last_exit_time";

	/** 是否包月 **/
	public static final String IS_MONTHLY_BOOL = "IS_MONTHLY_BOOL";
	/** 包月只剩7天或不足7天 **/
	public static final String MONTHLY_ONLY_SEVEN_DAYS = "MONTHLY_ONLY_SEVEN_DAYS";
	/** 收藏书架删除提示 **/
	public static final String BOOK_COLLECT_DELETE_IS_NOT_PROMPT = "BOOK_COLLECT_DELETE_IS_NOT_PROMPT";

    /** 章节列表排序方式 **/
    public static final String BOOK_MENU_ORDER_BOOL = "BOOK_MENU_ORDER_";

    /** 评论列表排序方式 **/
    public static final String BOOK_COMMENT_INFO_ORDER_BOOL = "BOOK_COMMENT_ORDER_";

    /** 我的书架默认推荐图书 1:男生 2：女生**/
    public static final String BOOK_COLLECT_SEX_INT = "BOOK_COLLECT_SEX_INT";

	/** 上传失败的阅读记录列表，格式为JSON */
	public static final String ERROR_BOOK_NET_HISTORY_JSON = "ERROR_BOOK_NET_HISTORY_JSON";
	/** 阅读页：字体大小 */
	public static final String SP_KEY_rdfontsize = "rdfontsize";
	/** 阅读页：翻页效果 */
	public static final String SP_KEY_rdreadspecialeffects = "rdreadspecialeffects";
	/** 阅读页：行距大小类型 */
	public static final String SP_KEY_rdfontpadding = "rdfontpadding";
	/** 阅读页：夜晚模式 */
	public static final String SP_KEY_nightstyle = "nightstyle";
	/** 阅读页：初次引导 */
	public static final String SP_KEY_book_read_prompt = "bookread_prompt";
	/** 阅读页：音量键翻页 */
	public static final String SP_KEY_voice_flag = "voiceflag";
	/**提交反馈内容的最新时间*/
	public static final String FD_KEY_feedback_time="feedbacktime";

	public static boolean contains(Context context, String key) {
		return context.getSharedPreferences(USER_CONFING, Context.MODE_WORLD_READABLE).contains(key);
	}

	/**
	 * 保存String到默认sp（USER_CONFIG）
	 *
	 * @param context
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean putStringByDefaultSP(Context context, String key, String value) {
		return putString(context, SharePreferenceUtils.USER_CONFING, key, value);
	}

	/**
	 * 保存StringSet到默认sp（USER_CONFIG）
	 * 注意：返回值无序，内部实现为HashSet，强插LinkedHashSet也没用
	 *
	 * @param key    TextUtils.isEmpty(key)不为true
	 * @param values values != null && values.size() != 0不为true
	 */
	public static boolean putStringSetByDefaultSP(Context context, String key, Set<String> values) {
		if (values != null && values.size() != 0 && context != null) {
			if (TextUtils.isEmpty(key)) {
				return context.getSharedPreferences(USER_CONFING, Context.MODE_WORLD_WRITEABLE)
						.edit()
						.putStringSet(key, values)
						.commit();
			}
		}
		return false;
	}

	/**
	 * 存储有序列表，内部实现：List -> gson.toJson -> putString
	 */
	public static boolean putOrderStringListByDefaultSP(Context context, String key, List<String> values) {
		if (values != null && context != null) {
			if (!TextUtils.isEmpty(key)) {
				Gson gson = new Gson();
				String valuesJSON;
				try {
					valuesJSON = gson.toJson(values, new TypeToken<List<String>>() {
					}.getType());
					return context.getSharedPreferences(USER_CONFING, Context.MODE_WORLD_WRITEABLE).edit().putString(key, valuesJSON).commit();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	/**
	 * 保存String到sp
	 * @param context
	 * @param name
	 * @param content
	 * @return
	 */
	public static boolean putString(Context context,String name,String content){
		return putString(context,name,name,content);
	}
	
	/**
	 * 保存String到sp
	 * @param context
	 * @param name sp的名称
	 * @param key 存的字段名称
	 * @param content 内容
	 * @return
	 */
	public static boolean putString(Context context,String name,String key,String content){
		if(content != null && name != null && context != null){
			return context.getSharedPreferences(name, Context.MODE_WORLD_WRITEABLE)
			.edit()
			.putString(key, content)
			.commit();
		}
		return false;
	}

	/**
	 * 保存int到sp，sp name使用USER_CONFING
	 * @param context
	 * @param key 存的字段名称
	 * @param content 内容
	 * @return
	 */
	public static boolean putInt(Context context,String key,int content){
		return putInt(context,USER_CONFING,key,content);
	}

	/**
	 * 保存int到sp
	 * @param context
	 * @param name sp的名称
	 * @param key 存的字段名称
	 * @param content 内容
	 * @return
	 */
	public static boolean putInt(Context context,String name,String key,int content){
		if(name != null && context != null){
			return context.getSharedPreferences(name, Context.MODE_WORLD_WRITEABLE).edit().putInt(key, content).commit();
		}
		return false;
	}

	public static boolean putLong(Context context, String spFileName, String key, long value) {
		return notNull(context, spFileName) && getSharedPreferences(context, spFileName).edit().putLong(key, value).commit();
	}

	public static boolean putLong(Context context, String key, long value) {
		return putLong(context, USER_CONFING, key, value);
	}

	/**
	 * 保存boolean值
	 * @param context
	 * @param name
	 * @param key
	 * @param content
	 * @return
	 */
	public static boolean putBoolean(Context context,String name,String key,boolean content){
		if(context != null){
			return context.getSharedPreferences(name, Context.MODE_WORLD_WRITEABLE)
			.edit()
			.putBoolean(key, content)
			.commit();
		}
		return false;
	}

	public static boolean putBooleanByDefaultSP(Context context, String key, boolean value) {
		return putBoolean(context, USER_CONFING, key, value);
	}

	/**
	 * 获取boolean值
	 * @param context
	 * @param name
	 * @return
	 */
	public static boolean getBoolean(Context context,String name,String key){
		if(context != null && name != null){
			return context.getSharedPreferences(name, Context.MODE_WORLD_READABLE).getBoolean(key, false);
		}
		return false;
	}

	public static boolean getBooleanByDefaultSP(Context context, String key, boolean defaultValue) {
		boolean result = defaultValue;
		if (context != null && !TextUtils.isEmpty(key)) {
			result = context.getSharedPreferences(USER_CONFING, Context.MODE_WORLD_READABLE).getBoolean(key, defaultValue);
		}
		return result;
	}

	/**
	 * 从默认sp（USER_CONFIG）获取StringSet
	 * 注意：返回值无序，内部实现为HashSet，强插LinkedHashSet也没用
	 */
	public static Set<String> getStringSetByDefaultSP(Context context, String key) {
		Set<String> result = new HashSet<>();
		if (context != null && !TextUtils.isEmpty(key)) {
			result = context.getSharedPreferences(USER_CONFING, Context.MODE_WORLD_READABLE).getStringSet(key, result);
		}
		return result;
	}

	/**
	 * 获取有序字符串列表，内部实现：sp.getString -> gson.formJSON -> List
	 */
	public static List<String> getOrderStringListByDefaultSP(Context context, String key) {
		List<String> result = new ArrayList<>();
		if (context != null && !TextUtils.isEmpty(key)) {
			String json = context.getSharedPreferences(USER_CONFING, Context.MODE_WORLD_READABLE).getString(key, "");
			if (!TextUtils.isEmpty(json)) {
				Gson gson = new Gson();
				result = gson.fromJson(json, new TypeToken<List<String>>() {
				}.getType());
			}
		}
		return result;
	}

    /**
     * 从SP获取带默认的String
     * @param context
     * @param name
     * @param defaultStr
     * @param nothing
     * @return
     */
    public static String getStringByName(Context context, String name, String defaultStr, boolean nothing){
        if (context!=null&&name!=null){
            return context.getSharedPreferences(name,Context.MODE_WORLD_READABLE).getString(name,defaultStr);
        }
        return defaultStr;
    }

	/**
	 * 获取默认sp（USER_CONFIG）中的String内容
	 */
	public static String getStringByDefaultSP(Context context, String key, String defaultValue) {
		return getString(context, USER_CONFING, key, defaultValue);
	}

	/**
	 * 获取SP中的String内容
	 * @param context
	 * @param name
	 * @param key
	 * @return 失败返回null
	 */
	public static String getString(Context context,String name,String key){
		if(context != null && name != null){
			return context.getSharedPreferences(name, Context.MODE_WORLD_READABLE).getString(key,null);
		}
		return null;
	}

	/**
	 * 获取SP中的String内容
	 *
	 * @return 失败返回默认值defaultValue
	 */
	public static String getString(Context context, String name, String key, String defaultValue) {
		String result = defaultValue;
		if (context != null && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(key)) {
			result = context.getSharedPreferences(name, Context.MODE_WORLD_READABLE).getString(key, defaultValue);
		}
		return result;
	}

	public static int getInt(Context context, String spFileName, String key, int defaultValue) {
		if (notNull(context, spFileName)) {
			return getSharedPreferences(context, spFileName).getInt(key, defaultValue);
		}
		return defaultValue;
	}

	public static int getInt(Context context, String key, int defaultValue) {
		return getInt(context, USER_CONFING, key, defaultValue);
	}

	public static long getLong(Context context, String spFileName, String key, long defaultValue) {
		if (notNull(context, spFileName)) {
			return getSharedPreferences(context, spFileName).getLong(key, defaultValue);
		}
		return defaultValue;
	}

	public static long getLong(Context context, String key, long defaultValue) {
		return getLong(context, USER_CONFING, key, defaultValue);
	}

	/**
	 * 获取sp中的多个值
	 * @param context
	 * @param name
	 * @return 有可能为null
	 */
	public static Map<String,?>  getAllString(Context context,String name){
		if(context != null && name != null){
			return context.getSharedPreferences(name, Context.MODE_WORLD_READABLE).getAll();
		}
		return null;
	}
	
	/**
	 * 清除某个sp文件
	 * @param context
	 * @param name
	 */
	public static void clearString(Context context,String name){
		context.getSharedPreferences(name, Context.MODE_WORLD_WRITEABLE)
		.edit()
		.clear().commit();
	}
	
	/**
	 * 保存对象到sp
	 * @param <T>
	 * @param t
	 */
	public static <T> boolean put(Context context,T t){
		
		if(t == null || context == null){
			return false;
		} 
		
		try {
			Editor edit = context.getSharedPreferences(t.getClass().getSimpleName(), Context.MODE_WORLD_WRITEABLE).edit();
			Field[] fields = t.getClass().getDeclaredFields();
			for(Field field:fields){
				field.setAccessible(true);
				Object value = field.get(t);
				if(value!= null){
					Class<?> type = field.getType();
					String name = field.getName();
					if(int.class.equals(type) || Integer.class.equals(type)){
						edit.putInt(name,(Integer)value);
					}else if(String.class.equals(type)){
						edit.putString(name,String.valueOf(value));
					}else if(long.class.equals(type) || Long.class.equals(type)){
						edit.putLong(name,(Long)value);
					}else if(double.class.equals(type) || Double.class.equals(type)){
						edit.putString(name,String.valueOf(value));
					}else if(float.class.equals(type) || Float.class.equals(type)){
						edit.putFloat(name, (Float)value);
					}
					edit.commit();
				}
			}
			return true;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 从sp获取对象
	 * @param <T>
	 * @param context
	 * @return 如果不存在则返回null
	 */
	public static <T> T get(Context context,Class<T> clazz){
		if(clazz == null || context == null){
			return null;
		}
		
		try {
			SharedPreferences sp = context.getSharedPreferences(clazz.getSimpleName(), Context.MODE_WORLD_READABLE);
			Object instance = clazz.newInstance();
			
			Field[] fields = clazz.getDeclaredFields();
			for(Field field:fields){
				field.setAccessible(true);
					Class<?> type = field.getType();
					String name = field.getName();
					
					if(int.class.equals(type) || Integer.class.equals(type)){
						int value = sp.getInt(name,-2001);
						if(value != -2001){
							field.setInt(instance, value);
						}
					}else if(String.class.equals(type)){
						String value = sp.getString(name, null);
						if(value != null){
							field.set(instance, value);
						}
					}else if(long.class.equals(type) || Long.class.equals(type)){
						long value = sp.getLong(name,-2001);
						if(value != -2001){
							field.setLong(instance, value);
						}
					}else if(double.class.equals(type) || Double.class.equals(type)){
						String value = sp.getString(name, null);
						if(value!= null){
							field.setDouble(instance, Double.valueOf(value));
						}
					}else if(float.class.equals(type) || Float.class.equals(type)){
						float value = sp.getFloat(name, -2001);
						if(value!= -2001){
							field.setFloat(instance, value);
						}
					}
			}
			return (T) instance;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/** 得到一个sharedPreferences中的key值对 */
	public static String getPrefByPackage(Context context, String name, String def) {
		String pkg = context.getPackageName();// 用包名当作文件名
		SharedPreferences prefs = context.getSharedPreferences(pkg, Context.MODE_PRIVATE);
		return prefs.getString(name, def);
	}

	/** 设置一个sharedPreferences中的key值对 */
	public static void setPrefByPackage(Context context, String name, String value) {
		String pkg = context.getPackageName();// 用包名当作文件名
		SharedPreferences prefs = context.getSharedPreferences(pkg, Context.MODE_PRIVATE);
		Editor ed = prefs.edit();
		ed.putString(name, value);
		ed.apply();
	}

	public static boolean getBooleanPrefByPackage(Context context, String name, boolean def) {
		String pkg = context.getPackageName();// 用包名当作文件名
		SharedPreferences prefs = context.getSharedPreferences(pkg, Context.MODE_PRIVATE);
		return prefs.getBoolean(name, def);
	}

	public static void setBooleanPrefByPackage(Context context, String name, boolean value) {
		String pkg = context.getPackageName();// 用包名当作文件名
		SharedPreferences prefs = context.getSharedPreferences(pkg, Context.MODE_PRIVATE);
		Editor ed = prefs.edit();
		ed.putBoolean(name, value);
		ed.apply();
	}

	/**
	 * 清空
	 * @param <T>
	 * @param context
	 * @param clazz
	 */
	public static <T> boolean clear(Context context,Class<T> clazz){
		if(clazz != null && context != null){
			return context
			.getSharedPreferences(clazz.getSimpleName(),Context.MODE_WORLD_WRITEABLE)
			.edit()
			.clear()
			.commit();
		}
		return false;
	}
	
	/**
	 * 判断字符串是否为空
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		if (str!=null&&!str.equals("")) {
			return false;
		}
		return true;
	}

	public static void remove(Context context, String... keys) {
		if (context != null && keys != null && keys.length > 0) {
			Editor editor = context.getSharedPreferences(USER_CONFING, Context.MODE_WORLD_WRITEABLE).edit();

			for (String keyItem : keys) {
				editor.remove(keyItem);
			}

			editor.apply();
		}
	}

	private static boolean notNull(Context context, String spFileName) {
		return !TextUtils.isEmpty(spFileName) && context != null;
	}

	private static SharedPreferences getSharedPreferences(Context context, String spFileName) {
		return context.getSharedPreferences(spFileName, Context.MODE_PRIVATE);
	}
}
