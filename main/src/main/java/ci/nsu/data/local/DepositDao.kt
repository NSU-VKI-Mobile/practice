package ci.nsu.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ci.nsu.data.model.DepositCalculation // Важный импорт!
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {

    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId ORDER BY calculationDate DESC")
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalculation(calculation: DepositCalculation)

    @Delete
    suspend fun deleteCalculation(calculation: DepositCalculation)
}
