package ci.nsu.mobile.main.data.local.dao

import androidx.room.*
import ci.nsu.mobile.main.data.local.entities.DepositCalculation
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Insert
    suspend fun insert(calculation: DepositCalculation)

    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId ORDER BY calculationDate DESC")
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>>

    @Query("SELECT * FROM deposit_calculations WHERE id = :id AND userId = :userId")
    suspend fun getCalculationById(id: Long, userId: Long): DepositCalculation?

    @Delete
    suspend fun delete(calculation: DepositCalculation)

    @Query("DELETE FROM deposit_calculations WHERE userId = :userId")
    suspend fun deleteAllForUser(userId: Long)
}