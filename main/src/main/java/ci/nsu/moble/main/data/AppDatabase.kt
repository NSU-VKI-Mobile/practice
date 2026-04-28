package ci.nsu.moble.main.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DepositCalculation::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dao(): DepositDao

    companion object {
        // @Volatile — значение переменной сразу видно всем потокам
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Singleton: всегда возвращаем один и тот же экземпляр
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "deposits.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}