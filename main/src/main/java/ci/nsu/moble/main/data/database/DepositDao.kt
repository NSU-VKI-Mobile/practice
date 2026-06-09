package ci.nsu.moble.main.data.database

import androidx.room.*
import ci.nsu.moble.main.data.models.DepositCalculation
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Insert
    suspend fun insert(calculation: DepositCalculation)

    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId ORDER BY calculationDate DESC")
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>>

    @Delete
    suspend fun delete(calculation: DepositCalculation)
}