package com.neteinstein.moralhazard.feature.dilemma.domain.usecase

import com.neteinstein.moralhazard.feature.dilemma.domain.model.Dilemma
import com.neteinstein.moralhazard.feature.dilemma.domain.model.SoulVerdict
import com.neteinstein.moralhazard.feature.dilemma.domain.repository.DilemmaRepository
import kotlin.random.Random

/** Deals a shuffled round of distinct dilemmas. */
class StartDilemmaRoundUseCase(
    private val repository: DilemmaRepository,
    private val random: Random,
) {
    operator fun invoke(size: Int = ROUND_SIZE): List<Dilemma> = repository.dilemmas().shuffled(random).take(size)

    companion object {
        const val ROUND_SIZE = 8
    }
}

/** Turns accumulated corruption into a verdict, relative to the worst you could have done. */
class JudgeSoulUseCase {
    operator fun invoke(corruption: Int, maxCorruption: Int): SoulVerdict {
        val ratio = if (maxCorruption <= 0) 0f else corruption.toFloat() / maxCorruption
        return when {
            corruption <= 0 -> SoulVerdict(
                "Suspiciously Saintly",
                "Not one bad choice. Either you're a saint or you're lying. Heaven has opened an investigation.",
            )

            ratio < 0.25f -> SoulVerdict(
                "Mostly Harmless",
                "A few smudges on an otherwise clean soul. You'd return a shopping trolley, probably.",
            )

            ratio < 0.5f -> SoulVerdict(
                "Morally Flexible",
                "You bend, you don't break. Ethically, you're a yoga instructor.",
            )

            ratio < 0.75f -> SoulVerdict(
                "HR Has Been Notified",
                "Your choices have been forwarded to several departments, one of them below ground level.",
            )

            else -> SoulVerdict(
                "Management Material",
                "Downstairs has reviewed your application and would like to offer you a corner office. It's warm.",
            )
        }
    }
}
