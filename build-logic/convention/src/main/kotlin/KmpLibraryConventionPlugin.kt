import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jlleitschuh.gradle.ktlint.KtlintExtension

/**
 * Base for every shared module: Kotlin Multiplatform with the AGP 9 KMP-library plugin
 * (`com.android.kotlin.multiplatform.library`), targets Android + iosArm64 + iosSimulatorArm64 + wasmJs,
 * host-side (JVM) tests for commonTest, Kover coverage and ktlint.
 */
class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.pluginId("kotlin-multiplatform"))
        pluginManager.apply(libs.pluginId("android-kmp-library"))
        pluginManager.apply(libs.pluginId("kover"))
        pluginManager.apply(libs.pluginId("ktlint"))

        extensions.configure<KtlintExtension> {
            version.set(libs.version("ktlint"))
        }

        extensions.configure<KotlinMultiplatformExtension> {
            // The AGP KMP-library target doubles as the Android DSL (`kotlin { android { } }`).
            targets.withType(KotlinMultiplatformAndroidLibraryTarget::class.java).configureEach {
                namespace = moralHazardNamespace
                compileSdk = libs.version("android-compileSdk").toInt()
                minSdk = libs.version("android-minSdk").toInt()
                // Runs commonTest on the JVM: `./gradlew testAndroidHostTest`
                withHostTest { }
            }

            // iosX64 is intentionally absent: removed upstream, Apple Silicon only.
            iosArm64()
            iosSimulatorArm64()

            @OptIn(ExperimentalWasmDsl::class)
            wasmJs {
                browser()
            }

            applyDefaultHierarchyTemplate()

            sourceSets.getByName("commonMain").dependencies {
                implementation(libs.library("kotlinx-coroutines-core"))
            }
            sourceSets.getByName("commonTest").dependencies {
                implementation(kotlin("test"))
                implementation(libs.library("kotlinx-coroutines-test"))
            }
        }
    }
}
