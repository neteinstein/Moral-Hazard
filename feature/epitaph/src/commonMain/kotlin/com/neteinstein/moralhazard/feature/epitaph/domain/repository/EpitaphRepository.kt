package com.neteinstein.moralhazard.feature.epitaph.domain.repository

import com.neteinstein.moralhazard.feature.epitaph.domain.model.EpitaphFragments

interface EpitaphRepository {
    fun fragments(): EpitaphFragments
}
