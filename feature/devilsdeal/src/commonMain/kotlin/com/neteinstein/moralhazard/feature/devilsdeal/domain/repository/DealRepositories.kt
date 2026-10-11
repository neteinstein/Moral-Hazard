package com.neteinstein.moralhazard.feature.devilsdeal.domain.repository

import com.neteinstein.moralhazard.feature.devilsdeal.domain.model.Deal

interface DealRepository {
    fun deals(): List<Deal>
}

interface HighScoreRepository {
    fun bestScore(): Int

    fun saveBestScore(score: Int)
}
