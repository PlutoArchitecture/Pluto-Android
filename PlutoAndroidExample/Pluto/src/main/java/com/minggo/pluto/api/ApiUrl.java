package com.minggo.pluto.api;

/**
 * 接口URL
 */
public class ApiUrl {

	//region 默认参数，每个请求都会有
	public final static String UID = "uid";//GG号（加密后)
	public final static String MID = "mid"; // 手机唯一识别码
	public final static String PID = "pid"; // 整型 手机渠道号id,用来区分推荐的渠道，如360, 百度
	public final static String IMEI = "imei"; //手机IMEI号
	public final static String M_VER = "m_ver"; //手机版本号
	public final static String S_VER = "s_ver";//手机系统版本
	public final static String IMEI_TIME = "imeiTime";//app安装后首次打开时间

	public final static String PASS = "pass"; // 参数加密结果
	public final static String VERSIONNAME = "versionName"; // 当前版本名称
	public final static String VERSIONCODE = "versionCode"; // 当前版本id
	public final static String TIMESTAMP = "timestamp"; // 请求时间戳
	//endregion

	//region 常用参数
	public final static String PN = "pn";//页码
	public final static String PS = "ps";//每页条数
	public final static int PN_V = 1;//页码
	public final static int PS_V = 10;//每页条数
	public final static String STATUS = "status";//状态
	public final static String TYPE_ID = "type_id";//类型
	public final static String ENCRYPT_ID = "encryptId";//加密用户Id
	public static final String ID = "id";//唯一标示
	public static final String BOOK_ID = "bookId";//书Id
	public static final String MENU_ID = "menuId";//章节Id
	/** 设备唯一识别码 */
	public static final String UUID = "UUID";
	//endregion

	public static String URL_DOMAIN = "";//正式服务器
	public static String MD5KEY = ""; // 加密key

}
