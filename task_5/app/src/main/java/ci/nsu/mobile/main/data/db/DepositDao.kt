package ci.nsu.mobile.main.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DepositDao {

    @Insert
    suspend fun insertDeposit(deposit: DepositEntity)

    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    suspend fun getAllDeposits(): List<DepositEntity>

    @Query("SELECT * FROM deposit_calculations WHERE id = :id")
    suspend fun getDepositById(id: Long): DepositEntity?
}