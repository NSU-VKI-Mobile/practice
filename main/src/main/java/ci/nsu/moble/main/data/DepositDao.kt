package ci.nsu.moble.main.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {

    @Insert
    suspend fun insert(item: DepositCalculationEntity)

    @Query("SELECT * FROM deposit_calculations ORDER BY id DESC")
    fun getAll(): Flow<List<DepositCalculationEntity>>

    @Query("SELECT * FROM deposit_calculations WHERE id = :id")
    suspend fun getById(id: Long): DepositCalculationEntity
}