package com.minggo.plutoandroidexample.model;


import com.minggo.pluto.annotation.Primarykey;
import com.minggo.pluto.db.orm.Id_A;

import java.io.Serializable;

/**
 * 用户模型
 * 
 * @author minggo
 * @time 2014-9-18下午1:15:29
 */
public class User implements Serializable {

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


	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getAndroidVS() {
		return androidVS;
	}

	public void setAndroidVS(String androidVS) {
		this.androidVS = androidVS;
	}

	public String getMobileVS() {
		return mobileVS;
	}

	public void setMobileVS(String mobileVS) {
		this.mobileVS = mobileVS;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
