package org.sujan.stockflow.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.sujan.stockflow.presentation.auth.LoginViewModel
import org.sujan.stockflow.presentation.dashboard.DashboardViewModel

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { DashboardViewModel(get()) }
}
