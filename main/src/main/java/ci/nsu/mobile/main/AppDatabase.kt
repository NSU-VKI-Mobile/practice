package ci.nsu.mobile.main

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
//одна бд на все приложение патттерн одиночка
@Database(
    entities = [Calculation::class],//какие табл будут в БД
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)//используем конвертер
abstract class AppDatabase : RoomDatabase() {
    abstract fun calculationDao(): CalculationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "calculations_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}