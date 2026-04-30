package ci.nsu.mobile.calculations.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ci.nsu.mobile.calculations.data.local.dao.DepositDao
import ci.nsu.mobile.calculations.data.local.entity.DepositCalculationEntity

@Database(entities = [DepositCalculationEntity::class], version = 2, exportSchema = false)
abstract class DepositDatabase : RoomDatabase() {
    abstract fun depositDao(): DepositDao

    companion object {
        @Volatile
        private var INSTANCE: DepositDatabase? = null

        fun getDatabase(context: Context): DepositDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DepositDatabase::class.java,
                    "deposits_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}