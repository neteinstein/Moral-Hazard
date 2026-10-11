package com.neteinstein.moralhazard.feature.home.data

import com.neteinstein.moralhazard.feature.home.domain.model.GameAccent
import com.neteinstein.moralhazard.feature.home.domain.model.GameId
import com.neteinstein.moralhazard.feature.home.domain.model.GameInfo
import com.neteinstein.moralhazard.feature.home.domain.repository.GameCatalogRepository

/** Original copy, authored in-repo (see AGENTS.md "Content"). */
class StaticGameCatalogRepository : GameCatalogRepository {
    override fun games(): List<GameInfo> = listOf(
        GameInfo(
            id = GameId.LesserEvil,
            title = "The Lesser Evil",
            tagline = "Two bad options. Pick one. Live with it.",
            description = "Absurd moral dilemmas. We keep score of your corruption, and so does someone downstairs.",
            accent = GameAccent.Ember,
        ),
        GameInfo(
            id = GameId.DevilsDeal,
            title = "Deal With the Devil",
            tagline = "Read the fine print. Or don't. We prefer don't.",
            description = "Trade slices of your soul for perks. Hit zero and the house keeps everything.",
            accent = GameAccent.Brimstone,
        ),
        GameInfo(
            id = GameId.Epitaph,
            title = "Epitaph Engraver",
            tagline = "Pre-write your tombstone. Save your family the trouble.",
            description = "Generate the last word on your life, carved in stone and unfortunately accurate.",
            accent = GameAccent.Ectoplasm,
        ),
    )

    override fun greetings(): List<String> = listOf(
        "Welcome back. We kept your seat warm. Very warm.",
        "Your conscience called. We let it go to voicemail.",
        "Abandon all hope. Keep the app open, though.",
        "Today's forecast: 100% chance of poor decisions.",
        "Morally flexible? You'll fit right in.",
        "Remember: it's only a sin if someone's watching. Someone is.",
    )
}
