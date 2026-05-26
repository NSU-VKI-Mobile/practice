package ci.nsu.mobile.main.Data.Database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DepositDao {
    @Insert
    suspend fun insert(calculation: DepositCalculation)

    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId ORDER BY calculationDate DESC")
    fun getAllDepositsForUser(userId: Long): LiveData<List<DepositCalculation>>

    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    fun getAllDeposits(): LiveData<List<DepositCalculation>>  // Оставляем для обратной совместимости
}