package com.neteinstein.moralhazard.feature.dilemma.domain.repository

import com.neteinstein.moralhazard.feature.dilemma.domain.model.Dilemma

interface DilemmaRepository {
    fun dilemmas(): List<Dilemma>
}
