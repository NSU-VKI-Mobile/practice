package ci.nsu.mobile.main.Data.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ci.nsu.mobile.main.Data.DAO.CalculationDao
import ci.nsu.mobile.main.Data.Entity.CalculationEntity

@Database(entities = [CalculationEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun calculationDao(): CalculationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "calculations_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}