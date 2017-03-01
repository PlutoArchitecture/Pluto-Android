package com.minggo.pluto.db.orm;

import com.minggo.pluto.annotation.AutoIncrement;
import com.minggo.pluto.annotation.Exclude;
import com.minggo.pluto.annotation.Primarykey;

import java.lang.reflect.Field;
import java.util.Date;


/**
 * SQL语句拼装
 * 
 * @author minggo
 * @time 2014-6-23 S下午9:43:33
 */
public class SQLHelper {

	/**
	 * 获取创建表的SQL语句
	 * 
	 * @param tableName
	 * @param clazz
	 *            要保存的实体
	 * @return
	 */
	public static final String getCreateTable(String tableName, Class<?> clazz) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS ");
		sb.append(tableName);
		sb.append("(");
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			// 排除字段
			if (field.getAnnotation(Exclude.class) != null) {
				continue;
			}

			if (field.getAnnotation(Primarykey.class) != null) { // 主键字段
				
				if (field.getAnnotation(AutoIncrement.class) == null) {
					sb.append(field.getName() + getTypeText(field) + "PRIMARY KEY NOT NULL,");
				}else{
					System.out.println("设置了数据库递增字段");
					sb.append(field.getName() + getTypeText(field) + "PRIMARY KEY autoincrement NOT NULL ,");
				}
			} else {
				if (field.getAnnotation(AutoIncrement.class) == null) {
					sb.append(field.getName() + getTypeText(field) + ",");
				}else{
					sb.append(field.getName() + getTypeText(field) + "auto_increment,");
				}
			}
		}

		// 处理掉最后一个逗号
		int indexOf = sb.lastIndexOf(",");
		if (indexOf != -1) {
			sb.deleteCharAt(indexOf);
		}

		sb.append(")");

		return sb.toString();
	}

	/**
	 * 获得匹配的数据类型
	 * 
	 * @param field
	 * @return
	 */
	private static final String getTypeText(Field field) {
		Class<?> type = field.getType();
		field.setAccessible(true);
		if (type.equals(int.class) || type.equals(Integer.class)) {
			return " INTEGER ";
		} else if (type.equals(long.class) || type.equals(Long.class)) {
			return " LONG ";
		} else if (type.equals(double.class) || type.equals(Double.class)) {
			return " Double ";
		} else if (type.equals(Date.class)) {
			return " LONG ";
		} else { // 默认text类型
			return " TEXT ";
		}
	}

	/**
	 * 获取删除表的SQL
	 * 
	 * @return
	 */
	public static String getDrapTable(String tableName) {
		return "DROP TABLE IF EXISTS " + tableName;
	}

}
