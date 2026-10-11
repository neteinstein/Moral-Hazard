package com.neteinstein.moralhazard.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * Platform DI bootstrap. Call exactly once, as early as possible:
 * - Android: `MoralHazardApplication.onCreate()` -> `initKoin { androidContext(this@MoralHazardApplication) }`
 * - iOS: `iOSApp.init()` -> `InitKoinKt.doInitKoin()` (Kotlin/Native prefixes `init*` functions with `do`)
 * - Web: `main()` -> `initKoin()`
 */
fun initKoin(config: KoinAppDeclaration?): KoinApplication = startKoin {
    config?.invoke(this)
    modules(appModule)
}

/** No-arg overload so Swift can call `InitKoinKt.doInitKoin()` without passing `nil`. */
fun initKoin(): KoinApplication = initKoin(config = null)
