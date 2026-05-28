package ci.nsu.mobile.calculations.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DepositDao {

    @Insert
    suspend fun insert(calculation: DepositCalculation): Long

    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    suspend fun getAllCalculations(): List<DepositCalculation>

    @Query("SELECT * FROM deposit_calculations WHERE id = :id")
    suspend fun getCalculationById(id: Long): DepositCalculation?

    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId ORDER BY calculationDate DESC")
    suspend fun getCalculationsByUserId(userId: Long): List<DepositCalculation>

    @Query("DELETE FROM deposit_calculations WHERE id = :id AND userId = :userId")
    suspend fun deleteCalculationById(id: Long, userId: Long): Int
}