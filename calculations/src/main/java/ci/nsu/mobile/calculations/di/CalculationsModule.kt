package ci.nsu.mobile.calculations.di

import ci.nsu.mobile.calculations.data.local.AppDatabase
import ci.nsu.mobile.calculations.data.repository.CalculationsProviderImpl
import ci.nsu.mobile.calculations.data.repository.DepositRepository
import ci.nsu.mobile.calculations.navigation.CalculationsNavigatorImpl
import ci.nsu.mobile.calculations.viewmodel.CalculationViewModel
import ci.nsu.mobile.calculations.viewmodel.DepositViewModel
import ci.nsu.mobile.domain.calculations.CalculationsNavigator
import ci.nsu.mobile.domain.calculations.CalculationsProvider
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val calculationsModule = module {
    single { AppDatabase.getDatabase(androidApplication()) }
    single { get<AppDatabase>().depositDao() }
    single { DepositRepository(get()) }
    single<CalculationsProvider> { CalculationsProviderImpl(get()) }
    single<CalculationsNavigator> { CalculationsNavigatorImpl() }
    viewModel { DepositViewModel(get()) }
    viewModel { CalculationViewModel() }
}
