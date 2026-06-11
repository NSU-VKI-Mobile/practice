package ci.nsu.mobile.calculations.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ci.nsu.mobile.calculations.data.local.dao.DepositDao
import ci.nsu.mobile.calculations.data.local.model.DepositCalculation

@Database(entities = [DepositCalculation::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun depositDao(): DepositDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "deposits_db")
                    .build().also { INSTANCE = it }
            }
        }
    }
}