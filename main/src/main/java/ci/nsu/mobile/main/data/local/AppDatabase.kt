package ci.nsu.mobile.main.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DepositEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): DepositDao
}