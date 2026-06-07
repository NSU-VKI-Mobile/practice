package ci.nsu.mobile.calculations.di


import android.content.Context
import androidx.room.Room
import ci.nsu.mobile.calculations.data.room.AppDatabase
import ci.nsu.mobile.calculations.data.room.DepositDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideDepositDao(database: AppDatabase): DepositDao {
        return database.depositDao()
    }
}