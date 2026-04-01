package ci.nsu.mobile.main.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Insert
    suspend fun insert(deposit: DepositEntity)

    // Получаем расчёты ТОЛЬКО для конкретного пользователя,
    // отсортированные по дате (свежие сверху)
    @Query("SELECT * " +
            "FROM deposit_calculations " +
            "WHERE userLogin = :login " +
            "ORDER BY calculationDate DESC")
    fun getDepositsForUser(login: String): Flow<List<DepositEntity>>

    @Delete
    suspend fun delete(deposit: DepositEntity)
}