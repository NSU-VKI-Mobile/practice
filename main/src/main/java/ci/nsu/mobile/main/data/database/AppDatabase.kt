package ci.nsu.mobile.main.data.database

import android.annotation.SuppressLint
import android.content.Context
import androidx.databinding.adapters.Converters

import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import androidx.room3.TypeConverters
import ci.nsu.mobile.main.models.DepositCalculation

@SuppressLint("RestrictedApi")
@Database(
    entities = [DepositCalculation::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun depositDao(): DepositDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "deposits_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}