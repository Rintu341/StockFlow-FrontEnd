package org.sujan.stockflow.di

import org.koin.dsl.module
import org.sujan.stockflow.data.local.SessionStorage
import org.sujan.stockflow.data.local.TokenStorage
import org.sujan.stockflow.platform.AndroidSessionStorage
import org.sujan.stockflow.platform.AndroidTokenStorage

actual fun platformModule() = module {
    single<TokenStorage> { AndroidTokenStorage(get()) }
    single<SessionStorage> { AndroidSessionStorage(get()) }
}
