package ci.nsu.mobile.main.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DepositCalculationEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun depositDao(): DepositDao
}