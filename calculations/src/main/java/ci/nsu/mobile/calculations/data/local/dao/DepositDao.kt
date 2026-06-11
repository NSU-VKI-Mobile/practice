package ci.nsu.mobile.calculations.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ci.nsu.mobile.calculations.data.local.model.DepositCalculation
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Insert
    suspend fun insert(calculation: DepositCalculation)

    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId ORDER BY calculationDate DESC")
    fun getByUserId(userId: Long): Flow<List<DepositCalculation>>

    @Query("DELETE FROM deposit_calculations WHERE id = :id")
    suspend fun deleteById(id: Long)
}