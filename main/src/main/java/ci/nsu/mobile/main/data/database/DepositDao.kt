package ci.nsu.mobile.main.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Insert
    suspend fun insert(calculation: DepositEntity)

    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    fun getAllCalculations(): Flow<List<DepositEntity>>

    @Query("DELETE FROM deposit_calculations")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(calculation: DepositEntity)
}