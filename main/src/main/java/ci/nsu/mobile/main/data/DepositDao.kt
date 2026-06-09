package ci.nsu.mobile.main.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Insert
    suspend fun insertCalculation(calculation: DepositCalculation)

    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    fun getAllCalculations(): Flow<List<DepositCalculation>>

    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC LIMIT 10")
    suspend fun getLastTenCalculations(): List<DepositCalculation>

    @Query("DELETE FROM deposit_calculations WHERE id = :id")
    suspend fun deleteCalculation(id: Long)

    @Query("DELETE FROM deposit_calculations")
    suspend fun deleteAllCalculations()
}