package ci.nsu.mobile.main.dao;

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ci.nsu.mobile.main.entity.DepositEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Insert
    suspend fun insert(deposit: DepositEntity)

    @Query("SELECT * FROM deposits ORDER BY date DESC")
    fun getAll(): Flow<List<DepositEntity>>
}