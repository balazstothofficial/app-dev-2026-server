package tcs.app.dev.server

import tcs.app.dev.shared.Option
import tcs.app.dev.shared.OptionId

object MockData {
    val Universities = listOf(
        Option(OptionId("lmu"), LMUTitle, LMUDescription),
        Option(OptionId("tum"), TUMTitle, TUMDescription)
    )

    private const val LMUTitle = "Ludwig-Maximilians-Universität"

    private const val TUMTitle = "Technische Universität München"

    private const val LMUDescription =
        "Die LMU gehört zu den forschungsstärksten Universitäten Europas. Mit ihrem ausdifferenzierten Fächerspektrum verfügt sie über ein herausragendes Potenzial für zukunftsweisende Forschung."

    private const val TUMDescription =
        "Durch verantwortungsvolle Forschung, vertrauenswürdige Innovationen und weitblickende Lehr- und Weiterbildungskonzepte gestalten wir eine nachhaltige Zukunft."

}
