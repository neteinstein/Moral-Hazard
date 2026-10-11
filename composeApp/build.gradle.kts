plugins {
    alias(libs.plugins.moralhazard.kmp.compose)
}

kotlin {
    // iOS: a static framework embedded by the Xcode Run Script phase (embedAndSignAppleFrameworkForXcode).
    listOf(iosArm64(), iosSimulatorArm64()).forEach { target ->
        target.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    // Web: Compose for Web on Kotlin/Wasm. `./gradlew :composeApp:wasmJsBrowserDevelopmentRun`
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.designsystem)
            implementation(projects.feature.home)
            implementation(projects.feature.dilemma)
            implementation(projects.feature.devilsdeal)
            implementation(projects.feature.epitaph)
            implementation(projects.feature.settings)

            implementation(libs.lifecycle.viewmodel)
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.lifecycle.runtime.compose)
            implementation(libs.lifecycle.viewmodel.navigation3)
            implementation(libs.navigation3.ui)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.multiplatform.settings.noarg)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
        commonTest.dependencies {
            implementation(libs.koin.test)
            implementation(libs.multiplatform.settings.test)
        }
    }
}
