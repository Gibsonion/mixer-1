# Add project specific ProGuard rules here.
# Keep Shizuku classes
-keep class dev.rikka.shizuku.** { *; }
-keepclassmembers class * {
    native <methods>;
}
