package ci.nsu.mobile.main.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ci.nsu.mobile.main.entity.DepositEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {

    @Insert
    suspend fun insert(deposit: DepositEntity)

    @Delete
    suspend fun delete(deposit: DepositEntity)

    @Query("SELECT * FROM deposits WHERE userId = :userId ORDER BY date DESC")
    fun getByUser(userId: Long): Flow<List<DepositEntity>>
}