## Summary

<!-- What does this PR do and why? One or two sentences. -->

## Changes

<!-- Bullet the notable changes, grouped by module (e.g. `:feature:dilemma`, `:composeApp`, `androidApp`, `iosApp`, CI). -->

-

## Test plan

<!--
What you ran and what you saw. REQUIRED: also list what you could NOT run and why
(e.g. "iOS build not run locally: no macOS", "Android build not run: sandbox blocks dl.google.com").
CI on this PR is the source of truth for anything you could not run.
-->

- [ ] `./gradlew testAndroidHostTest`
- [ ] `./gradlew ktlintCheck`
- [ ] `./gradlew :androidApp:assembleDebug`
- [ ] iOS: `xcodebuild` / Run in Xcode
- [ ] Web: `./gradlew :composeApp:wasmJsBrowserDevelopmentRun`

**Not run (and why):**

-

## Risk

<!-- What could break? Platforms affected, migrations, dependency bumps, signing/secrets touched. Low / Medium / High. -->
