package org.sujan.stockflow.di

import org.koin.core.module.Module

expect fun platformModule(): Module

val appModule = listOf(
    networkModule,
    repositoryModule,
    viewModelModule,
    platformModule()
)
