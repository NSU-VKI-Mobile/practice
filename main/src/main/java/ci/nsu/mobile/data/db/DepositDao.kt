package ci.nsu.mobile.data.db

import androidx.room.*

@Dao
interface DepositDao {
    @Query("SELECT * FROM deposit_calculations WHERE userLogin = :login ORDER BY calculationDate DESC")
    suspend fun getHistory(login: String): List<DepositCalculation>

    @Insert
    suspend fun insert(item: DepositCalculation)

    @Delete
    suspend fun delete(item: DepositCalculation)

    @Query("DELETE FROM deposit_calculations")
    suspend fun clearAll()
}