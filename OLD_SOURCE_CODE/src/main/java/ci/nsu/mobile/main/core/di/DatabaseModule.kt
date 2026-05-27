package ci.nsu.mobile.main.core.di

import android.content.Context
import ci.nsu.mobile.main.auth.data.datasource.local.TokenManager
import ci.nsu.mobile.main.calculations.data.database.AppDatabase
import ci.nsu.mobile.main.calculations.data.database.DepositDao
import ci.nsu.mobile.main.calculations.data.repository.DepositRepository
import dagger.Module
import dagger.Provides
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
    fun provideDepositRepository(depositDao: DepositDao,
                                 tokenManager: TokenManager
    ): DepositRepository {
        return DepositRepository(depositDao, tokenManager)
    }
}