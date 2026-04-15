package ci.nsu.mobile.main

import androidx.room.*
import kotlinx.coroutines.flow.Flow
//доступ к данным
@Dao
interface CalculationDao {
    // Получить все расчёты, отсортированные по дате (новые сверху)
    @Query("SELECT * FROM calculations ORDER BY date DESC")
    fun getAllCalculations(): Flow<List<Calculation>>

    @Query("SELECT * FROM calculations ORDER BY date DESC")
    suspend fun getAllCalculationsList(): List<Calculation>
    //расчеты по айди
    @Query("SELECT * FROM calculations WHERE id = :id")
    suspend fun getCalculationById(id: Long): Calculation?
    //доб новый расчет
    @Insert
    suspend fun insertCalculation(calculation: Calculation)
    // удалить расчет
    @Delete
    suspend fun deleteCalculation(calculation: Calculation)
    // удалить все расчеты
    @Query("DELETE FROM calculations")
    suspend fun deleteAllCalculations()
}