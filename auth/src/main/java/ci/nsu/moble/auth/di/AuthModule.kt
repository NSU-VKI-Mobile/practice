package ci.nsu.moble.auth.di

import ci.nsu.moble.auth.data.network.NetworkModule
import ci.nsu.moble.auth.data.repository.AuthRepository
import ci.nsu.moble.auth.data.storage.TokenManager
import ci.nsu.moble.auth.manager.AuthManagerImpl
import ci.nsu.moble.auth.ui.login.LoginViewModel
import ci.nsu.moble.auth.ui.register.RegisterViewModel
import ci.nsu.moble.auth.ui.users.UsersViewModel
import ci.nsu.moble.domain.interfaces.AuthManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single { TokenManager(get()) }
    single { NetworkModule.provideApiService(get()) }
    single { AuthRepository(get(), get()) }
    single<AuthManager> { AuthManagerImpl(get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { UsersViewModel(get()) }
}