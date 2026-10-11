rootProject.name = "MoralHazard"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    // Not FAIL_ON_PROJECT_REPOS: the Kotlin/Wasm plugin adds its own toolchain repositories (Node.js,
    // Binaryen, Yarn) at the project level. Modules must still never declare repositories themselves.
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

// App shells
include(":androidApp")
include(":composeApp")

// Shared core
include(":core:designsystem")

// One module per feature
include(":feature:home")
include(":feature:dilemma")
include(":feature:devilsdeal")
include(":feature:epitaph")
include(":feature:settings")
