package ci.nsu.mobile.main.data.roomDatabase

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface DepositDao {
    @Query("SELECT * FROM deposits ORDER BY id DESC")
    fun getAll(): Flow<List<DepositEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(calculation: DepositEntity)

    @Query("DELETE FROM deposits")
    suspend fun deleteAll()

    @Query("SELECT * FROM deposits WHERE userId = :userId")
    fun getDepositsByUserId(userId: Int): Flow<List<DepositEntity>>

    @Query("DELETE FROM deposits WHERE userId = :userId")
    suspend fun deleteDepositsByUserId(userId: Int)
}