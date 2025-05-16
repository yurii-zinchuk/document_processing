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

# Coroutines keep rules
-keepclassmembers class kotlinx.coroutines.** {
    *;
}
-keepclassmembers class kotlin.coroutines.** {
    *;
}
-keep class kotlinx.coroutines.** { *; }
-keep class kotlin.coroutines.** { *; }

# Prevent stripping coroutine internals
-dontwarn kotlin.coroutines.**

# Retrofit & Gson
-keepattributes Signature
-keepattributes *Annotation*

# Retrofit annotations
-keep class retrofit2.Call
-keep class retrofit2.** { *; }

# Prevent Gson from stripping model info
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**

# Required for Retrofit to preserve generics
-keep class ** implements java.lang.reflect.Type
