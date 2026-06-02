package ci.nsu.mobile.depositcalculator.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ci.nsu.mobile.depositcalculator.data.database.DepositDao

@Database(
    entities = [DepositCalculationEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun depositDao(): DepositDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "deposit_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
        fun getDatabases(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "deposit_database_v2"
                ).fallbackToDestructiveMigration()  // Добавьте для отладки
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// Конвертер для типов данных
class Converters {
    @androidx.room.TypeConverter
    fun fromDouble(value: Double?): Double? = value

    @androidx.room.TypeConverter
    fun toDouble(value: Double?): Double? = value
}
