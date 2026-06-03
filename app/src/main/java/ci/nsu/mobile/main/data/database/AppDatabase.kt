package ci.nsu.mobile.main.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ci.nsu.mobile.main.data.model.NotificationItem

@Database(
    entities = [NotificationItem::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "notifications_db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                println("🗄️ Database created: $instance")
                instance
            }
        }
    }
}