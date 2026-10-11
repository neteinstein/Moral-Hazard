# AGENTS.md — Moral Hazard

Guidance for humans and coding agents working in this repo. Read it all before your first change;
the **Gotchas** section exists because each item has bitten someone.

Moral Hazard is a collection of small **dark-humour games** for Android, iOS and Web, built from one
Kotlin Multiplatform codebase:

| Game | Module | What it is |
|---|---|---|
| The Lesser Evil | `:feature:dilemma` | Absurd two-option moral dilemmas; tracks your "corruption" and ends with a verdict. |
| Deal With the Devil | `:feature:devilsdeal` | Sign or refuse contracts; perks cost soul, hit 0% soul and you score nothing. Best score persisted. |
| Epitaph Engraver | `:feature:epitaph` | Mad-libs tombstone generator with a carve animation. |

Plus `:feature:home` (game lobby) and `:feature:settings` (light/dark/system theme, persisted).

## Stack

- **Kotlin 2.4** Multiplatform + **Compose Multiplatform 1.12** (Material 3), targets: Android,
  `iosArm64` + `iosSimulatorArm64`, `wasmJs` (browser).
- **AGP 9.4**: `com.android.kotlin.multiplatform.library` for shared modules, `com.android.application`
  (with AGP's built-in Kotlin) for `:androidApp`. compileSdk/targetSdk **37**, minSdk **32**.
- **MVVM**: JetBrains `lifecycle-viewmodel` + Coroutines `StateFlow`.
- **Navigation 3** (`org.jetbrains.androidx.navigation3`) with per-entry ViewModel stores.
- **Koin 4.2** for DI (`koin-compose-viewmodel`).
- **multiplatform-settings** (no-arg) for persistence: SharedPreferences / NSUserDefaults / localStorage.
- **Firebase** Analytics + Crashlytics on Android only.
- Tests: `kotlin.test` + `kotlinx-coroutines-test` in `commonTest`. Coverage: **Kover**. Lint: **ktlint** + Android lint.
- Gradle **9.8** with Kotlin DSL, version catalog, and convention plugins in `build-logic/`.

## Module layout

```
androidApp/            Android application shell (com.android.application): manifest, MainActivity,
                       MoralHazardApplication (Firebase + Koin init), resources, R8 rules.
composeApp/            KMP library: App() root, Navigation 3 host, AppViewModel, the ONE Koin module
                       (di/AppModule.kt), the platform DI bootstrap (di/InitKoin.kt), iOS
                       MainViewController, Web main(). Produces the iOS framework "ComposeApp" and the
                       wasm web bundle.
core/designsystem/     Theme (palette, typography, shapes, animated light/dark) + shared components
                       (EmberBackground, ChoiceCard, HazardMeter, ResultPanel, GameGlyph, MhIcons...).
feature/<name>/        One module per feature: home, dilemma, devilsdeal, epitaph, settings.
build-logic/           Included build with convention plugins (moralhazard.kmp.library/compose/feature).
iosApp/                Hand-authored Xcode project (see Gotchas).
scripts/               CI helpers and offline-check.sh for sandboxed agents.
```

**Why it's split this way**

- **`androidApp` vs `composeApp`**: AGP 9 refuses to apply `org.jetbrains.kotlin.multiplatform` and
  `com.android.application` to the same module. So shared UI/logic lives in a KMP *library*
  (`composeApp`, `com.android.kotlin.multiplatform.library`) and the APK is built by a thin `androidApp`.
- **One module per feature**: features compile in parallel, can't reach into each other (they never
  depend on one another), and keep their domain/data/ui together. `composeApp` is the only module that
  sees all features; it wires navigation and DI.
- **`core/designsystem`**: the only thing features share. No domain code lives there.

**Inside every feature module** (commonMain-first, clean architecture):

```
feature/<name>/src/commonMain/kotlin/com/neteinstein/moralhazard/feature/<name>/
  domain/model/        Plain Kotlin models. NO Compose types, ever (no Color, no @Composable).
  domain/repository/   Repository interfaces.
  domain/usecase/      Use cases (operator fun invoke). Pure, injectable Random for anything random.
  data/                Repository implementations and data sources (in-memory content, Settings).
  ui/screens/          <Name>Screen (pulls state from ViewModel) + <Name>Content (stateless) + <Name>ViewModel.
  ui/components/       Feature-private composables.
feature/<name>/src/commonTest/kotlin/...   Tests for domain/data/ViewModel.
```

Theme lives in `core/designsystem/.../ui/theme`, navigation in `composeApp/.../ui/navigation`.

Default **all** code to `commonMain`. Use `androidMain`/`iosMain`/`wasmJsMain` or `expect`/`actual` only
for what a platform genuinely cannot share. Today that is exactly:
`PlatformThemeEffect` (system-bar/window appearance), iOS `MainViewController`, Web `main()`, plus the
Android shell in `androidApp`.

## Conventions

### MVVM
- A **screen** composable (`FooScreen`) gets its ViewModel via `koinViewModel()`, collects
  `state` with `collectAsStateWithLifecycle()`, and passes state + method references down.
- A **content** composable (`FooContent`) is stateless: `state` + callbacks + `modifier`. It is what
  `@Preview`s render. No business logic in composables — compute in the ViewModel or a use case.
- ViewModels expose **one immutable state class** as `StateFlow<FooUiState>`; UI events come back as
  plain method calls (`viewModel.choose(Side.A)`), not event objects.
- ViewModels depend on **use cases**, use cases on **repository interfaces**.
- Randomness is always an injected `kotlin.random.Random` so tests can seed it.

### Koin
- **Every** registration lives in `composeApp/src/commonMain/.../di/AppModule.kt`. Don't add per-platform
  or per-feature Koin modules.
- The bootstrap (`initKoin`) lives in its own file, `di/InitKoin.kt`, and is called once per platform:
  Android `MoralHazardApplication`, iOS `iOSApp.init()`, Web `main()`.
- `AppModuleTest` resolves every ViewModel from the real graph — add new ViewModels to it.

### Testing
- All shared tests go in `commonTest` with `kotlin.test`. They run on the JVM via the Android host-test
  compilation: `./gradlew testAndroidHostTest`.
- New domain/data code arrives **with tests**. ViewModel tests use `Dispatchers.setMain(UnconfinedTestDispatcher())`
  and in-memory fakes (`MapSettings`, anonymous repository objects).
- Composables are not unit-tested; keep them thin so there's nothing to test.

### Version catalog
- **Every** version is in `gradle/libs.versions.toml`. Never inline a version in a build file — not in
  module scripts, not in `build-logic` (it reads the same catalog).
- Before bumping, confirm the artifact is stable and published for **all** our targets (android, iosArm64,
  iosSimulatorArm64, wasmJs) and compatible with the current Kotlin / Compose Multiplatform versions.
- Kotlin-compiler-coupled plugins (`kotlin`, compose compiler) share the `kotlin` version.

### Commits
Conventional Commits: `feat(dilemma): …`, `fix(ios): …`, `docs(agents): …`, `build(deps): …`, `ci: …`,
`test(devilsdeal): …`, `refactor(designsystem): …`. Scope = module or area.

## Build & verify

```bash
./gradlew testAndroidHostTest                 # all commonTest suites on the JVM
./gradlew ktlintCheck                         # style (ktlintFormat to auto-fix)
./gradlew :androidApp:lintDebug               # Android lint
./gradlew koverHtmlReport                     # aggregated coverage -> build/reports/kover/html
./gradlew :androidApp:assembleDebug           # APK (needs androidApp/google-services.json, see below)
./gradlew :androidApp:assembleRelease         # R8-minified release (unsigned unless MH_RELEASE_* env set)
./gradlew :composeApp:wasmJsBrowserDevelopmentRun   # Web dev server
./gradlew :composeApp:wasmJsBrowserDistribution     # Web production bundle
open iosApp/iosApp.xcodeproj                  # iOS: build & run from Xcode (macOS only)
```

Local Android builds need `androidApp/google-services.json`. Use your real Firebase file, or for a
throwaway build: `cp androidApp/google-services.json.ci androidApp/google-services.json` (gitignored).

### Sandboxed / cloud agents

Cloud agent containers often **cannot build this project**: `dl.google.com` (Google Maven: AGP,
AndroidX, Compose runtime, Firebase, Android SDK) is typically blocked, there's no emulator, no macOS
for iOS, and Maven Central may rate-limit (HTTP 429). Gradle can't even *configure* the project
without AGP.

What you **can** check offline: run `scripts/offline-check.sh`. It uses a Maven Central mirror to run
ktlint on every file and to compile + test the pure-Kotlin layer (domain, data, ViewModels, navigator)
on the JVM with tiny stubs. It does **not** compile Compose UI, Android, iOS or Web.

**CI on the PR is the real build signal.** Say in the PR's test plan exactly what you couldn't run.

## Content

All game copy (dilemmas, devil's deals, epitaph fragments, greetings) is **original writing authored
in this repo** — there is no external spec, spreadsheet or design doc it derives from, so there's no
`docs/` source of truth. The canonical copy is the data layer:
`feature/*/src/commonMain/.../data/*.kt`. When adding content follow `.claude/skills/add-game-content`.
Keep the tone: dark, absurd, aimed at the player or at institutions, never at real people or groups.

## Gotchas

- **AGP 9 + KMP**: never apply `com.android.application` and `org.jetbrains.kotlin.multiplatform` to one
  module. Shared modules use `com.android.kotlin.multiplatform.library`; configure them through the
  convention plugin (`targets.withType<KotlinMultiplatformAndroidLibraryTarget>()`), not an `android {}`
  block at the top level.
- **The JVM unit-test task is `testAndroidHostTest`**, not `testDebugUnitTest`: the KMP-library plugin has
  no build variants. Host tests are only created because the convention plugin calls `withHostTest {}`.
- **`androidApp` uses AGP's built-in Kotlin** — don't add `org.jetbrains.kotlin.android`. It still needs
  `org.jetbrains.kotlin.plugin.compose` and a direct `compose-runtime` dependency for `setContent {}`.
- **iOS Koin init is `InitKoinKt.doInitKoin()`**: Kotlin/Native's Obj-C exporter renames top-level
  functions starting with `init` by prefixing `do`. There's a no-arg `initKoin()` overload so Swift
  doesn't have to pass `nil`.
- **The Xcode project is hand-authored** (`iosApp/iosApp.xcodeproj/project.pbxproj`) — no CocoaPods, no
  xcodegen, no SPM for Kotlin. One app target; a "Compile Kotlin Framework" Run Script phase runs
  `./gradlew :composeApp:embedAndSignAppleFrameworkForXcode` before Swift compiles; the framework is
  found through `FRAMEWORK_SEARCH_PATHS` = `composeApp/build/xcode-frameworks/$(CONFIGURATION)/$(SDK_NAME)`
  and linked with `-framework ComposeApp`. `ENABLE_USER_SCRIPT_SANDBOXING = NO` is required for Gradle.
  When adding a Swift file, add a `PBXFileReference`, a `PBXBuildFile`, the group child, and the Sources
  phase entry, with new unique 24-hex IDs. Signing team goes in `iosApp/Configuration/Config.xcconfig`
  (`TEAM_ID`, empty in git).
- **No iosX64** target: it was removed upstream; simulator is arm64-only (`EXCLUDED_ARCHS` excludes x86_64).
- **No emoji / material-icons in UI**: Web has no emoji font (renders tofu) and the material-icons
  artifacts are deprecated. Use `MhIcons` (vector paths) or draw with `Canvas` (`GameGlyph`).
- **multiplatform-settings no-arg on Android** gets its Context via androidx-startup automatically; in
  tests always inject `MapSettings`.
- **Theme vs system bars**: the in-app theme can differ from the OS theme, so `PlatformThemeEffect`
  syncs status-bar icons (Android) / `overrideUserInterfaceStyle` (iOS). On iOS, "System" must clear the
  override or `isSystemInDarkTheme()` will read back our own override.
- **Secrets**: `google-services.json`, `GoogleService-Info.plist`, keystores are gitignored. CI copies
  `androidApp/google-services.json.ci` (a placeholder) unless the `GOOGLE_SERVICES_JSON` secret exists.
  Never commit a real one, and never "fix" CI by committing credentials.
- **Crashlytics mapping upload** is off unless `-Pmh.crashlytics.uploadMapping=true`; it needs real
  Firebase credentials.
- **ktlint**: style is `android_studio` via `.editorconfig`; composables are exempt from function naming;
  `data/**` files may exceed the line length so content strings aren't split mid-sentence.
- **Repositories live only in `settings.gradle.kts`.** The mode is not `FAIL_ON_PROJECT_REPOS` because the
  Kotlin/Wasm plugin registers Node.js/Binaryen download repositories itself; don't add repositories in modules.
- **Configuration cache is on.** Don't read env vars / files eagerly in build scripts; use `providers.*`.

## Further reading

- `CI_CD.md` — pipeline jobs, secrets, signing status per platform.
- `.claude/skills/` — packaged recurring tasks (new feature module, new game content, offline verification).
