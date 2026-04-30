package ci.nsu.moble.main.data.dbo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ci.nsu.moble.main.data.dao.DepositDao
import ci.nsu.moble.main.data.entities.DepositRecordEntity

@Database(entities = [DepositRecordEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): DepositDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "deposits_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}