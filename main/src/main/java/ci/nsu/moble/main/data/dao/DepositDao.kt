package ci.nsu.moble.main.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ci.nsu.moble.main.data.entities.DepositCalculationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Insert
    suspend fun insert(record: DepositCalculationEntity)
    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    fun getAll(): Flow<List<DepositCalculationEntity>>
    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId ORDER BY calculationDate DESC")
    fun getCalculationsByUserId(userId: Long): Flow<List<DepositCalculationEntity>>
    @Query("DELETE FROM deposit_calculations")
    suspend fun deleteAll()
    @Delete
    suspend fun delete(record: DepositCalculationEntity)
    @Query("DELETE FROM deposit_calculations WHERE userId = :userId")
    suspend fun deleteAllByUserId(userId: Long)
}