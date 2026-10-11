# Moral Hazard

Dark-humour games about poor choices, for **Android, iOS and the Web** — one Kotlin Multiplatform +
Compose Multiplatform codebase.

- **The Lesser Evil** — two bad options; pick one; get judged.
- **Deal With the Devil** — trade slices of your soul for perks. Read the fine print (afterwards).
- **Epitaph Engraver** — pre-write your tombstone. Save your family the trouble.

All scenarios are fiction.

## Quick start

```bash
cp androidApp/google-services.json.ci androidApp/google-services.json   # or your real Firebase file
./gradlew :androidApp:installDebug                                      # Android
./gradlew :composeApp:wasmJsBrowserDevelopmentRun                       # Web
open iosApp/iosApp.xcodeproj                                            # iOS (macOS + Xcode)
./gradlew testAndroidHostTest ktlintCheck                               # tests + style
```

- Architecture, conventions and gotchas: [AGENTS.md](AGENTS.md)
- Pipeline, secrets and signing: [CI_CD.md](CI_CD.md)
