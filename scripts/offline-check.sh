#!/usr/bin/env bash
# Offline-ish verification for sandboxed/cloud agents that cannot reach Google Maven (dl.google.com).
#
# The real build needs AGP and AndroidX/Compose artifacts that only live on Google Maven, so it cannot even
# configure in such sandboxes. This script checks what CAN be checked from Maven Central alone:
#   1. ktlint over every Kotlin/KTS file (same version and .editorconfig as the Gradle plugin).
#   2. The pure-Kotlin layer (domain + data + ViewModels + navigator) compiled for the JVM, with tiny stubs
#      for androidx.lifecycle.ViewModel and SnapshotStateList, running every commonTest except DI-graph tests.
# Compose UI, Android, iOS and Web compilation are NOT covered: CI on the PR is the real build signal.
#
# Usage: scripts/offline-check.sh            (MAVEN_MIRROR overrides the Maven Central URL)
set -euo pipefail

REPO="$(cd "$(dirname "$0")/.." && pwd)"
MIRROR="${MAVEN_MIRROR:-https://maven-central.storage-download.googleapis.com/maven2}"
WORK="${OFFLINE_CHECK_DIR:-${TMPDIR:-/tmp}/moralhazard-offline-check}"
KTLINT_VERSION="$(sed -n 's/^ktlint = "\(.*\)"/\1/p' "$REPO/gradle/libs.versions.toml")"

mkdir -p "$WORK"

echo "==> ktlint $KTLINT_VERSION"
KTLINT_JAR="$WORK/ktlint-$KTLINT_VERSION.jar"
[[ -f "$KTLINT_JAR" ]] || curl -sSfL -o "$KTLINT_JAR" \
  "$MIRROR/com/pinterest/ktlint/ktlint-cli/$KTLINT_VERSION/ktlint-cli-$KTLINT_VERSION-all.jar"
(cd "$REPO" && java -jar "$KTLINT_JAR" "**/*.kt" "**/*.kts" "!**/build/**")

echo "==> JVM harness (domain/data/ViewModel tests)"
H="$WORK/harness"
rm -rf "$H/src" && mkdir -p "$H/gradle" "$H/src/commonMain/kotlin/stubs"
cp -r "$REPO/gradle/wrapper" "$H/gradle/" && cp "$REPO/gradlew" "$H/"
cp "$REPO/gradle/libs.versions.toml" "$H/gradle/"
cat > "$H/settings.gradle.kts" <<KTS
pluginManagement { repositories { maven("$MIRROR"); gradlePluginPortal() } }
dependencyResolutionManagement { repositories { maven("$MIRROR") } }
rootProject.name = "offline-check"
KTS
cat > "$H/build.gradle.kts" <<'KTS'
plugins { alias(libs.plugins.kotlin.multiplatform) }
val repo = file(providers.gradleProperty("repo").get())
val mods = listOf("feature/home", "feature/dilemma", "feature/devilsdeal", "feature/epitaph", "feature/settings", "composeApp")
kotlin {
    jvm()
    sourceSets {
        commonMain {
            mods.forEach { kotlin.srcDir(repo.resolve("$it/src/commonMain/kotlin")) }
            kotlin.exclude("**/ui/components/**", "**/ui/theme/**", "**/*Screen.kt", "**/AppNavDisplay.kt", "**/ui/App.kt", "**/di/**")
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.multiplatform.settings.noarg)
            }
        }
        commonTest {
            mods.forEach { kotlin.srcDir(repo.resolve("$it/src/commonTest/kotlin")) }
            kotlin.exclude("**/AppModuleTest.kt")
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.multiplatform.settings.test)
            }
        }
    }
}
KTS
cat > "$H/src/commonMain/kotlin/stubs/Lifecycle.kt" <<'KT'
package androidx.lifecycle

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

open class ViewModel

private val scopes = HashMap<ViewModel, CoroutineScope>()
val ViewModel.viewModelScope: CoroutineScope
    get() = scopes.getOrPut(this) { CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate) }
KT
cat > "$H/src/commonMain/kotlin/stubs/Snapshot.kt" <<'KT'
package androidx.compose.runtime

import androidx.compose.runtime.snapshots.SnapshotStateList

fun <T> mutableStateListOf(vararg elements: T): SnapshotStateList<T> = SnapshotStateList<T>().apply { addAll(elements) }
KT
cat > "$H/src/commonMain/kotlin/stubs/SnapshotStateList.kt" <<'KT'
package androidx.compose.runtime.snapshots

class SnapshotStateList<T> : MutableList<T> by ArrayList()
KT
(cd "$H" && ./gradlew jvmTest --console=plain -Prepo="$REPO" -Dorg.gradle.configuration-cache=false)
echo "==> Offline checks passed. Remember: Compose UI / Android / iOS / Web compile only in CI."
