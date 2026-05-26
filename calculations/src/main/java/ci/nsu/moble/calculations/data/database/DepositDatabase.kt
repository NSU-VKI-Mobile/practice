package ci.nsu.moble.calculations.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DepositEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class DepositDatabase : RoomDatabase() {
    abstract fun dao(): DepositDao

    companion object {
        @Volatile
        private var INSTANCE: DepositDatabase? = null

        fun getDb(context: Context): DepositDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DepositDatabase::class.java,
                    "deposits_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}