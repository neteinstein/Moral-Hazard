package com.neteinstein.moralhazard.feature.dilemma.data

import com.neteinstein.moralhazard.feature.dilemma.domain.model.Dilemma
import com.neteinstein.moralhazard.feature.dilemma.domain.model.DilemmaOption
import com.neteinstein.moralhazard.feature.dilemma.domain.repository.DilemmaRepository

/** Original copy, authored in-repo (see AGENTS.md "Content"). All scenarios are fiction. */
class InMemoryDilemmaRepository : DilemmaRepository {
    override fun dilemmas(): List<Dilemma> = DILEMMAS

    private companion object {
        val DILEMMAS = listOf(
            Dilemma(
                id = "trolley-landlord",
                prompt = "A runaway trolley is heading for five people. Pull the lever and it hits one person instead: your landlord.",
                optionA = DilemmaOption("Pull the lever. Twice, if possible.", 3, "Rent is now, legally speaking, nobody's problem."),
                optionB = DilemmaOption("Do nothing. Fate can handle the paperwork.", 1, "Five funerals. You send one card, signed 'everyone'."),
                percentChoosingA = 71,
            ),
            Dilemma(
                id = "grandma-centrepiece",
                prompt = "Grandma leaves you her fortune, on one condition: her urn stays on the dinner table. Forever.",
                optionA = DilemmaOption("Take the money. She makes a lovely centrepiece.", 2, "She finally gets a seat at the table. Pass her the salt."),
                optionB = DilemmaOption("Refuse. Some things money can't buy, like her approval.", 0, "You never had her approval anyway. Now you don't have the money either."),
                percentChoosingA = 64,
            ),
            Dilemma(
                id = "genie-hunger",
                prompt = "A genie offers to end world hunger, but every time anyone eats, you feel full.",
                optionA = DilemmaOption("Accept. Lunch was overrated.", 0, "Eight billion meals a day. You haven't been hungry since. Or comfortable."),
                optionB = DilemmaOption("Decline. You have brunch plans.", 3, "The eggs Benedict were excellent. History will be less kind."),
                percentChoosingA = 38,
            ),
            Dilemma(
                id = "date-or-cause",
                prompt = "You may learn the exact date of your death or the exact cause. Not both.",
                optionA = DilemmaOption("The date. You'll book a nice restaurant.", 0, "Table for one, the night before. The tiramisu is to die for."),
                optionB = DilemmaOption("The cause. Then avoid it, smugly, forever.", 1, "It's 'stepping on a rake'. You've already moved to a city with no gardens."),
                percentChoosingA = 52,
            ),
            Dilemma(
                id = "coworker-deadline",
                prompt = "Your coworker passed away at their desk. Nobody has noticed in three days. Their project is due Friday.",
                optionA = DilemmaOption("Finish the project and quietly take the credit.", 3, "Promoted. Your speech thanked 'the team'. The team was one guy, and he's still at his desk."),
                optionB = DilemmaOption("Tell HR. After lunch.", 1, "HR asks if this can wait until Monday. It can, apparently."),
                percentChoosingA = 23,
            ),
            Dilemma(
                id = "immortal-funerals",
                prompt = "You can live forever, but you must attend the funeral of everyone you've ever met.",
                optionA = DilemmaOption("Immortality. Buy a black suit in bulk.", 2, "By year 300 the florist sends you a Christmas card."),
                optionB = DilemmaOption("Mortality. Someone else can do the eulogies.", 0, "Your own eulogy runs twelve minutes. Seven of them are about your parking."),
                percentChoosingA = 41,
            ),
            Dilemma(
                id = "cult-benefits",
                prompt = "A cult offers free healthcare, a pension and a robe. The only catch: occasional chanting.",
                optionA = DilemmaOption("Join. The dental plan is phenomenal.", 2, "Your teeth have never been better. You can't remember your last name."),
                optionB = DilemmaOption("Decline and pay your own deductible like a sucker.", 0, "Free will: intact. Wisdom teeth: also intact, and furious."),
                percentChoosingA = 66,
            ),
            Dilemma(
                id = "wallet-dog",
                prompt = "You find a wallet holding 500 in cash and a photo of the owner's extremely sad dog.",
                optionA = DilemmaOption("Return it. That dog has suffered enough.", 0, "The owner cries. The dog does not. The dog has seen things."),
                optionB = DilemmaOption("Keep the cash. Mail the dog a postcard.", 3, "'Wish you were here.' The dog was not."),
                percentChoosingA = 81,
            ),
            Dilemma(
                id = "will-mime",
                prompt = "Your will can be read aloud by a lawyer, or performed live by a mime.",
                optionA = DilemmaOption("The lawyer. Dignity matters.", 0, "Your family inherits everything, plus a 400-an-hour invoice."),
                optionB = DilemmaOption("The mime. Let them fight over invisible boxes.", 1, "Your cousin is still trapped in the invisible box. It's been a year."),
                percentChoosingA = 34,
            ),
            Dilemma(
                id = "afterlife-office",
                prompt = "The afterlife is real, and it's an open-plan office. Pick your desk for eternity.",
                optionA = DilemmaOption("By the window. Eternity needs a view.", 1, "The view is of another open-plan office. Someone waves. Forever."),
                optionB = DilemmaOption("By the exit. Just in case.", 2, "The exit sign has been flickering for nine thousand years. Facilities is 'on it'."),
                percentChoosingA = 57,
            ),
            Dilemma(
                id = "billionaire-rocket",
                prompt = "A billionaire offers you ten million to ride their rocket. It was built by the lowest bidder.",
                optionA = DilemmaOption("Go. Money is money, even in orbit.", 2, "You are now, technically, the richest satellite."),
                optionB = DilemmaOption("Stay and watch the launch. Bring popcorn.", 1, "Best fireworks you've ever seen. You feel a bit bad. A bit."),
                percentChoosingA = 29,
            ),
            Dilemma(
                id = "phone-or-plant",
                prompt = "Your flat is on fire. You can save your phone or your six-year-old houseplant.",
                optionA = DilemmaOption("The phone. All the photos of the plant are on it.", 2, "You post a tasteful memorial. It gets 14 likes. The plant would've wanted 15."),
                optionB = DilemmaOption("The plant. It finally knows you care.", 0, "It drops a leaf in gratitude. Or smoke inhalation. Hard to say."),
                percentChoosingA = 58,
            ),
            Dilemma(
                id = "gym-membership",
                prompt = "Your doctor gives you six months to live. Your gym membership has seven left.",
                optionA = DilemmaOption("Cancel it. Finally, a valid excuse.", 1, "The gym charges a cancellation fee. Death, it turns out, is not a waiver."),
                optionB = DilemmaOption("Go every day. Die with abs nobody asked for.", 0, "Open casket. Obviously."),
                percentChoosingA = 47,
            ),
            Dilemma(
                id = "reaper-game",
                prompt = "The Grim Reaper challenges you to a game for your life. You get to pick the game.",
                optionA = DilemmaOption("Monopoly. He'll want to die by the end.", 1, "Six hours in, Death mortgages Park Lane and storms off. You live, technically, on a technicality."),
                optionB = DilemmaOption("Rock-paper-scissors. Best of seven thousand.", 2, "Death always picks scissors. He's not great at metaphors."),
                percentChoosingA = 63,
            ),
            Dilemma(
                id = "ex-lifeguard",
                prompt = "Your ex is drowning. You're the only lifeguard on duty. You are also on your break.",
                optionA = DilemmaOption("Save them, then mention it every day for the rest of their life.", 1, "They are alive. They wish, occasionally, that they weren't."),
                optionB = DilemmaOption("Breaks are a legal right. Check the union handbook.", 3, "The union backs you completely. The coroner is less enthusiastic."),
                percentChoosingA = 62,
            ),
        )
    }
}
