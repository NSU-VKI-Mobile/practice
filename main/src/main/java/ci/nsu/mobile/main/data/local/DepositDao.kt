package ci.nsu.mobile.main.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Query("SELECT * FROM deposits ORDER BY id DESC")
    fun getAllCalculations(): Flow<List<DepositCalculation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(calculation: DepositCalculation)

    @Query("DELETE FROM deposits")
    suspend fun deleteAll()
}