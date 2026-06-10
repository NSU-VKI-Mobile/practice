package ci.nsu.mobile.main.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface DepositDao {
    @Query("SELECT * FROM deposit_calculations ORDER BY id DESC")
    fun getAll(): Flow<List<DepositEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(calculation: DepositEntity)

    @Delete
    suspend fun delete(deposit: DepositEntity)
    @Query("DELETE FROM deposit_calculations")
    suspend fun deleteAll()

    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId")
    fun getDepositsByUserId(userId: Int): Flow<List<DepositEntity>>

    @Query("DELETE FROM deposit_calculations WHERE userId = :userId")
    suspend fun deleteDepositsByUserId(userId: Int)

}