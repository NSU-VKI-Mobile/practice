package ci.nsu.mobile.main.data.local


import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton



@Module
@InstallIn(SingletonComponent::class)
object DataModule {





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
}