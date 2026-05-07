package ci.nsu.mobile.main.data.database

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
}