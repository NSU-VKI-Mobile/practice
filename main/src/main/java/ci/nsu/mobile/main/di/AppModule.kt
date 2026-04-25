package ci.nsu.mobile.main.di

import android.content.Context
import androidx.room.Room
import ci.nsu.mobile.main.data.local.AppDatabase
import ci.nsu.mobile.main.data.local.DepositDao
import ci.nsu.mobile.main.data.repository.DepositRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "db"
        ).build()
    }

    @Provides
    fun provideDao(db: AppDatabase): DepositDao {
        return db.dao()
    }

    @Provides
    fun provideRepository(
        dao: DepositDao
    ): DepositRepository {
        return DepositRepository(dao)
    }
}