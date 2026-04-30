package ci.nsu.moble.main.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ci.nsu.moble.main.data.entities.DepositRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Insert
    suspend fun insert(record: DepositRecordEntity)
    @Query("SELECT * FROM history ORDER BY date DESC")
    fun getAll(): Flow<List<DepositRecordEntity>>
    @Query("DELETE FROM history")
    suspend fun deleteAll()
    @Delete
    suspend fun delete(record: DepositRecordEntity)

}