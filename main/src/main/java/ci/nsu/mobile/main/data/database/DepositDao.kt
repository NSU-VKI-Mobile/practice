package ci.nsu.mobile.main.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DepositDao {

    @Insert
    suspend fun insert(calculation: DepositCalculation): Long

    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId ORDER BY calculationDate DESC")
    suspend fun getCalculationsByUserId(userId: Long): List<DepositCalculation>

    @Query("SELECT * FROM deposit_calculations WHERE id = :id AND userId = :userId")
    suspend fun getCalculationById(id: Long, userId: Long): DepositCalculation?

    @Query("DELETE FROM deposit_calculations WHERE id = :id AND userId = :userId")
    suspend fun deleteCalculationById(id: Long, userId: Long): Int
}