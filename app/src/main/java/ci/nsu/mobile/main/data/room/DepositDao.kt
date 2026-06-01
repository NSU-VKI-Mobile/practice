package ci.nsu.mobile.main.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DepositDao {
    @Insert
    suspend fun InsertDeposit(deposit: DepositCalculationEntity)

    @Query("select * from deposit_calculations where userId=:userId order by calculationDate desc")
    fun GetAll(userId: Long): Flow<List<DepositCalculationEntity>>

    @Query("select * from deposit_calculations where initialAmount=:initialAmount and periodMonths=:periodMonths and interestRate=:interestRate  AND ((monthlyTopUp IS NULL AND :monthlyTopUp IS NULL) OR monthlyTopUp = :monthlyTopUp) LIMIT 1")
    suspend fun findDuplication(initialAmount: Double, periodMonths: Int, interestRate: Int, monthlyTopUp: Double?): DepositCalculationEntity?

    @Delete
    suspend fun deleteDeposit(deposit: DepositCalculationEntity)
}