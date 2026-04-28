package ci.nsu.mobile.main.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ci.nsu.mobile.main.models.ShoppingList


@Database(
    entities = [ShoppingList::class],
    version = 1,
    exportSchema = false //не создавать папку со схемой бд
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun shoppingListDao(): ShoppingListDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null //только одна бд

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) { //если INSTANCE пуст
                val instance = Room.databaseBuilder( //начало
                    context.applicationContext, //бд в приложении, а не активити
                    AppDatabase::class.java, //передача класса с аннотациями, для структуры бд
                    "shopping_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}