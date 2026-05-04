package ci.nsu.mobile.main

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)  // ← ИГНОРИРУЕМ ДУБЛИКАТЫ
    suspend fun insert(calculation: DepositEntity)

    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    fun getAllCalculations(): Flow<List<DepositEntity>>

    @Query("DELETE FROM deposit_calculations")
    suspend fun deleteAll()
}