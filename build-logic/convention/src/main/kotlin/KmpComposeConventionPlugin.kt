import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/** Adds Compose Multiplatform (compiler + runtime/UI/Material 3) on top of [KmpLibraryConventionPlugin]. */
class KmpComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(KmpLibraryConventionPlugin::class.java)
        pluginManager.apply(libs.pluginId("compose-multiplatform"))
        pluginManager.apply(libs.pluginId("compose-compiler"))

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.getByName("commonMain").dependencies {
                implementation(libs.library("compose-runtime"))
                implementation(libs.library("compose-foundation"))
                implementation(libs.library("compose-ui"))
                implementation(libs.library("compose-animation"))
                implementation(libs.library("compose-material3"))
                implementation(libs.library("compose-ui-tooling-preview"))
            }
        }
    }
}
