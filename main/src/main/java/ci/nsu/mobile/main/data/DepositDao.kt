package ci.nsu.mobile.main.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {

    @Insert
    suspend fun insert(deposit: Deposit)

    @Query("SELECT * FROM deposits ORDER BY id DESC")
    fun getAllDeposits(): Flow<List<Deposit>>

    @Query("SELECT * FROM deposits WHERE id = :id LIMIT 1")
    fun getDepositById(id: Int): Flow<Deposit?>

    // ДОБАВЬ ЭТО:
    @Query("DELETE FROM deposits")
    suspend fun deleteAll()
}