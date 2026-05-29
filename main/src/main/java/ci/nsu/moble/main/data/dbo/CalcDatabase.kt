package ci.nsu.moble.main.data.dbo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ci.nsu.moble.main.data.dao.DepositDao
import ci.nsu.moble.main.data.entities.DepositCalculationEntity

@Database(entities = [DepositCalculationEntity::class], version = 1)
abstract class CalcDatabase : RoomDatabase() {
    abstract fun dao(): DepositDao
    companion object {
        @Volatile
        private var INSTANCE: CalcDatabase? = null

        fun getDatabase(context: Context): CalcDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    CalcDatabase::class.java,
                    "deposits_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}