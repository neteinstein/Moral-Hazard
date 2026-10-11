import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension

// The Android application shell. AGP 9 forbids applying com.android.application together with
// org.jetbrains.kotlin.multiplatform, so all shared code lives in :composeApp (a KMP library) and this
// module only holds the manifest, Activity, Application class, resources and Firebase wiring.
// Kotlin is compiled by AGP 9's built-in Kotlin support (no kotlin-android plugin).
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.ktlint)
}

// Release signing comes from CI secrets / local env. Without them, release builds are unsigned
// (fine for CI validation, not for the Play Store). See CI_CD.md.
val releaseKeystore: String? = providers.environmentVariable("MH_RELEASE_KEYSTORE_PATH").orNull

android {
    namespace = "com.neteinstein.moralhazard.android"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.neteinstein.moralhazard"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0.0"
    }

    signingConfigs {
        if (releaseKeystore != null) {
            create("release") {
                storeFile = file(releaseKeystore)
                storePassword = providers.environmentVariable("MH_RELEASE_KEYSTORE_PASSWORD").orNull
                keyAlias = providers.environmentVariable("MH_RELEASE_KEY_ALIAS").orNull
                keyPassword = providers.environmentVariable("MH_RELEASE_KEY_PASSWORD").orNull
            }
        }
    }

    buildTypes {
        debug {
            manifestPlaceholders["firebaseCollectionEnabled"] = "false"
        }
        release {
            // R8 full mode: shrink, optimise and obfuscate. Keep rules live in proguard-rules.pro.
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            manifestPlaceholders["firebaseCollectionEnabled"] = "true"
            signingConfig = signingConfigs.findByName("release")
            (this as ExtensionAware).configure<CrashlyticsExtension> {
                // Uploading the R8 mapping needs real Firebase credentials; CI opts in explicitly.
                mappingFileUploadEnabled = providers.gradleProperty("mh.crashlytics.uploadMapping").orNull.toBoolean()
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }

    lint {
        abortOnError = true
        warningsAsErrors = false
        // Version-bump nags are handled by Dependabot, not by failing the build.
        disable += setOf("GradleDependency", "NewerVersionAvailable", "AndroidGradlePluginVersion")
    }
}

ktlint {
    version.set(libs.versions.ktlint.get())
}

dependencies {
    implementation(projects.composeApp)
    implementation(libs.compose.runtime)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.android)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
}
