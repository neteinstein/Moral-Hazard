package com.neteinstein.moralhazard.feature.home.domain.repository

import com.neteinstein.moralhazard.feature.home.domain.model.GameInfo

interface GameCatalogRepository {
    fun games(): List<GameInfo>

    /** Rotating one-liners for the home header. */
    fun greetings(): List<String>
}
