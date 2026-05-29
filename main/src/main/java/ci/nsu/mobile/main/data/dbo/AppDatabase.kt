package ci.nsu.mobile.main.data.dbo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ci.nsu.mobile.main.DepositCalculation
import ci.nsu.mobile.main.data.dao.DepositDao

@Database(
    entities = [DepositCalculation::class],
    version = 1
)

abstract class AppDatabase : RoomDatabase() {

    abstract fun depositDao(): DepositDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {

            return INSTANCE ?: synchronized(this) {

                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "deposit_db"
                ).build().also {

                    INSTANCE = it
                }
            }
        }
    }
}