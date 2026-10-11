---
name: add-feature-module
description: Scaffold a new feature module (e.g. a new game) in Moral Hazard following the repo's KMP clean-architecture layout, Koin wiring and navigation. Use when asked to add a game, screen or feature module.
---

# Add a feature module

Follow these steps exactly; each one is required for the module to build, be reachable and be tested.

1. **Gradle**
   - Create `feature/<name>/build.gradle.kts` containing only:
     ```kotlin
     plugins {
         alias(libs.plugins.moralhazard.kmp.feature)
     }
     ```
     Add extra dependencies only via `libs.` aliases (add them to `gradle/libs.versions.toml` first).
   - `include(":feature:<name>")` in `settings.gradle.kts`.
   - Add `implementation(projects.feature.<name>)` to `composeApp/build.gradle.kts` and
     `kover(projects.feature.<name>)` to the root `build.gradle.kts`.
   - The namespace is derived from the path (`com.neteinstein.moralhazard.feature.<name>`). Never add an
     `android {}` block.

2. **Code** under `feature/<name>/src/commonMain/kotlin/com/neteinstein/moralhazard/feature/<name>/`:
   - `domain/model/` plain data classes/enums — no Compose imports.
   - `domain/repository/` interfaces; `domain/usecase/` classes with `operator fun invoke`. Inject
     `kotlin.random.Random` for randomness.
   - `data/` implementations (content in-memory; persistence through `com.russhwolf.settings.Settings`).
   - `ui/screens/<Name>ViewModel.kt`: `data class <Name>UiState`, `MutableStateFlow` + `asStateFlow()`,
     public methods for events.
   - `ui/screens/<Name>Screen.kt`: `<Name>Screen(onBack, viewModel = koinViewModel())` collecting state with
     `collectAsStateWithLifecycle()`, and a stateless `<Name>Content(state, callbacks…, modifier)` with a
     `@Preview`. Wrap in `EmberBackground` + transparent `Scaffold` + `MhTopBar` like existing games.

3. **DI** — register everything in `composeApp/src/commonMain/.../di/AppModule.kt` (repositories with
   `singleOf(::Impl) bind Interface::class`, use cases `factoryOf`, ViewModel `viewModelOf`). Add the
   ViewModel to `AppModuleTest`.

4. **Navigation** — add a `data object` to `ui/navigation/Route.kt`, an `entry<Route.X> { … }` in
   `AppNavDisplay.kt`. If it's a game: add a `GameId`, a `GameInfo` in
   `feature/home/.../data/StaticGameCatalogRepository.kt`, map it in `GameId.toRoute()` and in
   `GameCard.kt` (`glyph()`), and add a `GlyphKind` drawing if needed.

5. **Tests** in `feature/<name>/src/commonTest/kotlin/...` covering use cases, data and the ViewModel.

6. **Verify**: `./gradlew :feature:<name>:testAndroidHostTest ktlintCheck`, or in a sandbox
   `scripts/offline-check.sh` (also add the module path to the `mods` list in that script).
   Commit as `feat(<name>): …`.
