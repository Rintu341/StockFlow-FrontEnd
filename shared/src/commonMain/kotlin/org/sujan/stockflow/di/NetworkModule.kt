package org.sujan.stockflow.di

import org.koin.dsl.module
import org.sujan.stockflow.data.remote.AuthApi
import org.sujan.stockflow.data.remote.HttpClientFactory

val networkModule = module {
    single { HttpClientFactory.create() }
    single { AuthApi(get()) }
}
