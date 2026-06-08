package ci.nsu.mobile.main.data.di


import android.content.Context
import androidx.room.Room

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

import ci.nsu.mobile.main.data.api.AuthApiService
import ci.nsu.mobile.main.data.local.SessionManager
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.data.roomDatabase.AppDatabase
import ci.nsu.mobile.main.data.roomDatabase.DepositDao

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context): SessionManager {
        return SessionManager(context)
    }


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
    @Singleton
    fun provideDao(db: AppDatabase): DepositDao {
        return db.dao()
    }

//    @Provides
//    @Singleton
//    fun provideRepository(
//        dao: DepositDao
//    ): DepositRepository {
//        return DepositRepository(dao)
//    }
}