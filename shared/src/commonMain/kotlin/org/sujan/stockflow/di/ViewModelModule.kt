package org.sujan.stockflow.di

import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import org.sujan.stockflow.presentation.auth.ChangePasswordViewModel
import org.sujan.stockflow.presentation.auth.LoginViewModel
import org.sujan.stockflow.presentation.dashboard.DashboardViewModel
import org.sujan.stockflow.presentation.navigation.AppStartViewModel
import org.sujan.stockflow.presentation.navigation.MainViewModel
import org.sujan.stockflow.presentation.settings.SettingsViewModel

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
//    viewModel { DashboardViewModel(get()) }
    viewModelOf(::AppStartViewModel)
    viewModelOf(::DashboardViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::ChangePasswordViewModel)
    viewModelOf(::SettingsViewModel)
}
