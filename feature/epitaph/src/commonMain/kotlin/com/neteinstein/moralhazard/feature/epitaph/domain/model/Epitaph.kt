package com.neteinstein.moralhazard.feature.epitaph.domain.model

data class Epitaph(
    val heading: String,
    val body: String,
    val dates: String,
    val closer: String,
)

/** Building blocks; `{name}` in [openers] is replaced with the deceased-to-be. */
data class EpitaphFragments(
    val openers: List<String>,
    val bodies: List<String>,
    val dates: List<String>,
    val closers: List<String>,
)
