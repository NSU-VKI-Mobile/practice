package ci.nsu.mobile.main.database

import androidx.room.*
import ci.nsu.mobile.main.model.DepositCalculation
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    fun getAllCalculations(): Flow<List<DepositCalculation>>

    @Query("SELECT * FROM deposit_calculations WHERE id = :id")
    suspend fun getCalculationById(id: Long): DepositCalculation?

    @Insert
    suspend fun insertCalculation(calculation: DepositCalculation)

    @Delete
    suspend fun deleteCalculation(calculation: DepositCalculation)

    @Query("DELETE FROM deposit_calculations")
    suspend fun deleteAllCalculations()
}