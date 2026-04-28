package ci.nsu.moble.main.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {

    @Insert
    suspend fun insert(deposit: DepositCalculation)

    @Query("SELECT * FROM deposits ORDER BY date DESC")
    fun getAll(): Flow<List<DepositCalculation>>

    @Query("SELECT * FROM deposits WHERE id = :id")
    suspend fun getById(id: Long): DepositCalculation?

    @Query("DELETE FROM deposits WHERE id = :id")
    suspend fun deleteById(id: Long)
}