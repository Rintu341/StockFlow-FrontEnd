package org.sujan.stockflow

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.sujan.stockflow.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "StockFlow",
        ) {
            App()
        }
    }
}
