# Keep data classes for Room and Gson mapping
-keep class com.hackerrank.app.data.local.entity.** { *; }
-keep class com.hackerrank.app.domain.model.** { *; }
-keep class com.hackerrank.app.data.seed.** { *; }

# Keep Room generated database implementation
-keep class * extends androidx.room.RoomDatabase {
    <init>(...);
}
-dontwarn androidx.room.**

# Keep Hilt / Dagger
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep @dagger.hilt.InstallIn class * { *; }
-keep @dagger.hilt.android.AndroidEntryPoint class * { *; }
-keep class * extends dagger.hilt.internal.GeneratedComponent { *; }
-keep class * implements dagger.hilt.internal.GeneratedComponent { *; }
-keep class * implements dagger.hilt.internal.GeneratedComponentManager { *; }
-dontwarn dagger.hilt.**

# Gson rules
-keepattributes Signature, *Annotation*, EnclosingMethod, InnerClasses
-keep class com.google.gson.** { *; }
-keep class * extends com.google.gson.reflect.TypeToken
-dontwarn com.google.gson.**
