# CI/CD

Workflow: `.github/workflows/ci.yml`. Triggers: every pull request, pushes to `main`, manual dispatch.
Runs for the same ref cancel each other (`concurrency`).

## Jobs (all run in parallel)

| Job | Runner | Command | Artifacts |
|---|---|---|---|
| Build · Android | ubuntu-latest | `./gradlew :androidApp:assembleDebug :androidApp:assembleRelease` | `android-apks` (APKs + R8 `mapping.txt`) |
| Unit tests (JVM) | ubuntu-latest | `./gradlew testAndroidHostTest` (every `commonTest`) | `test-reports` |
| Lint | ubuntu-latest | `./gradlew ktlintCheck :androidApp:lintDebug` | `lint-reports` |
| Coverage (Kover) | ubuntu-latest | `./gradlew koverXmlReport koverHtmlReport koverLog` | `coverage` (HTML + XML), summary on the run page |
| Build · Web (Wasm) | ubuntu-latest | `./gradlew :composeApp:wasmJsBrowserDistribution` | `web-dist` |
| Build · iOS (simulator) | macos-26 | `xcodebuild -scheme iosApp -sdk iphonesimulator CODE_SIGNING_ALLOWED=NO build` (the Xcode Run Script phase compiles the Kotlin framework) | — |

All jobs use JDK 21 (Temurin) and `gradle/actions/setup-gradle` for caching. The release build
exercises R8 (minify + resource shrinking), so obfuscation problems surface on every PR.

Dependabot (`.github/dependabot.yml`) opens weekly PRs for Gradle dependencies and GitHub Actions.
Every version bump still has to pass this pipeline (KMP/Compose compatibility is enforced by actually
compiling all targets).

## Secrets

| Secret | Used by | Required? | Notes |
|---|---|---|---|
| `GOOGLE_SERVICES_JSON` | all jobs (via `scripts/ci/provide-google-services.sh`) | No | Full contents of the real `google-services.json`. When absent, the committed placeholder `androidApp/google-services.json.ci` is copied in: enough to build, useless for talking to Firebase. |
| `MH_RELEASE_KEYSTORE_PATH`, `MH_RELEASE_KEYSTORE_PASSWORD`, `MH_RELEASE_KEY_ALIAS`, `MH_RELEASE_KEY_PASSWORD` | `androidApp` release signing (env vars) | Not yet wired | `androidApp/build.gradle.kts` signs `release` only when these are present. A future release job should decode a base64 keystore secret to a temp file and export these. |
| `GoogleService-Info.plist` | — | Not used yet | iOS has no Firebase yet. If added, follow the same `*.ci` placeholder pattern. |

Real credential files are gitignored (`google-services.json`, `GoogleService-Info.plist`, `*.jks`,
`*.keystore`, `*.p12`, `*.mobileprovision`).

## Signing status per platform

| Platform | Status |
|---|---|
| Android debug | Signed with the auto-generated debug key. |
| Android release | **Unsigned in CI** (validation only). Signing config reads `MH_RELEASE_*` env vars when set. No Play upload job yet. Crashlytics mapping upload is disabled unless `-Pmh.crashlytics.uploadMapping=true`. |
| iOS | **Unsigned** (`CODE_SIGNING_ALLOWED=NO`, simulator SDK). `TEAM_ID` in `iosApp/Configuration/Config.xcconfig` is empty in git. No TestFlight job yet. |
| Web | No signing. Bundle is uploaded as an artifact; no deploy job yet. |

## Not (yet) covered

- Instrumented Android tests / iOS simulator tests (`iosSimulatorArm64Test`) — commonTest is verified on
  the JVM only.
- Store deployment (Play, TestFlight) and web hosting.
