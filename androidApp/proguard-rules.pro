# R8 rules for the Moral Hazard Android app.
# Library consumer rules (Compose, Coroutines, Koin, Firebase) ship inside their AARs/JARs; only add rules
# here for code that is reached reflectively from this project.

# Keep line numbers for readable Crashlytics stack traces; hide the original file names.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Koin resolves by KClass, not by name, and constructor references (singleOf(::Foo)) are plain lambdas,
# so no keep rules are needed. If you ever add reflection-based Koin APIs, add rules here.

# Enum-based persistence (ThemeMode.name is written to storage) must not be renamed.
-keepclassmembers enum com.neteinstein.moralhazard.** {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
