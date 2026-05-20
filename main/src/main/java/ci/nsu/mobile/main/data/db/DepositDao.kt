package ci.nsu.mobile.main.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete

@Dao
interface DepositDao {

    @Insert
    suspend fun insertDeposit(deposit: DepositEntity)

    @Query("SELECT * FROM deposits ORDER BY calculationDate DESC")
    suspend fun getAllDeposits(): List<DepositEntity>

    @Delete
    suspend fun deleteDeposit(deposit: DepositEntity)
}