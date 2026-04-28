package ci.nsu.moble.main.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {

    // Сохранить расчёт в базу (или заменить, если такой уже есть)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deposit: DepositCalculation)

    // Получить все расчёты, отсортированные по дате (новые сверху)
    @Query("SELECT * FROM deposits ORDER BY calculationDate DESC")
    fun getAll(): Flow<List<DepositCalculation>>

    // Получить один расчёт по ID
    @Query("SELECT * FROM deposits WHERE id = :id")
    fun getById(id: Long): Flow<DepositCalculation?>
}