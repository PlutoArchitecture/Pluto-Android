package com.minggo.pluto.db.orm;

import android.content.ContentValues;
import android.database.Cursor;

import com.minggo.pluto.annotation.AutoIncrement;
import com.minggo.pluto.annotation.Exclude;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;

/**
 * 持久化工具类(有Exclude注解的字段排除在外)
 * @author minggo
 * @time 2014-6-23 S下午9:32:28
 */
public final class DaoUtils {

	/**
	 * 将对象转为ContentValues 支持【int,long,double,String,date】
	 * 
	 * @param <T>
	 * @param t
	 * @return
	 */
	public static final <T> ContentValues object2ContentValues(T t) {
		ContentValues values = new ContentValues();
		try {
			Class<T> clazz = (Class<T>) t.getClass();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				// 将有Exclude注解的字段排除在外
				if (field.getAnnotation(Exclude.class) != null) {
					continue;
				}
				Class<?> type = field.getType();
				if (type.equals(int.class) || type.equals(Integer.class)) {
					values.put(field.getName(), field.getInt(t));
				} else if (type.equals(String.class)) {
					values.put(field.getName(), String.valueOf(field.get(t)));
				} else if (type.equals(long.class) || type.equals(Long.class)) {
					values.put(field.getName(), field.getLong(t));
				} else if (type.equals(double.class)
						|| type.equals(Double.class)) {
					values.put(field.getName(), field.getDouble(t));
				} else if (type.equals(Date.class)) {
					Date date = (Date) field.get(t);
					if(date != null){
						values.put(field.getName(),date.getTime());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return values;
	}
	/**
	 * 将对象转为ContentValues 支持【int,long,double,String,date】除了自增字段
	 * @param t
	 * @return
	 */
	public static final <T> ContentValues object2ContentValuesWithoutIncrement(T t) {
		ContentValues values = new ContentValues();
		try {
			Class<T> clazz = (Class<T>) t.getClass();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				// 将有Exclude注解的字段排除在外
				if (field.getAnnotation(Exclude.class) != null||field.getAnnotation(AutoIncrement.class)!=null) {
					continue;
				}
				Class<?> type = field.getType();
				if (type.equals(int.class) || type.equals(Integer.class)) {
					values.put(field.getName(), field.getInt(t));
				} else if (type.equals(String.class)) {
					values.put(field.getName(), String.valueOf(field.get(t)));
				} else if (type.equals(long.class) || type.equals(Long.class)) {
					values.put(field.getName(), field.getLong(t));
				} else if (type.equals(double.class)
						|| type.equals(Double.class)) {
					values.put(field.getName(), field.getDouble(t));
				} else if (type.equals(Date.class)) {
					Date date = (Date) field.get(t);
					if(date != null){
						values.put(field.getName(),date.getTime());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return values;
	}

	/**
	 * 将游标转为对象集合 支持【int,long,double,String,date】
	 * 
	 * @param <T>
	 * @param cursor
	 * @param clazz
	 * @return
	 */
	public static final <T> ArrayList<T> cursor2ObjectList(Cursor cursor,
			Class<T> clazz) {
		ArrayList<T> list = new ArrayList<T>();
		try {
			if (clazz != null && cursor != null && !cursor.isClosed()) {
				String[] columnNames = cursor.getColumnNames();
				int length = columnNames.length;
				Field[] fields = clazz.getDeclaredFields();
				while (cursor.moveToNext()) {
					T instance = clazz.newInstance();
					for (int index = 0; index < length; index++) {
						String columnName = columnNames[index];
						for (Field field : fields) {
							field.setAccessible(true);
							// 排除Exclude注解的字段
							if (field.getAnnotation(Exclude.class) != null) {
								continue;
							}
							if (field.getName().equals(columnName)) {
								setField(cursor, index, instance, field);
							}
						}

					}
					list.add(instance);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	
	private static final <T> void setField(Cursor cursor, int columnIndex,
			T instance, Field field) throws IllegalArgumentException,
			IllegalAccessException {
		Class<?> type = field.getType();
		field.setAccessible(true);
		
		int index = cursor.getColumnIndex(field.getName());
		
		if (type.equals(int.class) || type.equals(Integer.class)) {
			field.setInt(instance, cursor.getInt(index));
		} else if (type.equals(String.class)) {
			field.set(instance, cursor.getString(index));
		} else if (type.equals(long.class) || type.equals(Long.class)) {
			field.setLong(instance, cursor.getLong(index));
		} else if (type.equals(double.class) || type.equals(Double.class)) {
			field.setDouble(instance, cursor.getDouble(index));
		}else if (type.equals(Date.class)) {
			long datetime = cursor.getLong(index);
			if(datetime>0){
				field.set(instance, new Date(datetime));
			}
		}
	}

}
