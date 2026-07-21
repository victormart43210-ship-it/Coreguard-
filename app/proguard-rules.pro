# ProGuard/R8 rules for CoreGuard release builds.
#
# The Android Gradle Plugin already contributes the default optimized Android
# rule set. These app-specific rules focus on protecting the security surface
# without breaking framework entry points or persisted app state.

# Preserve Kotlin/annotation metadata commonly needed by AndroidX and Kotlin
# generated code.
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod

# The release build enables shrinking, but the security validator is not yet
# wired into the app startup path. Keep it and its nested status model so the
# hardening code remains present in release artifacts.
-keep class com.android.performance.janktest.security.SecurityValidator {
    public *;
}

# Preserve enum helper methods used when restoring the stored subscription tier
# from SharedPreferences.
-keepclassmembers enum com.coldboar.coreguard.SubscriptionManager$Tier {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Strip Android log calls from release builds to reduce information leakage in
# logcat on production devices.
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
    public static int w(...);
    public static int e(...);
    public static int wtf(...);
}
