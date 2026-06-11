package ci.nsu.mobile.main.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {

    // Добавить новый расчёт в базу
    @Insert
    suspend fun insert(calculation: DepositCalculation)

    // Получить всю историю, новые записи будут сверху
    @Query("SELECT * FROM deposit_calculations ORDER BY createdAt DESC")
    fun getAllCalculations(): Flow<List<DepositCalculation>>

    // Удалить всю историю
    @Query("DELETE FROM deposit_calculations")
    suspend fun deleteAll()
}