package org.sujan.stockflow.di

import org.koin.dsl.module
import org.sujan.stockflow.data.local.SessionStorage
import org.sujan.stockflow.data.local.TokenStorage
import org.sujan.stockflow.platform.IosSessionStorage
import org.sujan.stockflow.platform.IosTokenStorage

actual fun platformModule() = module {
    single<TokenStorage> { IosTokenStorage() }
    single<SessionStorage> { IosSessionStorage() }
}
