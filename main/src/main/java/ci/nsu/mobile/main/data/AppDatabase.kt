package ci.nsu.mobile.main.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DepositCalculation::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun depositDao(): DepositDao
//Возвращает объект для доступа к данным (DAO)

    companion object {
        //изменения переменной видны всем потокам
        @Volatile
        private var INSTANCE: AppDatabase? = null
        //--------------------------------------
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