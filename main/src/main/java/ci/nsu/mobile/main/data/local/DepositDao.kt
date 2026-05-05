package ci.nsu.mobile.main.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {

    @Insert
    suspend fun insert(entity: DepositEntity)

    @Query("SELECT * FROM deposit_calculations ORDER BY date DESC")
    fun getAll(): Flow<List<DepositEntity>>
}