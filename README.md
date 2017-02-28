# Pluto
[![License MIT](https://img.shields.io/badge/license-MIT-green.svg?style=flat)](https://raw.githubusercontent.com/minggo620/Pluto-Android/master/LICENSE)&nbsp;
[![Travis](https://img.shields.io/travis/rust-lang/rust.svg)]()
[![Github All Releases](https://img.shields.io/badge/download-1.4M Total-green.svg)](https://codeload.github.com/minggo620/Pluto-Android/zip/master)  

High integrated development framework for Android applications.<br/>
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
![Pluto架构图](http://upload-images.jianshu.io/upload_images/1252638-594c275b66a53e9b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

Usage
==============

###Initial Pluto 

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

###UIFramework
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

- **FinalBitmap Usage**

```
FinalBitmap finalBitmap = FinalBitmap.create(this); //this is Context type
finalBitmap.configLoadingImage(R.drawable.pluto_corner);
finalBitmap.configLoadfailImage(R.drawable.pluto_corner);
finalBitmap.display(imageView2,"http://m8en.com:8877/content/charmword_thumbnail.png");

```

###ServiceFramework
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
####①LogicManager Construction Method
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
