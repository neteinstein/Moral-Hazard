plugins {
    `kotlin-dsl`
}

group = "com.neteinstein.moralhazard.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.compose.compiler.gradlePlugin)
    compileOnly(libs.kover.gradlePlugin)
    compileOnly(libs.ktlint.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("kmpLibrary") {
            id = libs.plugins.moralhazard.kmp.library.get().pluginId
            implementationClass = "KmpLibraryConventionPlugin"
        }
        register("kmpCompose") {
            id = libs.plugins.moralhazard.kmp.compose.get().pluginId
            implementationClass = "KmpComposeConventionPlugin"
        }
        register("kmpFeature") {
            id = libs.plugins.moralhazard.kmp.feature.get().pluginId
            implementationClass = "KmpFeatureConventionPlugin"
        }
    }
}
