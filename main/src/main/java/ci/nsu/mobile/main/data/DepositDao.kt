package ci.nsu.mobile.main.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Insert
    suspend fun insert(deposit: DepositEntity)

    // Flow позволяет UI автоматически обновляться при добавлении новых записей
    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    fun getAllDeposits(): Flow<List<DepositEntity>>
}