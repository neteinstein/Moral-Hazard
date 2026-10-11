package com.neteinstein.moralhazard.feature.epitaph.domain.usecase

import com.neteinstein.moralhazard.feature.epitaph.domain.model.Epitaph
import com.neteinstein.moralhazard.feature.epitaph.domain.repository.EpitaphRepository
import kotlin.random.Random

/** Mad-libs a tombstone. Never repeats the previous body back-to-back, so every tap feels new. */
class GenerateEpitaphUseCase(
    private val repository: EpitaphRepository,
    private val random: Random,
) {
    operator fun invoke(name: String, previous: Epitaph? = null): Epitaph {
        val fragments = repository.fragments()
        val bodies = fragments.bodies.filterNot { it == previous?.body }.ifEmpty { fragments.bodies }
        return Epitaph(
            heading = fragments.openers.random(random).replace(NAME_TOKEN, sanitizeName(name)),
            body = bodies.random(random),
            dates = fragments.dates.random(random),
            closer = fragments.closers.random(random),
        )
    }

    companion object {
        const val NAME_TOKEN = "{name}"
        const val MAX_NAME_LENGTH = 24
        const val DEFAULT_NAME = "Our Dear Player"

        /** Trims, collapses whitespace and caps length. Blank names get a polite default. */
        fun sanitizeName(raw: String): String {
            val cleaned = raw.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }.joinToString(" ")
            return cleaned.take(MAX_NAME_LENGTH).trimEnd().ifEmpty { DEFAULT_NAME }
        }
    }
}
