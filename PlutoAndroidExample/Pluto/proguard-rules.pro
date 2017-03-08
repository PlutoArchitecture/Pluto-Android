# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/mmd_mac03/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#用到反射及注解需要加上下面
-keepattributes *Annotation*,EnclosingMethod
#日志显示代码行数
-keepattributes SourceFile,LineNumberTable
#使代码不被压缩优化（点击事件的控件，被优化掉的原因就是因为用注解后，在代码中该控件的实例没有其他地方用到，所以程序就认为该代码没有被用到，就被注视掉了。）
#用到afinal注解的项目代码混淆后某些控件view点击事件无效 - 七月流浪猫 - 博客频道 - CSDN.NET
#http://blog.csdn.net/haibin80s/article/details/46965645
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-keepattributes Exceptions,InnerClasses,Signature

#不混淆Serializable的子类
# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Keep Butterknife stuff
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

# Keep GSON stuff
-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }

#排除指定Pluto的LogicParam混淆
-keep class com.minggo.pluto.logic.LogicParam { *; }
-keep class com.minggo.pluto.logic.LogicParam$* { *; }
-keep class com.minggo.pluto.logic.model.** { *; }


#WebView
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
#apache工具类mmons.** { *; }
-dontwarn org.apache.commons.**
#onClickXXX不混淆，防止FinalActivity无法调用
-keepclassmembers class * {
    public void *(android.view.View);
    public void *(android.widget.AdapterView,android.view.View,int,long);
    public boolean *(android.widget.AdapterView,android.view.View,int,long);
}
