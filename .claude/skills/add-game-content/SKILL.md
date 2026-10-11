---
name: add-game-content
description: Add or edit dark-humour copy (dilemmas, devil's deals, epitaph fragments, home greetings) in Moral Hazard's data layer while keeping tests and tone intact. Use when asked to write more content for a game.
---

# Add game content

All copy is original and lives in code — there is no external source document. The canonical files:

| Content | File |
|---|---|
| Lesser Evil dilemmas | `feature/dilemma/src/commonMain/.../data/InMemoryDilemmaRepository.kt` |
| Devil's deals | `feature/devilsdeal/src/commonMain/.../data/DealDataSources.kt` (`InMemoryDealRepository`) |
| Epitaph fragments | `feature/epitaph/src/commonMain/.../data/InMemoryEpitaphRepository.kt` |
| Lobby greetings / game blurbs | `feature/home/src/commonMain/.../data/StaticGameCatalogRepository.kt` |

## Rules

- **Invariants enforced by `init` blocks and tests** — keep them true:
  - Dilemma: unique `id` (kebab-case), `corruption` 0..3 per option, `percentChoosingA` 0..100.
    Give the "worse" option the higher corruption; make the aftermath a punchline, not a lecture.
  - Deal: unique `id`, `soulCost` 1..100, `reward` > 0. Price/reward should create a real trade-off
    (decks hold 8 deals and total cost should regularly exceed 100% soul).
  - Epitaph openers must contain `{name}`; bodies should be distinct (the generator avoids repeats).
- **Tone**: dark and absurd, punching at the player, bureaucracy, the afterlife, or cosmic irony. No real
  people, brands, tragedies, or protected groups. No slurs, no self-harm encouragement.
- No emoji (Web can't render them). Use plain ASCII hyphens/quotes.
- Keep each string on one line; `data/**` files are exempt from ktlint's line length for this reason.
- Don't "clean up" or reword existing entries unless asked — tests and screenshots may reference them.

## Verify

`./gradlew :feature:<module>:testAndroidHostTest` (or `scripts/offline-check.sh` in a sandbox).
Commit as `feat(<module>): add N <things>`.
