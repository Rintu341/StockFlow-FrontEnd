package org.sujan.stockflow.di

import org.koin.dsl.module
import org.sujan.stockflow.data.local.SessionStorage
import org.sujan.stockflow.data.local.TokenStorage
import org.sujan.stockflow.platform.DesktopSessionStorage
import org.sujan.stockflow.platform.DesktopTokenStorage

actual fun platformModule() = module {
    single<TokenStorage> { DesktopTokenStorage() }
    single<SessionStorage> { DesktopSessionStorage() }
}
