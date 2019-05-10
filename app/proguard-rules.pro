# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\androidSDK/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#-------------------------------------------基本不用动区域--------------------------------------------

-ignorewarnings

#---------------------------------基本指令区----------------------------------
#指定压缩级别
-optimizationpasses 5
#不跳过非公共的库的类成员
-dontskipnonpubliclibraryclassmembers
-printmapping proguardMapping.txt
#混淆时采用的算法
-optimizations !code/simplification/cast,!field/*,!class/merging/*
#把混淆类中的方法名也混淆了
-useuniqueclassmembernames
#优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification
#将文件来源重命名为“SourceFile”字符串
-renamesourcefileattribute SourceFile
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
# 四大组件及基本类
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.support.multidex.MultiDexApplication
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.app.Fragment
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v4.** {*;}
-keep public class * extends android.support.annotation.**
-keep public class * extends android.support.v7.** {*;}
-keep public class android.app.Notification
-keep public class * extends android.app.Dialog
-keep class android.support.** {*;}
-keep public class javax.** {*;}
-keep public class android.webkit.** {*;}
-keep public class android.graphics.** {*;}
-dontwarn android.graphics.**

#保持自定义view不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#保持native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保持所有实现 Serializable 接口的类成员
-keep public class * implements java.io.Serializable {*;}

#保护实现接口Serializable的类中，指定规则的类成员不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <methods>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
    }

#所有枚举类型不要混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#保持R文件不被混淆，否则，你的反射是获取不到资源id的
-keep class **.R$* {*;}

#保持测试相关的代码
-dontnote junit.framework.**
-dontnote junit.runner.**
-dontwarn android.test.**
-dontwarn android.support.test.**
-dontwarn org.junit.**

#保持一些看不懂但会引发warnning的代码
-dontwarn org.kobjects.**
-dontwarn org.ksoap2.**
-dontwarn org.kxml2.**
-dontwarn org.xmlpull.v1.**
-keep class org.kobjects.** { *; }
-keep class org.ksoap2.** { *; }
-keep class org.kxml2.** { *; }
-keep class org.xmlpull.** { *; }

#---------------------------------webview------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}

#---------------------------------实体类---------------------------------
-keep public class **.*model*.** {*;}
-keep public class **.*enity*.** {*;}

#---------------------------------反射相关的类和方法-----------------------


#---------------------------------与js互相调用的类------------------------


#---------------------------------自定义View的类------------------------
-keep class com.zwq65.unity.ui._custom.view.**  {*;}

#---------------------------------第三方包-------------------------------

######################## dagger混淆 ########################
-dontwarn dagger.**

######################## okhttp3混淆 ########################
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-keep class org.apache.http.**
-keep interface org.apache.http.**
-dontwarn org.apache.**

######################## bugly混淆 ########################
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

######################## retrofit混淆 ########################
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-dontwarn javax.annotation.**
-keepattributes Signature
-keepattributes Exceptions

######################## ButterKnife混淆 ########################
# Retain generated class which implement Unbinder.
-keep public class * implements butterknife.Unbinder { public <init>(**, android.view.View); }
# Prevent obfuscation of types which use ButterKnife annotations since the simple name
# is used to reflectively look up the generated ViewBinding.
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }

######################## gson混淆 ########################
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature
# For using GSON @Expose annotation
-keepattributes *Annotation*
# Gson specific classes
-dontwarn sun.misc.**
-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }
# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

######################## glide混淆 ########################
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
# for DexGuard only
-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

######################## greenDao混淆 ########################
-keep class de.greenrobot.dao.** {*;}
-keep class org.greenrobot.greendao.** {*;}
-dontwarn org.greenrobot.greendao.**
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static Java.lang.String TABLENAME;
}

######################## Rxjava RxAndroid混淆 ########################
-keep class rx.** {*;}
-dontwarn rx.**
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

######################## Retrolambda混淆 ########################
-dontwarn java.lang.invoke.*
-dontwarn **$$Lambda$*

######################## barlibrary混淆 ########################
-keep class com.gyf.barlibrary.** {*;}
-dontwarn  com.gyf.barlibrary.**

######################## logger混淆 ########################
-keep class com.orhanobut.logger.** {*;}
-dontwarn  com.orhanobut.logger.**

######################## mkloader混淆 ########################
-keep class com.tuyenmonkey.mkloader.** {*;}
-dontwarn  com.tuyenmonkey.mkloader.**

########################slf4j混淆 ########################
-keep class ch.qos.** { *; }
-keep class org.slf4j.** { *; }
-keepattributes *Annotation*