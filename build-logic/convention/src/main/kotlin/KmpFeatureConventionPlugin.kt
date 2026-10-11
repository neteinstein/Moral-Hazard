import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * A feature module: Compose + MVVM (ViewModel/StateFlow) + Koin + the shared design system.
 * Features never depend on each other; the app shell (`:composeApp`) wires them together.
 */
class KmpFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(KmpComposeConventionPlugin::class.java)

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.getByName("commonMain").dependencies {
                implementation(project(":core:designsystem"))
                implementation(libs.library("lifecycle-viewmodel"))
                implementation(libs.library("lifecycle-viewmodel-compose"))
                implementation(libs.library("lifecycle-runtime-compose"))
                implementation(project.dependencies.platform(libs.library("koin-bom")))
                implementation(libs.library("koin-core"))
                implementation(libs.library("koin-compose"))
                implementation(libs.library("koin-compose-viewmodel"))
            }
        }
    }
}
