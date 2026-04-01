package ci.nsu.mobile.main.di

import ci.nsu.mobile.main.data.local.AppDatabase
import ci.nsu.mobile.main.data.repository.AuthRepository
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.viewmodel.AuthViewModel
import ci.nsu.mobile.main.viewmodel.DepositViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // База данных и DAO (создаются в единственном экземпляре - single)
    single { AppDatabase.getDatabase(androidApplication()) }
    single { get<AppDatabase>().depositDao() }

    // Репозитории (тоже синглтоны)
    single { AuthRepository() }
    single { DepositRepository(get()) } // get() сам найдет и подставит DepositDao!

    // ViewModels (создаются заново для каждого экрана)
    viewModel { AuthViewModel(get()) } // get() подставит AuthRepository
    viewModel { DepositViewModel(get()) } // get() подставит DepositRepository
}