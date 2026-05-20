package ci.nsu.moble.main

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {

    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    fun getAll(): Flow<List<DepositEntity>>

    @Insert
    suspend fun insert(entity: DepositEntity)

    @Delete
    suspend fun delete(entity: DepositEntity)
}