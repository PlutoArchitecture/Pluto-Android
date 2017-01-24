package com.minggo.pluto.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 接口返回通用结果实体类
 * 
 * @author minggo
 * @time 2014-12-2下午1:50:13
 */
public class Result<T> implements Serializable {
	/**
	 * 是否成功
	 */
	public boolean success;
	/**
	 * 错误信息
	 */
	public String errorMsg; 
	/**
	 * 内容
	 */
	public T content;
	/**
	 * 返回码
	 */
	public int code;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
