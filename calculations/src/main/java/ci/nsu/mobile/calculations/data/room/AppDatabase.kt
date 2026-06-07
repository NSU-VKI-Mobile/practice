package ci.nsu.mobile.calculations.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ci.nsu.mobile.domain.models.DepositCalculation

@Database(entities = [DepositCalculation::class],
    version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun depositDao(): DepositDao
}