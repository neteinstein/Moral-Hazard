package com.neteinstein.moralhazard

import androidx.compose.ui.window.ComposeUIViewController
import com.neteinstein.moralhazard.ui.App
import platform.UIKit.UIViewController

/** iOS entry point, consumed from Swift as `MainViewControllerKt.MainViewController()`. */
@Suppress("FunctionName", "ktlint:standard:function-naming")
fun MainViewController(): UIViewController = ComposeUIViewController { App() }
