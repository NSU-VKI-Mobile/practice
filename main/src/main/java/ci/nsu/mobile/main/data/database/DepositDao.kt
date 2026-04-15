package ci.nsu.mobile.main.data.database

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

    @Query("SELECT * FROM deposit_calculations WHERE id = :id")
    suspend fun getCalculationById(id: Long): DepositCalculationEntity?

    @Query("DELETE FROM deposit_calculations WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM deposit_calculations")
    suspend fun deleteAll()
}