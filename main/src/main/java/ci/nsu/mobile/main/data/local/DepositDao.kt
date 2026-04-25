package ci.nsu.mobile.main.data.local

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
}