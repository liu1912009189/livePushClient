# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/pym/workplace/tools/adt-bundle-mac-x86_64-20140702/sdk/tools/proguard/proguard-android.txt
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
-ignorewarnings
#指定代码的压缩级别
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
 #优化  不优化输入的类文件
-dontoptimize
 #预校验
-dontpreverify
 #混淆时是否记录日志
-verbose
 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#保护注解
-keepattributes *Annotation*

# 指定一个文本文件用来生成混淆后的名字。默认情况下，混淆后的名字一般为 a、b、c 这种。
# 通过使用配置的字典文件，可以使用一些非英文字符做为类名。成员变量名、方法名。字典文件中的空格，标点符号，重复的词，还有以'#'开头的行都会被忽略。
# 需要注意的是添加了字典并不会显著提高混淆的效果，只不过是更不利与人类的阅读。正常的编译器会自动处理他们，并且输出出来的jar包也可以轻易的换个字典再重新混淆一次。
# 最有用的做法一般是选择已经在类文件中存在的字符串做字典，这样可以稍微压缩包的体积。
# 查找了字典文件的格式：一行一个单词，空行忽略，重复忽略
-obfuscationdictionary proguard-dictionary.txt

# 指定一个混淆类名的字典，字典格式与 -obfuscationdictionary 相同
-classobfuscationdictionary proguard-dictionary.txt
# 指定一个混淆包名的字典，字典格式与 -obfuscationdictionary 相同
-packageobfuscationdictionary proguard-dictionary.txt

# 保持哪些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.ext.BroadcastReceiver
-keep public class * extends android.ext.ContentProvider

-keep class com.marswin89.marsdaemon.NativeDaemonBase{*;}
-keep class com.marswin89.marsdaemon.nativ.NativeDaemonAPI20{*;}
-keep class com.marswin89.marsdaemon.nativ.NativeDaemonAPI21{*;}
-keep class com.marswin89.marsdaemon.DaemonApplication{*;}
-keep class com.marswin89.marsdaemon.DaemonClient{*;}
-keepattributes Exceptions,InnerClasses,...
-keep class com.marswin89.marsdaemon.DaemonConfigurations{*;}
-keep class com.marswin89.marsdaemon.DaemonConfigurations$*{*;}
-keep class com.marswin89.marsdaemon.PackageUtils{*;}

-keep class com.water.richprocess.FunctionUtils{ *;}
-keep class com.water.richprocess.CLogUtil{*;}
-keep class com.water.richprocess.DataManager{*;}
-keep class com.water.richprocess.IDaemonService{*;}
-keep class com.water.richprocess.RichApplication{*;}

#小米混淆开始
-keep class cn.richinfo.richpush.receiver.XiaoMiMessageReceiver {*;}
#可以防止一个误报的 warning 导致无法成功编译，如果编译使用的 Android 版本是 23。
-dontwarn com.xiaomi.push.**
#小米混淆结束


#华为推送开始
#-keep class com.huawei.hms.**{*;}
#-dontwarn com.huawei.android.hms.**
#-keep class com.huawei.android.pushagent.**{*;}
#-dontwarn com.huawei.android.pushagent.**
#-keep class com.huawei.android.pushselfshow.**{*;}
#-dontwarn com.huawei.android.pushselfshow.**
#-keep class com.huawei.android.microkernel.**{*;}
#-dontwarn com.huawei.android.microkernel.**
#-keep class cn.richinfo.richpush.receiver.HuaweiPushRevicer{*;}
-ignorewarning
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.hianalytics.android.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}

-keep class com.huawei.gamebox.plugin.gameservice.**{*;}

-keep public class com.huawei.android.hms.agent.** extends android.app.Activity { public *; protected *; }
-keep interface com.huawei.android.hms.agent.common.INoProguard {*;}
-keep class * extends com.huawei.android.hms.agent.common.INoProguard {*;}
#华为推送结束

#vivo推送开始
-dontwarn com.vivo.push.**
-keep class com.vivo.push.**{*;}
-keep class cn.richinfo.richpush.receiver.VivoPushReceiver{*;}
#vivo推送结束

#oppo推送开始
-dontwarn com.coloros.mcssdk.**
-keep class com.coloros.mcssdk.**{*;}
-keep class com.heytap.**{*;}
-keep class cn.richinfo.richpush.receiver.OppoMessageService{*;}
#oppo推送结束

#暴露接口混淆开始
-keep class cn.richinfo.richpush.RichPushApi{*;}
-keep public abstract interface cn.richinfo.richpush.RichPushListener{ *;}
-keep public abstract interface cn.richinfo.richpush.HttpCallBack{ *;}
-keep class cn.richinfo.richpush.Constant{*;}
-keep class cn.richinfo.richpush.ClickResultActivity{*;}
-keep class cn.richinfo.richpush.contentprovider.ContentProviderHelp{*;}
#暴露接口混淆结束

#测试用的混淆，正式发布可以删除
-keep class com.water.richprocess.CLogUtil{*;}
-keep class cn.richinfo.richpush.model.MsgEvent{*;}
-keep class cn.richinfo.richpush.model.PushWayEvent{*;}
-keep class cn.richinfo.richpush.model.TestString{*;}
-keep class cn.richinfo.richpush.model.**{*;}
-keep class cn.richinfo.richpush.contentprovider.ContentProviderHelp{*;}

#umeng
-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**
-dontwarn org.apache.thrift.**

-keepattributes *Annotation*

-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class org.apache.thrift.** {*;}
-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}

#umeng推送混淆开始
-keep class com.umeng.commonsdk.** {*;}
-keep public class com.umeng.message.example.example.R$*{
   public static final int *;
}
#umeng推送混淆结束


#避免log打印输出
 -assumenosideeffects class android.util.Log {
      public static *** v(...);
      public static *** d(...);
      public static *** i(...);
      public static *** w(...);
 }

 -keepattributes *Annotation*
 -keepclassmembers class ** {
     @de.greenrobot.event.Subscribe <methods>;
 }
 -keep enum de.greenrobot.event.ThreadMode { *; }
 # Only required if you use AsyncExecutor
 -keepclassmembers class * extends de.greenrobot.event.util.ThrowableFailureEvent {
     <init>(Java.lang.Throwable);
 }

 -keepclassmembers class ** {
     public void onEvent*(**);
 }

-keep class cn.richinfo.richpush.model.AppEntity{*;}
-keep class cn.richinfo.richpush.model.MsgEvent{*;}

-keep class cn.richinfo.richpush.receiver.UmengRevicer{*;}