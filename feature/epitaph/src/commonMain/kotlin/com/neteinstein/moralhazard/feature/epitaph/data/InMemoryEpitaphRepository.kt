package com.neteinstein.moralhazard.feature.epitaph.data

import com.neteinstein.moralhazard.feature.epitaph.domain.model.EpitaphFragments
import com.neteinstein.moralhazard.feature.epitaph.domain.repository.EpitaphRepository

/** Original copy, authored in-repo (see AGENTS.md "Content"). */
class InMemoryEpitaphRepository : EpitaphRepository {
    override fun fragments(): EpitaphFragments = FRAGMENTS

    private companion object {
        val FRAGMENTS = EpitaphFragments(
            openers = listOf(
                "Here lies {name}",
                "In loving memory of {name}",
                "Beneath this stone: {name}",
                "{name} rests here",
                "Reserved for {name}",
            ),
            bodies = listOf(
                "Read the terms and conditions in full. Died of boredom on page 41.",
                "Said 'hold my drink' with total confidence.",
                "Lost a fight with a vending machine. Still owed 50 cents.",
                "Replied all, one time too many.",
                "Taken too soon by the second-cheapest sushi.",
                "Whose last words were 'I saw this done on YouTube.'",
                "Tried to pet the 'friendly' goose.",
                "Asked the bear for a selfie. The bear said no.",
                "Finally found inner peace. Also the bottom of the lake.",
                "Microwaved fish in the office. The office remembered.",
                "Checked if the stove was hot. Thoroughly.",
                "Believed the lifeguard would come back from lunch.",
            ),
            dates = listOf(
                "Born: yes. Died: also yes.",
                "Too soon - not soon enough",
                "Earlier than planned - right on schedule",
                "Some Tuesday - another Tuesday",
                "Limited edition - now discontinued",
            ),
            closers = listOf(
                "Gone, but not forgiven.",
                "Finally on time for something.",
                "Please stop calling.",
                "Do not disturb. Seriously.",
                "Back in five minutes (est. eternity).",
                "At least the houseplants are thriving.",
                "Visiting hours: never.",
                "No refunds.",
            ),
        )
    }
}
