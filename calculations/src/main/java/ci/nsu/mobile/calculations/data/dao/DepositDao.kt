package ci.nsu.mobile.calculations.data.dao

import androidx.room.*
import ci.nsu.mobile.calculations.data.entity.DepositEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {

    @Insert
    suspend fun insert(deposit: DepositEntity)

    @Delete
    suspend fun delete(deposit: DepositEntity)

    @Query("DELETE FROM deposits WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT * FROM deposits WHERE userId = :userId ORDER BY date DESC")
    fun getByUser(userId: Long): Flow<List<DepositEntity>>
}