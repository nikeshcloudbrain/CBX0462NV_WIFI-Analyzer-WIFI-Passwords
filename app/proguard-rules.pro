# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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
############### ALL ###############

-dontwarn javax.**
-dontwarn java.lang.management.**
-dontwarn org.apache.log4j.**
-dontwarn org.apache.commons.logging.**
-dontwarn android.support.**
-dontwarn com.google.ads.**
-dontwarn org.slf4j.**
-dontwarn org.json.**
-dontwarn net.sqlcipher.**
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**
-dontwarn retrofit2.**
############### SOS ###############
# You can delete this section if you are not using SOS

-keep class com.shockwave.**
-keepclassmembers class com.shockwave.** { *; }
# Google Play Admob
    -keep public class com.google.android.gms.ads.** {
        public *;
    }

    -keep public class com.google.ads.** {
         public *;
    }
# Facebook
-keep class com.facebook.** {*;}
-dontwarn com.facebook.**

-keep class com.tapdaq.sdk.** { *; }
-keep class com.tapdaq.adapters.* { *; }
-keep class com.tapdaq.unityplugin.* { *; }
-keep class com.google.android.gms.ads.identifier.** { *; }



-dontwarn lombok.**

# Our components are initialized using reflection and can appear to be unused
-keepclassmembers class * implements com.salesforce.android.sos.component.Component

# ------------------ Eventbus Begin --------------------------
# The onEvent methods are called from the EventBus library and can appear unused.
-keepclassmembers class com.salesforce.android.sos.** {
    public void onEvent(...);
}
# ------------------ Eventbus End ----------------------------

# ------------------ Opentok Begin ---------------------------
# OpenTok cannot handle any code stripping for optimization.
-keep class com.opentok.** { *; }
-keep class org.webrtc.** { *; }
# ------------------ Opentok End -----------------------------

# ------------------ Gson Begin ------------------------------
# Preserve the special static methods that are required in all enumeration classes.
# We use these predominantly for serializing enums with Gson.
-keepclassmembers enum com.salesforce.android.sos.** {
    **[] $VALUES;
    public *;
}
# ------------------ Gson End --------------------------------

# ------------------ Gson ----------------------------
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

###### SALESFORCE MOBILE SDK ######
# You can delete this section if you are not using Knowledge or Cases

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# Required for user login to work on KitKat devices...
# If you don't include this, KitKat devices crash with an IllegalStateException at login!
-keepclassmembernames class com.salesforce.androidsdk.auth.SalesforceTLSSocketFactory { *; }

# For sqlcipher in Mobile SDK
-keep class net.sqlcipher.** { *; }

-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}


-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }

# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation

-keepattributes EnclosingMethod

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }



# Diable all application logs
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** w(...);
    public static *** v(...);
    public static *** i(...);
}