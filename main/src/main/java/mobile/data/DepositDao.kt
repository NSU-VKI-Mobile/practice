package mobile.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    // Получить всю историю (по убыванию даты, чтобы новые были сверху)
    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    fun getAllCalculations(): Flow<List<DepositCalculation>>

    // Сохранить новый расчет
    @Insert
    suspend fun insertCalculation(calculation: DepositCalculation)
}