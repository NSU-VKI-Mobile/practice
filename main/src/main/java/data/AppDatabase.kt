package data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [DepositEntity::class], //список классов Entity; массив
    version = 1,
    exportSchema = false //не созд. лишние файлы
)

abstract class AppDatabase : RoomDatabase()
{
    abstract fun depositDao(): DepositDao
    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "deposit_dp"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}