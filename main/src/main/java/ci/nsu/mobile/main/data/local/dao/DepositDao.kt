package ci.nsu.mobile.main.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ci.nsu.mobile.main.data.local.entity.DepositEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Insert
    suspend fun insert(deposit: DepositEntity): Long

    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId ORDER BY calculationDate DESC")
    fun getDepositsForUser(userId: Long): Flow<List<DepositEntity>>

    @Query("SELECT * FROM deposit_calculations WHERE id = :id")
    suspend fun getDepositById(id: Long): DepositEntity?

    @Query("DELETE FROM deposit_calculations WHERE id = :id")
    suspend fun deleteById(id: Long)
}