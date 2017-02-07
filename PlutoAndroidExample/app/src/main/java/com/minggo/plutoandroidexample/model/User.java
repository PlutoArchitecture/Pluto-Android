package com.minggo.plutoandroidexample.model;


import com.minggo.pluto.annotation.Primarykey;
import com.minggo.pluto.db.orm.Id_A;

/**
 * 用户模型
 * 
 * @author minggo
 * @time 2014-9-18下午1:15:29
 */
public class User {
	@Primarykey
	@Id_A
	public int userId;
	public String username;
	public String password;
	public String avatar;
	public String email;
	public String phone;
	public String imei;
	public String androidVS;
	public String mobileVS;
	public String sex;
	public String province;
	public int age;
	public int type;// 1:注册用户 2：系统用户
	public double latitude;
	public double longitude;
	public String address;
}
