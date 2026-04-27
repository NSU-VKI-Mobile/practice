package ci.nsu.mobile.main.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    //Вставляет новый расчёт в базу данных
    @Insert
    suspend fun insert(calculation: DepositCalculation)
    //--------------------------------------------------

    //Возвращает все расчёты из таблицы
    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    fun getAllCalculations(): Flow<List<DepositCalculation>>
//-----------------------------------------
    //Ищет расчёт по ID
    @Query("SELECT * FROM deposit_calculations WHERE id = :id")
    suspend fun getCalculationById(id: Long): DepositCalculation?
}