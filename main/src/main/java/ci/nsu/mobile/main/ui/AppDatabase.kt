import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ci.nsu.mobile.main.ui.DepositCalculation

@Database(version = 1,
    entities = [
        DepositCalculation::class
    ])
abstract class AppDatabase : RoomDatabase() {
//    abstract fun depositDao(): DepositDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

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