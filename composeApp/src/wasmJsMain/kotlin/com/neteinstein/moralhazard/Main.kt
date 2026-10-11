package com.neteinstein.moralhazard

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.neteinstein.moralhazard.di.initKoin
import com.neteinstein.moralhazard.ui.App

/** Web entry point (Kotlin/Wasm). */
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()
    ComposeViewport { App() }
}
