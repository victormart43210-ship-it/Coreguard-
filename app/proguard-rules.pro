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
-keep class **.SecurityValidator {
    public *;
}

# Preserve enum helper methods used when restoring the stored subscription tier
# from SharedPreferences.
-keepclassmembers enum **.SubscriptionManager$Tier {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Strip verbose/debug/info Android log calls from release builds to reduce
# routine information leakage in logcat on production devices while preserving
# warning/error diagnostics.
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int d(...);
    public static int i(...);
}
