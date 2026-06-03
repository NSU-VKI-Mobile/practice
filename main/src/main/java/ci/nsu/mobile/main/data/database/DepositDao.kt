package ci.nsu.mobile.mai.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    fun getAllCalculations(): Flow<List<DepositCalculation>>

    @Insert
    suspend fun insertCalculation(calculation: DepositCalculation)

    @Query("SELECT * FROM deposit_calculations WHERE id = :id")
    suspend fun getCalculationById(id: Long): DepositCalculation?
}