// Root build: declare every plugin once (apply false) so all modules share one classpath,
// and aggregate code coverage from every module that contains testable logic.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.kmp.library) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.kover)
}

dependencies {
    kover(projects.composeApp)
    kover(projects.core.designsystem)
    kover(projects.feature.home)
    kover(projects.feature.dilemma)
    kover(projects.feature.devilsdeal)
    kover(projects.feature.epitaph)
    kover(projects.feature.settings)
}

kover {
    reports {
        filters {
            excludes {
                // Composables and generated code are verified visually, not by unit tests.
                annotatedBy("androidx.compose.runtime.Composable")
                classes("*.ComposableSingletons*", "*.BuildConfig")
                packages("*.ui.theme", "*.ui.components")
            }
        }
    }
}
