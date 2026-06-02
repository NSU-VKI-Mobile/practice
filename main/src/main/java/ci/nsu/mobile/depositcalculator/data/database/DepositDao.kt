package ci.nsu.mobile.depositcalculator.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Insert
    suspend fun insert(calculation: DepositCalculationEntity)

    // Получить все расчеты (в порядке от новых к старым)
    @Query("SELECT * FROM deposit_calculations ORDER BY timestamp DESC")
    fun getAllCalculations(): Flow<List<DepositCalculationEntity>>

    // Получить последние 10 расчетов
    @Query("SELECT * FROM deposit_calculations ORDER BY timestamp DESC LIMIT 10")
    fun getLastTenCalculations(): Flow<List<DepositCalculationEntity>>

    // Удалить расчет
    @Delete
    suspend fun delete(calculation: DepositCalculationEntity)

    // Удалить все расчеты
    @Query("DELETE FROM deposit_calculations")
    suspend fun deleteAll()

    // Получить расчет по ID
    @Query("SELECT * FROM deposit_calculations WHERE id = :id")
    suspend fun getCalculationById(id: Long): DepositCalculationEntity?

}