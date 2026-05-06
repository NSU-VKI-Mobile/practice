package ci.nsu.mobile.main.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(calculation: DepositCalculation)

    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    fun getAllHistory(): Flow<List<DepositCalculation>>
}