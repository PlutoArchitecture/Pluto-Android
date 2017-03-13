package com.minggo.pluto.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.minggo.pluto.common.AppContext;
import com.minggo.pluto.db.manager.DataManager;
import com.minggo.pluto.db.manager.DataManagerStub;

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
public class SharePreferenceUtils extends DataManagerStub{

	public static SharePreferenceUtils sharePreferenceUtils;

	private static Context context;

	private SharePreferenceUtils(){

	}

	public static SharePreferenceUtils getInstance(){
		if (sharePreferenceUtils==null){
			synchronized (SharePreferenceUtils.class){
				if (sharePreferenceUtils==null){
					sharePreferenceUtils = new SharePreferenceUtils();
					context = AppContext.getInstance().context;
				}
			}
		}
		return sharePreferenceUtils;
	}

	/**
	 * 用户自定义配置的文件名
	 */
	public static final String USER_CONFING = "USER_CONFIG";


	public boolean contains(String key) {
		return context.getSharedPreferences(USER_CONFING, Context.MODE_PRIVATE).contains(key);
	}

	/**
	 * 保存String到默认sp（USER_CONFIG）
	 *
	 * @param context
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean putStringByDefaultSP(String key, String value) {
		return putString(SharePreferenceUtils.USER_CONFING, key, value);
	}

	/**
	 * 保存StringSet到默认sp（USER_CONFIG）
	 * 注意：返回值无序，内部实现为HashSet，强插LinkedHashSet也没用
	 *
	 * @param key    TextUtils.isEmpty(key)不为true
	 * @param values values != null && values.size() != 0不为true
	 */
	public boolean putStringSetByDefaultSP(String key, Set<String> values) {
		if (values != null && values.size() != 0 && context != null) {
			if (TextUtils.isEmpty(key)) {
				return context.getSharedPreferences(USER_CONFING, Context.MODE_PRIVATE)
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
	public boolean putOrderStringListByDefaultSP( String key, List<String> values) {
		if (values != null && context != null) {
			if (!TextUtils.isEmpty(key)) {
				Gson gson = new Gson();
				String valuesJSON;
				try {
					valuesJSON = gson.toJson(values, new TypeToken<List<String>>() {
					}.getType());
					return context.getSharedPreferences(USER_CONFING, Context.MODE_PRIVATE).edit().putString(key, valuesJSON).commit();
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
	public boolean putString(String name,String content){
		return putString(name,name,content);
	}
	
	/**
	 * 保存String到sp
	 * @param context
	 * @param name sp的名称
	 * @param key 存的字段名称
	 * @param content 内容
	 * @return
	 */
	public boolean putString(String name,String key,String content){
		if(content != null && name != null && context != null){
			return context.getSharedPreferences(name, Context.MODE_PRIVATE)
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
	public boolean putInt(String key,int content){
		return putInt(USER_CONFING,key,content);
	}

	/**
	 * 保存int到sp
	 * @param context
	 * @param name sp的名称
	 * @param key 存的字段名称
	 * @param content 内容
	 * @return
	 */
	public boolean putInt(String name,String key,int content){
		if(name != null && context != null){
			return context.getSharedPreferences(name, Context.MODE_PRIVATE).edit().putInt(key, content).commit();
		}
		return false;
	}

	public boolean putLong( String spFileName, String key, long value) {
		return notNull( spFileName) && getSharedPreferences( spFileName).edit().putLong(key, value).commit();
	}

	public boolean putLong( String key, long value) {
		return putLong( USER_CONFING, key, value);
	}

	/**
	 * 保存boolean值
	 * @param context
	 * @param name
	 * @param key
	 * @param content
	 * @return
	 */
	public boolean putBoolean(String name,String key,boolean content){
		if(context != null){
			return context.getSharedPreferences(name, Context.MODE_PRIVATE)
			.edit()
			.putBoolean(key, content)
			.commit();
		}
		return false;
	}

	public boolean putBooleanByDefaultSP( String key, boolean value) {
		return putBoolean( USER_CONFING, key, value);
	}

	/**
	 * 获取boolean值
	 * @param context
	 * @param name
	 * @return
	 */
	public boolean getBoolean(String name,String key){
		if(context != null && name != null){
			return context.getSharedPreferences(name, Context.MODE_PRIVATE).getBoolean(key, false);
		}
		return false;
	}

	public boolean getBooleanByDefaultSP( String key, boolean defaultValue) {
		boolean result = defaultValue;
		if (context != null && !TextUtils.isEmpty(key)) {
			result = context.getSharedPreferences(USER_CONFING, Context.MODE_PRIVATE).getBoolean(key, defaultValue);
		}
		return result;
	}

	/**
	 * 从默认sp（USER_CONFIG）获取StringSet
	 * 注意：返回值无序，内部实现为HashSet，强插LinkedHashSet也没用
	 */
	public Set<String> getStringSetByDefaultSP( String key) {
		Set<String> result = new HashSet<>();
		if (context != null && !TextUtils.isEmpty(key)) {
			result = context.getSharedPreferences(USER_CONFING, Context.MODE_PRIVATE).getStringSet(key, result);
		}
		return result;
	}

	/**
	 * 获取有序字符串列表，内部实现：sp.getString -> gson.formJSON -> List
	 */
	public List<String> getOrderStringListByDefaultSP( String key) {
		List<String> result = new ArrayList<>();
		if (context != null && !TextUtils.isEmpty(key)) {
			String json = context.getSharedPreferences(USER_CONFING, Context.MODE_PRIVATE).getString(key, "");
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
    public String getStringByName( String name, String defaultStr, boolean nothing){
        if (context!=null&&name!=null){
            return context.getSharedPreferences(name,Context.MODE_PRIVATE).getString(name,defaultStr);
        }
        return defaultStr;
    }

	/**
	 * 获取默认sp（USER_CONFIG）中的String内容
	 */
	public String getStringByDefaultSP( String key, String defaultValue) {
		return getString( USER_CONFING, key, defaultValue);
	}

	/**
	 * 获取SP中的String内容
	 * @param context
	 * @param name
	 * @param key
	 * @return 失败返回null
	 */
	public String getString(String name,String key){
		if(context != null && name != null){
			return context.getSharedPreferences(name, Context.MODE_PRIVATE).getString(key,null);
		}
		return null;
	}

	/**
	 * 获取SP中的String内容
	 *
	 * @return 失败返回默认值defaultValue
	 */
	public String getString( String name, String key, String defaultValue) {
		String result = defaultValue;
		if (context != null && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(key)) {
			result = context.getSharedPreferences(name, Context.MODE_PRIVATE).getString(key, defaultValue);
		}
		return result;
	}

	public int getInt( String spFileName, String key, int defaultValue) {
		if (notNull( spFileName)) {
			return getSharedPreferences( spFileName).getInt(key, defaultValue);
		}
		return defaultValue;
	}

	public int getInt( String key, int defaultValue) {
		return getInt( USER_CONFING, key, defaultValue);
	}

	public long getLong( String spFileName, String key, long defaultValue) {
		if (notNull( spFileName)) {
			return getSharedPreferences( spFileName).getLong(key, defaultValue);
		}
		return defaultValue;
	}

	public long getLong( String key, long defaultValue) {
		return getLong( USER_CONFING, key, defaultValue);
	}

	/**
	 * 获取sp中的多个值
	 * @param context
	 * @param name
	 * @return 有可能为null
	 */
	public  Map<String,?>  getAllString(String name){
		if(context != null && name != null){
			return context.getSharedPreferences(name, Context.MODE_PRIVATE).getAll();
		}
		return null;
	}
	
	/**
	 * 清除某个sp文件
	 * @param context
	 * @param name
	 */
	public static void clearString(String name){
		context.getSharedPreferences(name, Context.MODE_PRIVATE)
		.edit()
		.clear().commit();
	}
	
	/**
	 * 保存对象到sp
	 * @param <T>
	 * @param t
	 */
	public static <T> boolean put(T t){
		
		if(t == null || context == null){
			return false;
		} 
		
		try {
			Editor edit = context.getSharedPreferences(t.getClass().getSimpleName(), Context.MODE_PRIVATE).edit();
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
	public static <T> T get(Class<T> clazz){
		if(clazz == null || context == null){
			return null;
		}
		
		try {
			SharedPreferences sp = context.getSharedPreferences(clazz.getSimpleName(), Context.MODE_PRIVATE);
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
	public String getPrefByPackage( String name, String def) {
		String pkg = context.getPackageName();// 用包名当作文件名
		SharedPreferences prefs = context.getSharedPreferences(pkg, Context.MODE_PRIVATE);
		return prefs.getString(name, def);
	}

	/** 设置一个sharedPreferences中的key值对 */
	public void setPrefByPackage( String name, String value) {
		String pkg = context.getPackageName();// 用包名当作文件名
		SharedPreferences prefs = context.getSharedPreferences(pkg, Context.MODE_PRIVATE);
		Editor ed = prefs.edit();
		ed.putString(name, value);
		ed.apply();
	}

	public boolean getBooleanPrefByPackage( String name, boolean def) {
		String pkg = context.getPackageName();// 用包名当作文件名
		SharedPreferences prefs = context.getSharedPreferences(pkg, Context.MODE_PRIVATE);
		return prefs.getBoolean(name, def);
	}

	public void setBooleanPrefByPackage( String name, boolean value) {
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
	public <T> boolean clear(Class<T> clazz){
		if(clazz != null && context != null){
			return context
			.getSharedPreferences(clazz.getSimpleName(),Context.MODE_PRIVATE)
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
	public boolean isEmpty(String str){
		if (str!=null&&!str.equals("")) {
			return false;
		}
		return true;
	}

	public void remove( String... keys) {
		if (context != null && keys != null && keys.length > 0) {
			Editor editor = context.getSharedPreferences(USER_CONFING, Context.MODE_PRIVATE).edit();

			for (String keyItem : keys) {
				editor.remove(keyItem);
			}

			editor.apply();
		}
	}

	private boolean notNull( String spFileName) {
		return !TextUtils.isEmpty(spFileName) && context != null;
	}

	private SharedPreferences getSharedPreferences( String spFileName) {
		return context.getSharedPreferences(spFileName, Context.MODE_PRIVATE);
	}

	@Override
	public void saveData(Object key, Object object) {
		super.saveData(key, object);

		if (object instanceof Integer){
			putInt(key.toString(),(int)object);
		}else if (object instanceof String){
			putStringByDefaultSP(key.toString(),object.toString());
		}else if (object instanceof Boolean){
			putBooleanByDefaultSP(key.toString(),(boolean) object);
		}
	}

	@Override
	public <T> T queryData(Object key, Class<T> clazz) {

		if (clazz == Integer.class){
			return (T)Integer.valueOf(getInt(key.toString(),0));
		}else if(clazz == String.class){
			return (T)getSharedPreferences(getStringByDefaultSP(key.toString(),""));
		}else if (clazz == Boolean.class){
			return (T)Boolean.valueOf(getBooleanByDefaultSP(key.toString(),false));
		}
		return null;
	}

	@Override
	public void updateData(Object key, Object object) {
		super.updateData(key, object);
		if (object instanceof Integer){
			putInt(key.toString(),(int)object);
		}else if (object instanceof String){
			putString(key.toString(),object.toString());
		}else if (object instanceof Boolean){
			putBooleanByDefaultSP(key.toString(),(boolean) object);
		}
	}

	@Override
	public <T> void deleteData(Object key, Class<T> clazz) {
		super.deleteData(key, clazz);
		clearString(key.toString());
	}
}
