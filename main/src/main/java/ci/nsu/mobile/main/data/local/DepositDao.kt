package ci.nsu.mobile.main.data.local

import androidx.room.*
import ci.nsu.mobile.main.data.models.DepositCalculation
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {

    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId ORDER BY calculationDate DESC")
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalculation(calculation: DepositCalculation)

    @Delete
    suspend fun deleteCalculation(calculation: DepositCalculation)
}