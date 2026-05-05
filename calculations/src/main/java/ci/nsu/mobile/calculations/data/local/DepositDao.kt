package ci.nsu.mobile.calculations.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Insert
    suspend fun insert(deposit: DepositEntity)

    // Получаем расчёты только для конкретного пользователя.
    @Query("SELECT * " +
            "FROM deposit_calculations " +
            "WHERE userId = :userId " +
            "ORDER BY calculationDate DESC")
    fun getDepositsForUser(userId: Long): Flow<List<DepositEntity>>

    @Delete
    suspend fun delete(deposit: DepositEntity)

    @Query("DELETE FROM deposit_calculations WHERE id = :calculationId")
    suspend fun deleteById(calculationId: Long)
}
