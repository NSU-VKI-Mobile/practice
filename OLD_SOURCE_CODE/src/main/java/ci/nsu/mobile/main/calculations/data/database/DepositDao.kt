package ci.nsu.mobile.main.calculations.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(calculation: DepositCalculationEntity)

    @Update
    suspend fun update(calculation: DepositCalculationEntity)

    @Delete
    suspend fun delete(calculation: DepositCalculationEntity)

    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    fun getAllCalculations(): Flow<List<DepositCalculationEntity>>

    //calculations only  specific user
    @Query("SELECT * FROM deposit_calculations WHERE userId = :userId ORDER BY calculationDate DESC")
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculationEntity>>

    @Query("SELECT * FROM deposit_calculations WHERE id = :id")
    suspend fun getCalculationById(id: Long): DepositCalculationEntity?

    @Query("DELETE FROM deposit_calculations WHERE id = :id")
    suspend fun deleteById(id: Long)

    // delete all calculations for a specific user (debug)
    @Query("DELETE FROM deposit_calculations WHERE userId = :userId")
    suspend fun deleteAllForUser(userId: Long)

    @Query("DELETE FROM deposit_calculations")
    suspend fun deleteAll()
}