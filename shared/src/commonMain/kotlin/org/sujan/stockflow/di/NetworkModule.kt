package org.sujan.stockflow.di

import org.koin.dsl.module
import org.sujan.stockflow.data.local.SessionManager
import org.sujan.stockflow.data.remote.AuthApi
import org.sujan.stockflow.data.remote.DashboardApi
import org.sujan.stockflow.data.remote.HttpClientFactory

val networkModule = module {
    single { SessionManager(get(), get()) }
    single { HttpClientFactory.create(get(), get()) }
    single { AuthApi(get()) }
    single { DashboardApi(get()) }
}
