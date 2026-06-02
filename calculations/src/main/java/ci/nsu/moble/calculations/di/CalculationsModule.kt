package ci.nsu.moble.calculations.di

import android.content.Context
import ci.nsu.moble.calculations.data.database.DepositDatabase
import ci.nsu.moble.calculations.data.repository.DepositRepository
import ci.nsu.moble.calculations.provider.CalculationsProviderImpl
import ci.nsu.moble.calculations.ui.DepositViewModel
import ci.nsu.moble.domain.interfaces.CalculationsProvider
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val calculationsModule = module {
    single { provideDepositDatabase(androidContext()) }
    single { get<DepositDatabase>().dao() }
    single { DepositRepository(get()) }
    single<CalculationsProvider> { CalculationsProviderImpl(get()) }
    viewModel { (userId: Long) -> DepositViewModel(userId, get()) }
}

private fun provideDepositDatabase(context: Context): DepositDatabase {
    return DepositDatabase.getDb(context)
}