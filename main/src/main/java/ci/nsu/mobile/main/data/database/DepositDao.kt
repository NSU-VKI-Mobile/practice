package ci.nsu.mobile.main.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DepositDao {

    @Insert
    fun insert(calculation: DepositCalculation): Long

    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    fun getAllCalculations(): List<DepositCalculation>

    @Query("SELECT * FROM deposit_calculations WHERE id = :id")
    fun getCalculationById(id: Long): DepositCalculation?
}