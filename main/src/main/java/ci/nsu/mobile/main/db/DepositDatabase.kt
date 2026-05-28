package ci.nsu.mobile.main.db;

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ci.nsu.mobile.main.dao.DepositDao
import ci.nsu.mobile.main.entity.DepositEntity

@Database(entities = [DepositEntity::class], version = 1)
abstract class DepositDatabase : RoomDatabase() {
    abstract fun depositDao(): DepositDao

    companion object {
        @Volatile
        private var INSTANCE: DepositDatabase? = null
        fun getInstance(context: Context): DepositDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DepositDatabase::class.java,
                    "deposit_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}