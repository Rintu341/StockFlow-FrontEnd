package org.sujan.stockflow.di

import org.koin.dsl.module
import org.sujan.stockflow.data.repository.AuthRepositoryImpl
import org.sujan.stockflow.domain.repository.AuthRepository
import org.sujan.stockflow.domain.usecase.LoginUseCase

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(
        get(), get(), get()
    ) }
    single { LoginUseCase(get()) }
}
