package ci.nsu.mobile.main.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {

    @Insert
    suspend fun insert(deposit: DepositEntity)

    @Query("SELECT * FROM deposit_calculations ORDER BY id DESC")
    fun getAll(): Flow<List<DepositEntity>>


    @Delete
    suspend fun delete(deposit: DepositEntity)

    @Query("DELETE FROM deposit_calculations")
    suspend fun deleteAll()
}