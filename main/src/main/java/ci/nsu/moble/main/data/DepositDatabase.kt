package ci.nsu.moble.main.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DepositCalculationEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DepositDatabase : RoomDatabase() {
    abstract fun depositDao(): DepositDao
}