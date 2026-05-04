package ci.nsu.mobile.auth.di

import ci.nsu.mobile.auth.data.local.AuthManagerImpl
import ci.nsu.mobile.auth.data.local.TokenManager
import ci.nsu.mobile.auth.data.repository.AuthRepository
import ci.nsu.mobile.auth.navigation.AuthNavigatorImpl
import ci.nsu.mobile.auth.viewmodel.AuthViewModel
import ci.nsu.mobile.auth.viewmodel.RegisterViewModel
import ci.nsu.mobile.domain.auth.AuthManager
import ci.nsu.mobile.domain.auth.AuthNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single { TokenManager.apply { init(androidContext()) } }
    single { get<TokenManager>(); AuthRepository() }
    single<AuthManager> { get<TokenManager>(); AuthManagerImpl() }
    single<AuthNavigator> { AuthNavigatorImpl() }
    viewModel { AuthViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
}
