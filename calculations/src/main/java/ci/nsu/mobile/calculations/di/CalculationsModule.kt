package ci.nsu.mobile.calculations.di

import android.content.Context
import ci.nsu.mobile.calculations.data.database.AppDatabase
import ci.nsu.mobile.calculations.data.database.DepositDao
import ci.nsu.mobile.calculations.data.repository.DepositRepository
import ci.nsu.mobile.calculations.navigation.CalculationsNavigatorImpl
import ci.nsu.mobile.domain.auth.AuthManager  // ← из domain
import ci.nsu.mobile.domain.calculations.CalculationsNavigator
import ci.nsu.mobile.domain.calculations.CalculationsProvider
import dagger.Module
import dagger.Provides
import dagger.Binds
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideDepositDao(database: AppDatabase): DepositDao {
        return database.depositDao()
    }

    @Provides
    @Singleton
    fun provideDepositRepository(
        depositDao: DepositDao,
        authManager: AuthManager
    ): DepositRepository {
        return DepositRepository(depositDao, authManager)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class CalculationsModule {

    @Binds
    @Singleton
    abstract fun bindCalculationsProvider(
        depositRepository: DepositRepository
    ): CalculationsProvider

    @Binds
    @Singleton
    abstract fun bindCalculationsNavigator(
        calculationsNavigatorImpl: CalculationsNavigatorImpl
    ): CalculationsNavigator
}