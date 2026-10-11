---
name: offline-verify
description: Verify Moral Hazard changes inside a sandboxed/cloud container that cannot reach Google Maven (dl.google.com) — runs ktlint and the JVM-compilable domain/data/ViewModel tests. Use before committing when ./gradlew fails to resolve AGP/AndroidX.
---

# Offline verification

1. Try the real thing first: `./gradlew testAndroidHostTest ktlintCheck`. If it fails resolving
   `com.android.tools.build:gradle` / `androidx.*` (403 from the proxy, or `dl.google.com` unreachable),
   continue below.
2. Run `scripts/offline-check.sh`. It:
   - downloads the pinned ktlint CLI (version from `gradle/libs.versions.toml`) from a Maven Central
     mirror (`MAVEN_MIRROR`, default Google's `maven-central.storage-download.googleapis.com`, which avoids
     Maven Central's 429 rate limits) and lints every `.kt`/`.kts` file;
   - builds a throwaway JVM-only Gradle project that compiles the feature + composeApp `commonMain` code
     *minus* Compose UI (`*Screen.kt`, `ui/components`, `ui/theme`, `di/`, `App.kt`, `AppNavDisplay.kt`),
     stubs `androidx.lifecycle.ViewModel`/`SnapshotStateList`, and runs every `commonTest` except
     `AppModuleTest`.
3. Fix lint with the same jar: `java -jar <ktlint jar> --format "**/*.kt" "**/*.kts" "!**/build/**"`.
4. In the PR test plan, state what passed offline and list what could not run (Compose UI compile,
   Android assemble/lint, iOS, Web, `AppModuleTest`) — CI covers those.

If you add a feature module, add its path to the `mods` list in `scripts/offline-check.sh`.
