plugins {
    alias(libs.plugins.moralhazard.kmp.feature)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.multiplatform.settings.noarg)
        }
        commonTest.dependencies {
            implementation(libs.multiplatform.settings.test)
        }
    }
}
