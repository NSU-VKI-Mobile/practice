package ci.nsu.mobile.calculations.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ci.nsu.mobile.calculations.data.local.entity.DepositCalculationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Insert
    suspend fun insert(calculation: DepositCalculationEntity)

    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId ORDER BY calculationDate DESC")
    fun getHistoryForUser(userId: Int): Flow<List<DepositCalculationEntity>>

    @Query("DELETE FROM deposit_calculations WHERE id = :id")
    suspend fun delete(id: Long)
}