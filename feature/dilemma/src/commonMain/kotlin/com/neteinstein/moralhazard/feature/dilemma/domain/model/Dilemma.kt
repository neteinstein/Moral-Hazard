package com.neteinstein.moralhazard.feature.dilemma.domain.model

enum class Side { A, B }

data class DilemmaOption(
    val text: String,
    /** How much this choice taints the soul, 0 (saintly) to 3 (management material). */
    val corruption: Int,
    /** Shown after choosing. */
    val aftermath: String,
)

data class Dilemma(
    val id: String,
    val prompt: String,
    val optionA: DilemmaOption,
    val optionB: DilemmaOption,
    /** Fictional "what everyone else picked" statistic for option A; B is the remainder. */
    val percentChoosingA: Int,
) {
    init {
        require(percentChoosingA in 0..100) { "percentChoosingA must be 0..100" }
        require(optionA.corruption in 0..MAX_CORRUPTION && optionB.corruption in 0..MAX_CORRUPTION)
    }

    fun option(side: Side): DilemmaOption = if (side == Side.A) optionA else optionB

    fun percentChoosing(side: Side): Int = if (side == Side.A) percentChoosingA else 100 - percentChoosingA

    val worstCorruption: Int get() = maxOf(optionA.corruption, optionB.corruption)

    companion object {
        const val MAX_CORRUPTION = 3
    }
}

data class SoulVerdict(val title: String, val description: String)
