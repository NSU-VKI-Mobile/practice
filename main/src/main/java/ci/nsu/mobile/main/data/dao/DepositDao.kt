package ci.nsu.mobile.main.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ci.nsu.mobile.main.DepositCalculation
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {

    @Insert
    suspend fun insert(calc: DepositCalculation)

    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    fun getAll(): Flow<List<DepositCalculation>>

    @Query("SELECT * FROM deposit_calculations WHERE id=:id")
    suspend fun getById(id: Long): DepositCalculation
}