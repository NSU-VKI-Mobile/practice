package ci.nsu.mobile.main.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ci.nsu.mobile.main.data.model.DepositCalculation
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Insert
    suspend fun InsertDeposit(deposit: DepositCalculation)

    @Query("select * from deposit_calculations order by calculationDate desc")
    fun GetAll(): Flow<List<DepositCalculation>>

    @Query("select * from deposit_calculations where id = :id")
    fun GetById(id: Long): Flow<DepositCalculation>
}