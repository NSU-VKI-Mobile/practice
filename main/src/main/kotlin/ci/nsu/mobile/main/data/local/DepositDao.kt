package ci.nsu.mobile.main.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DepositDao {
    @Insert
    suspend fun insert(deposit: DepositCalculation)

    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    fun getAllDeposits(): LiveData<List<DepositCalculation>>

    @Query("SELECT * FROM deposit_calculations WHERE id = :id")
    suspend fun getDepositById(id: Long): DepositCalculation?
}