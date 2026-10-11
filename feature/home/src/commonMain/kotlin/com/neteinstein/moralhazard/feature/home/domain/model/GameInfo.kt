package com.neteinstein.moralhazard.feature.home.domain.model

/** Every playable game. The app shell maps each id to a navigation destination. */
enum class GameId { LesserEvil, DevilsDeal, Epitaph }

/** Semantic accent; the UI layer maps it to theme colours. */
enum class GameAccent { Ember, Brimstone, Ectoplasm }

data class GameInfo(
    val id: GameId,
    val title: String,
    val tagline: String,
    val description: String,
    val accent: GameAccent,
)
