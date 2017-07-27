# Pluto
[![License MIT](https://img.shields.io/badge/license-MIT-green.svg?style=flat)](https://raw.githubusercontent.com/minggo620/Pluto-Android/master/LICENSE)&nbsp;
[![Travis](https://img.shields.io/badge/build-passing-brightgreen.svg?maxAge=2592000)]()
[![Github Releases](https://img.shields.io/badge/release-1.2.5-blue.svg?maxAge=2592000)](https://bintray.com/minggoopen/pluto)
[![Github All Releases](https://img.shields.io/badge/download-1.0M-green.svg)](https://codeload.github.com/minggo620/Pluto-Android/zip/master)  

High integrated development framework for Android applications.

### 更多交流请加微信公众号：minggo_dev
![微信公众号：minggo_dev](http://upload-images.jianshu.io/upload_images/1252638-7a68bcc2b2ec9939.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

Features
==============
- **Cache Data High Integrated**:The Data operation(Sqlite, SharePreference, File) is integrated as a DataFramework, and provide a DataManagerProxy to operating cache data .
- **Network Data and Cache Data High Integrated**: The Data from network add the data from DataFramework are integrated in PlutoApiEngine.
- **Service High Integrated**: The special business logic and PlutoApiEngine are integrated as a LogicManager, and put the logic in a CommonAsyncTask process.
- **UI Framework High Integrated**: There is no need to add many component of Android, just extends PlutoActivity, It contains Toast, PlutoDialog, SoftInputManager, Handler and so on.More, through using LogicManager chain programming, it can easier and faster finish data ,logic and UI refresh work. 
- **Flexible Utils**: There are many kinds of common tools being provided for developer to deal many kind of trivial work，such as clipping Bitmap, formate Date, defined Log and so on.
- **Global Exception Handle** PlutoException can handle app global Exception if they are happening.

Architecture diagram
================
![Pluto架构图](http://upload-images.jianshu.io/upload_images/1252638-ad8a8f4aa61f8005.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

Usage
==============

### Initial Pluto 
In app Bulid.gradle file dependencies add under sentence

`compile 'com.minggo:Pluto:1.2.5'`
```
public class MyApplication extends Application {
	 @Override
	 public void onCreate() {
	 	 super.onCreate();
	 	 bindPluto();
	 }
	 private void bindPluto(){
	 		Pluto.initPluto(this);
	 		//option setting initial
	 		Pluto.APP_CACHE_FILE = "com.pluto.example";
	 		Pluto.LOG_SHOW = true;
	 		Pluto.URL_DOMAIN = "https://m8en.com";
	 		DBConfig.NAME = "com.pluto.example.db";
	 		DBConfig.VERSION = 1;
	 }
}
```

Pluto has to be inited with `initPluto(Context context)` method, and it's better to be inited in child of Application class.

### UIFramework
- **PlutoActivity Usage**

```
public class PlutoActivityExample extends PlutoActivity implements OnClickListener{
     ...
     loadingDialog.show();
     
     showSoftInput();
     
     isNetworkConnected();
     
     mUiHandler.sendEmptyMessage(10000);
     
     cancelAsyncTask(calculator); 
     
     showHomeAsUp();
     
     
     @Override
     public void handleUiMessage(Message msg) {
       super.handleUiMessage(msg);
     }
  
     ...
}
```
More detail you can find in demo project.

- **PlutoFragment Usage**

The Usage is the same to PlutoActivity, not repeat to declaring.

- **PlutoDialog Usage**

```
public class PlutoDialogExample extends PlutoActivity implements OnClickListener,PlutoDialogListener {
 	@Override
   protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_dialog_example);
    		
 		new PlutoDialog(this, PlutoDialog.DEFAULT_EXIT, this).show();
 
 		new PlutoDialog(this,PlutoDialog.DEFAULT,"Title","Dialog show message","left button","right button",this).show();
 
 		new PlutoDialog(this,PlutoDialog.LOADING).show();
 
 		new PlutoDialog(this, PlutoDialog.TEXT_ONLIY, "Text Only").show();
 	}
}

```
PlutoDialog has some defined constant, which are match difference dialog, such `PlutoDialog.DEFAULT_EXIT` is defined exit common dialog.More detail you can see them in demo.

- **Glide Usage**

```
Glide.with(this).load("http://m8en.com:8877/content/logo_battery_notification.png").placeholder(R.drawable.pluto_corner).into(imageView1);
Glide.with(this).load("http://m8en.com:8877/content/charmword_thumbnail.png").placeholder(R.drawable.pluto_corner).into(imageView2);
Glide.with(this).load("http://m8en.com:8877/content/logo_2048_thumbnail.png").placeholder(R.drawable.pluto_corner).into(imageView3);
```

### ServiceFramework
- **LogicManager Usage**

Finally, all logic operation will be integrated in a sentence of chain programming 

```
...
@Override
public void onClick(View view) {
		switch (view.getId()){
			case R.id.bt_data_1:
				new LogicManager(mUiHandler,User.class,GET__MODEL__ONLY_NETWORK)
				.setParamClass(LoginParam.class)
				.setParam(ParamName.PASSWORD,123456)
				.setParam(ParamName.EMAIL,"minggo8en@gmail.com")
				.setParam(ApiUrl.PASS, pass)
				.setArg1(1)
				.execute();
		   break;
		   case R.id.bt_data_1:
				new LogicManager(mUiHandler,ServerURL.class,GET__LIST__CACHE_EXPIRED_AND_NETWORK_RETURN)
				.setParamClass(ServerUrlParam.class)
				.setCacheKey(ServerUrlParam.CACHEKEY)
				.setLimitedTime(1)
				.setParam(ApiUrl.PASS, pass)
				.execute();
			break;
	}
	...
```
#### ①LogicManager Construction Method
It' most import to use construction method, `public <T> LogicManager(Handler handler, Class<T> clazz, LogicManagerType logicManagerType){}`, the param `Class<T> clazz` is PlutoApiEngine deal data and return this data of type, `LogicManagerType` is an enum that has 12 kind of network and cache logic.

```
	public enum LogicManagerType{
		GET__MODEL__CACHE_ADVANCE_AND_NETWORK_RETURN,
		GET__MODEL__CACHE_EXPIRED_AND_NETWORK_RETURN,
		GET__MODEL__ONLY_NETWORK,
		
		POST__MODEL__CACHE_ADVANCE_AND_NETWORK_RETURN,
		POST__MODEL__CACHE_EXPIRED_AND_NETWORK_RETURN,
		POST__MODEL__ONLY_NETWORK,
		
		GET__LIST__CACHE_ADVANCE_AND_NETWORK_RETURN,
		GET__LIST__CACHE_EXPIRED_AND_NETWORK_RETURN,
		GET__LIST__ONLY_NETWORK,
		
		POST__LIST__CACHE_ADVANCE_AND_NETWORK_RETURN,
		POST__LIST__CACHE_EXPIRED_AND_NETWORK_RETURN,
		POST__LIST__ONLY_NETWORK;
	}
```
#### ②LogicParam Define
for example，`LoginParam.class`
```
public final class LoginParam{
	public static final int WHAT = 10000;
	public static final String URL = DOMAIN+"charmword/loginUser.action";
	public static final String CACHEKEY = "user_info";
}
```
Defined static final constant, and LogicManager can reflect them.

- **PlutoException Handle Usage**

PlutoException is running when Pluto would be inited default. in Pluto.class
```
public static void initPluto(Context context) {
	if (!BuildConfig.DEBUG) {
		PlutoException.getAppExceptionHandler(context);
	}
}
```
Also, you can define throw PlutoException for special dealing.
 	
    try{
    	...
    } catch (IOException e) {
    	...
    	 throw PlutoException.network(e);
    }

- **Flexible Util Usage**

In package `com.minggo.pluto.util`, you can find the varies of integrated tools.
AnimationUtils.java
BitmapUtil.java
DateUtil.java
DisplayUtil.java
EncryptUtils.java
FastBlur.java
FileUtils.java
LogUtils.java
NetworkUtils.java
PhotoUtil.java
PollingUtils.java
SharePreferenceUtils.java
StringUtils.java
ThreadPoolUtils.java

### Extention Declare
- **DataFramework extention**
When you need to extend dealing data with some extro mothod. you can find the class DataManagerProxy then add difference method in it.Here not to introduce how to use DataManagerProxy.

- **ServiceFramework extention**
when you need to extend dealing logic with data, you can find the class PlutoApiEngine and then add method you defined in it.Finally in LogicManager class afford method matching PlutoApiEngine's logic.

- **NetworkFramework extention**
Sometimes, you need to extend ApiClient class to fix `Result<Map>` or other need, you can find out class ApiClient to adding your method.
 

中文介绍
======
高度整合Android应用开发框架

特性
======
- **本地缓存数据高度整合**:数据擦做如(Sqlite, SharePreference, File) 操作被整合成DataFramework, 并且提供 DataManagerProxy 来代理处理数据操作。
- **网络和本地数据高度整合**: 网络数据和本地数据统一整合到一个数据引擎PlutoApiEngine中，处理何时使用何种缓存数据。
- **业务逻辑高度整合**: 那些特定通用的业务逻辑与数据引擎PlutoApiEngine整合到逻辑层LogicManager，并将每个逻辑放在CommonAsyncTask线程中处理。
- **UI框架高度整合**: 只要继承PlutoActivity就不需要考虑在每个Activity或者其他页面组件上都添累赘组件。PlutoActivity包含 Toast, PlutoDialog, SoftInputManager, Handler 等等， on.更重要的是,通过 LogicManager 链式方法调用,这样更容易更快捷完成处理数据、逻辑和UI刷新工作。
- **丰富的工具类**: Pluto提供很平时开发常用的工具类来出各种琐碎的业务。比如剪裁Bitmap, 格式化 Date, 自定义Log等等.
- **全局Exception处理** PlutoException监控和处理整个应用Exception。

用法
==============

### 初始化Pluto 

在app Module Build.gradle中的dependencies加上一下这句

`compile 'com.minggo:Pluto:1.2.5'`

```
public class MyApplication extends Application {
	 @Override
	 public void onCreate() {
	 	 super.onCreate();
	 	 bindPluto();
	 }
	 
	 private void bindPluto(){
	 		Pluto.initPluto(this);
	 		//以下可选
	 		Pluto.APP_CACHE_FILE = "com.pluto.example";
	 		Pluto.LOG_SHOW = true;
	 		Pluto.URL_DOMAIN = "https://m8en.com";
	 		DBConfig.NAME = "com.pluto.example.db";
	 		DBConfig.VERSION = 1;
	 }
}
```

Pluto 必须调用`initPluto(Context context)`来初始化, 最好在自定义的Application中初始化。
### UIFramework
- **PlutoActivity使用**

```
public class PlutoActivityExample extends PlutoActivity implements OnClickListener{
     ...
     loadingDialog.show();//默认带有PlutoDialog中的LOADING样式
     
     showSoftInput();//显示输入法
     
     isNetworkConnected();//判断网络
     
     mUiHandler.sendEmptyMessage(10000);//默认weak类型Handler发送Message
     
     cancelAsyncTask(calculator); //观察者cancel当前CommonAsyncTask或者LogicManager
     
     showHomeAsUp();//ActionBar显示返回图标
     
     //重写PlutoActivity中handleUiMessage处理Message
     @Override
     public void handleUiMessage(Message msg) {
       super.handleUiMessage(msg);
     }
  
     ...
}
```
更详细更多用法见Demo.

- **PlutoFragment 用法**

用法跟PlutoActivity用法一样，不在重复.

- **PlutoDialog 用法**

```
public class PlutoDialogExample extends PlutoActivity implements OnClickListener,PlutoDialogListener {
 	@Override
   protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_dialog_example);
    		
    	//系统默认Dialog样式，只提供提示信息设置，this指的是点击按钮的回调接口实现类
 		new PlutoDialog(this, PlutoDialog.DEFAULT_EXIT, this).show();
 		
 		//系统默认Dialog样式，this指的是点击按钮的回调接口实现类
 		new PlutoDialog(this,PlutoDialog.DEFAULT,"Title","Dialog show message","left button","right button",this).show();
 		
 		//Loading提示对话框
 		new PlutoDialog(this,PlutoDialog.LOADING).show();
 		
 		//TEXT_ONLIY只展示text，没有任何按钮其他的控件
 		new PlutoDialog(this, PlutoDialog.TEXT_ONLIY, "Text Only").show();
 	}
}

```
PlutoDialog 有几种常量对应不同Dialog样式，更多可以参考Demo项目，同时可以扩展样式.

- **Glide 用法**

```
Glide.with(this).load("http://m8en.com:8877/content/logo_battery_notification.png").placeholder(R.drawable.pluto_corner).into(imageView1);
Glide.with(this).load("http://m8en.com:8877/content/charmword_thumbnail.png").placeholder(R.drawable.pluto_corner).into(imageView2);
Glide.with(this).load("http://m8en.com:8877/content/logo_2048_thumbnail.png").placeholder(R.drawable.pluto_corner).into(imageView3);
```

### ServiceFramework
- **LogicManager 用法**

最终暴露出来的操作就是一句链式的方法调用。

```
...
@Override
public void onClick(View view) {
		switch (view.getId()){
			case R.id.bt_data_1:
				new LogicManager(mUiHandler,User.class,GET__MODEL__ONLY_NETWORK)
				.setParamClass(LoginParam.class)
				.setParam(ParamName.PASSWORD,123456)
				.setParam(ParamName.EMAIL,"minggo8en@gmail.com")
				.setParam(ApiUrl.PASS, pass)
				.setArg1(1)
				.execute();
		   break;
		   case R.id.bt_data_1:
				new LogicManager(mUiHandler,ServerURL.class,GET__LIST__CACHE_EXPIRED_AND_NETWORK_RETURN)
				.setParamClass(ServerUrlParam.class)
				.setCacheKey(ServerUrlParam.CACHEKEY)
				.setLimitedTime(1)
				.setParam(ApiUrl.PASS, pass)
				.execute();
			break;
	}
	...
```
#### ①LogicManager构造方法说明
构造方法 `public <T> LogicManager(Handler handler, Class<T> clazz, LogicManagerType logicManagerType){}`中的`Class<T> clazz` 参数是PlutoApiEngine处理数据后返回的指定数据类型, `LogicManagerType` 是一个有12种类型的枚举.每个枚举根据表意理解就行，枚举如下

```
	public enum LogicManagerType{
		GET__MODEL__CACHE_ADVANCE_AND_NETWORK_RETURN,
		GET__MODEL__CACHE_EXPIRED_AND_NETWORK_RETURN,
		GET__MODEL__ONLY_NETWORK,
		
		POST__MODEL__CACHE_ADVANCE_AND_NETWORK_RETURN,
		POST__MODEL__CACHE_EXPIRED_AND_NETWORK_RETURN,
		POST__MODEL__ONLY_NETWORK,
		
		GET__LIST__CACHE_ADVANCE_AND_NETWORK_RETURN,
		GET__LIST__CACHE_EXPIRED_AND_NETWORK_RETURN,
		GET__LIST__ONLY_NETWORK,
		
		POST__LIST__CACHE_ADVANCE_AND_NETWORK_RETURN,
		POST__LIST__CACHE_EXPIRED_AND_NETWORK_RETURN,
		POST__LIST__ONLY_NETWORK;
	}
```
#### ②LogicParam定义
集成LogicParam比如，`LoginParam.class`
```
public final class LoginParam{
	public static final int WHAT = 10000;
	public static final String URL = DOMAIN+"charmword/loginUser.action";
	public static final String CACHEKEY = "user_info";
}
```
这个参数类定义一些常量统一管理，提供LogicManager来反射获取。

- **PlutoException处理用法**

默认情况下，当Pluto被初始化的时候PlutoException就开始捕捉.启动代码在Pluto.class中。
```
public static void initPluto(Context context) {
	if (!BuildConfig.DEBUG) {
		PlutoException.getAppExceptionHandler(context);
	}
}
```
而且，可以根据自己的情况捕捉错误抛给PlutoException处理.
 	
    try{
    	...
    } catch (IOException e) {
    	...
    	 throw PlutoException.network(e);
    }

- **丰富Util使用**

在`com.minggo.pluto.util`包中, 可以找到一下这些常用的工具类.
AnimationUtils.java
BitmapUtil.java
DateUtil.java
DisplayUtil.java
EncryptUtils.java
FastBlur.java
FileUtils.java
LogUtils.java
NetworkUtils.java
PhotoUtil.java
PollingUtils.java
SharePreferenceUtils.java
StringUtils.java
ThreadPoolUtils.java

### 扩展声明
- **DataFramework扩展**
数据框架层级如需扩展自己的方法可以直接改DataManagerProxy或者集成它，增加对应的数据库，文件，xml存储处理。这里就不介绍如何用DataManagerProxy，如需了解请查看源码.

- **ServiceFramework扩展**
业务逻辑层级如需扩展自己业务方法，首先考虑是否要读数据获取存储逻辑进行修改，如需修改就在PlutoApiEngine增加相应的方法，然后再在LogicManager类中增加对接PlutoApiEngine里边新的方法。 

- **NetworkFramework扩展**
网络数据层级如需调整，比如增加`Result<Map>` 返回的类型值或者其他网络业务，可以找到ApiClient类进行扩展即可。

