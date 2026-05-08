package ci.nsu.mobile.main.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalculation(calculation: DepositCalculation)

    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    fun getAllCalculations(): Flow<List<DepositCalculation>>

    @Query("SELECT * FROM deposit_calculations WHERE id = :id")
    suspend fun getCalculationById(id: Long): DepositCalculation?
}