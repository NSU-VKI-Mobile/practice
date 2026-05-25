package ci.nsu.mobile.main.data.local

import androidx.room.*
import ci.nsu.mobile.main.data.model.DepositCalculation
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Insert
    suspend fun insert(calculation: DepositCalculation)

    @Delete
    suspend fun delete(calculation: DepositCalculation)

    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId ORDER BY calculationDate DESC")
    fun getCalculationsByUser(userId: Long): Flow<List<DepositCalculation>>
}