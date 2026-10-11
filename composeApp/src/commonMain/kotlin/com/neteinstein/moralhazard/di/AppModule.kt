package com.neteinstein.moralhazard.di

import com.neteinstein.moralhazard.feature.devilsdeal.data.InMemoryDealRepository
import com.neteinstein.moralhazard.feature.devilsdeal.data.SettingsHighScoreRepository
import com.neteinstein.moralhazard.feature.devilsdeal.domain.repository.DealRepository
import com.neteinstein.moralhazard.feature.devilsdeal.domain.repository.HighScoreRepository
import com.neteinstein.moralhazard.feature.devilsdeal.domain.usecase.DrawDealsUseCase
import com.neteinstein.moralhazard.feature.devilsdeal.domain.usecase.FinalScoreUseCase
import com.neteinstein.moralhazard.feature.devilsdeal.domain.usecase.RecordScoreUseCase
import com.neteinstein.moralhazard.feature.devilsdeal.domain.usecase.SignDealUseCase
import com.neteinstein.moralhazard.feature.devilsdeal.ui.screens.DevilsDealViewModel
import com.neteinstein.moralhazard.feature.dilemma.data.InMemoryDilemmaRepository
import com.neteinstein.moralhazard.feature.dilemma.domain.repository.DilemmaRepository
import com.neteinstein.moralhazard.feature.dilemma.domain.usecase.JudgeSoulUseCase
import com.neteinstein.moralhazard.feature.dilemma.domain.usecase.StartDilemmaRoundUseCase
import com.neteinstein.moralhazard.feature.dilemma.ui.screens.LesserEvilViewModel
import com.neteinstein.moralhazard.feature.epitaph.data.InMemoryEpitaphRepository
import com.neteinstein.moralhazard.feature.epitaph.domain.repository.EpitaphRepository
import com.neteinstein.moralhazard.feature.epitaph.domain.usecase.GenerateEpitaphUseCase
import com.neteinstein.moralhazard.feature.epitaph.ui.screens.EpitaphViewModel
import com.neteinstein.moralhazard.feature.home.data.StaticGameCatalogRepository
import com.neteinstein.moralhazard.feature.home.domain.repository.GameCatalogRepository
import com.neteinstein.moralhazard.feature.home.domain.usecase.GetHomeFeedUseCase
import com.neteinstein.moralhazard.feature.home.ui.screens.HomeViewModel
import com.neteinstein.moralhazard.feature.settings.data.SettingsPreferencesRepository
import com.neteinstein.moralhazard.feature.settings.domain.repository.PreferencesRepository
import com.neteinstein.moralhazard.feature.settings.domain.usecase.ObserveThemeModeUseCase
import com.neteinstein.moralhazard.feature.settings.domain.usecase.SetThemeModeUseCase
import com.neteinstein.moralhazard.feature.settings.ui.screens.SettingsViewModel
import com.neteinstein.moralhazard.ui.AppViewModel
import com.russhwolf.settings.Settings
import kotlin.random.Random
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * The single place where every dependency in the app is registered. Platforms only call [initKoin];
 * they never declare modules of their own.
 */
val appModule: Module = module {
    // Core
    single<Settings> { Settings() }
    single<Random> { Random.Default }

    // App shell
    viewModelOf(::AppViewModel)

    // Feature: home
    singleOf(::StaticGameCatalogRepository) bind GameCatalogRepository::class
    factoryOf(::GetHomeFeedUseCase)
    viewModelOf(::HomeViewModel)

    // Feature: The Lesser Evil
    singleOf(::InMemoryDilemmaRepository) bind DilemmaRepository::class
    factoryOf(::StartDilemmaRoundUseCase)
    factoryOf(::JudgeSoulUseCase)
    viewModelOf(::LesserEvilViewModel)

    // Feature: Deal With the Devil
    singleOf(::InMemoryDealRepository) bind DealRepository::class
    singleOf(::SettingsHighScoreRepository) bind HighScoreRepository::class
    factoryOf(::DrawDealsUseCase)
    factoryOf(::SignDealUseCase)
    factoryOf(::FinalScoreUseCase)
    factoryOf(::RecordScoreUseCase)
    viewModelOf(::DevilsDealViewModel)

    // Feature: Epitaph Engraver
    singleOf(::InMemoryEpitaphRepository) bind EpitaphRepository::class
    factoryOf(::GenerateEpitaphUseCase)
    viewModelOf(::EpitaphViewModel)

    // Feature: settings
    singleOf(::SettingsPreferencesRepository) bind PreferencesRepository::class
    factoryOf(::ObserveThemeModeUseCase)
    factoryOf(::SetThemeModeUseCase)
    viewModelOf(::SettingsViewModel)
}
