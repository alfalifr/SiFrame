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



#-dontwarn kotlin.reflect.jvm.internal.**
-keep,allowoptimization class kotlin.reflect.jvm.internal.** { *; }
#,allowobfuscation
#,allowoptimization
#,allowshrinking

#-keep class sidev.lib.android.siframe.arch.** { *; }
#-keep class sidev.lib.universal.fun.** { *; }
#-keep class sidev.lib.universal.tool.util.** { *; }
#-keep class sidev.lib.android.siframe.intfc.lifecycle.** { *; }

-keepclassmembers,allowoptimization,allowobfuscation class * extends sidev.lib.android.siframe.arch.viewmodel.ObsFiewModel {
    public protected <init>(...);
}
-keepclassmembers,allowoptimization,allowshrinking class * extends sidev.lib.android.siframe.arch.intent_state.ViewIntent {
#    public protected java.lang.String isResultTemporary;
#    public protected private java.lang.String isResultTemporary;
    *;
}
-keepclassmembers,allowoptimization,allowshrinking class * extends sidev.lib.android.siframe.arch.intent_state.ViewState { *; }

# Add *one* of the following rules to your Proguard configuration file.
# Alternatively, you can annotate classes and class members with @androidx.annotation.Keep

# keep the class and specified members from being removed or renamed
#-keep class sidev.lib.universal.tool.util.ReflexUtil { java.lang.Object newInstance(java.lang.String); }

# keep the specified class members from being removed or renamed
# only if the class is preserved
#-keepclassmembers class sidev.lib.universal.tool.util.ReflexUtil { java.lang.Object newInstance(java.lang.String); }

# keep the class and specified members from being renamed only
#-keepnames class sidev.lib.universal.tool.util.ReflexUtil { java.lang.Object newInstance(java.lang.String); }

# keep the specified class members from being renamed only
#-keepclassmembernames class sidev.lib.universal.tool.util.ReflexUtil { java.lang.Object newInstance(java.lang.String); }

