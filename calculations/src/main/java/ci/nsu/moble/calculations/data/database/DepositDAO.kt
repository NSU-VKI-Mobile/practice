package ci.nsu.moble.calculations.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deposit: DepositEntity)

    @Query("SELECT * FROM deposits WHERE userId = :userId ORDER BY calculationDate DESC")
    fun getDepositsByUserId(userId: Long): Flow<List<DepositEntity>>

    @Delete
    suspend fun delete(deposit: DepositEntity)

    @Query("DELETE FROM deposits WHERE id = :id")
    suspend fun deleteById(id: Long)
}