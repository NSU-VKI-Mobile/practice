package ci.nsu.mobile.main.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DepositDao {
    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    suspend fun getAllCalculations(): List<DepositCalculation>

    @Insert
    suspend fun insert(calculation: DepositCalculation)

    @Query("DELETE FROM deposit_calculations")
    suspend fun deleteAll()
}