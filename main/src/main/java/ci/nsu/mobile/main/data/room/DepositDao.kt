package ci.nsu.mobile.main.data.room

import androidx.room3.Dao
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Query

@Dao
interface DepositDao {
    @Insert
    suspend fun InsertDeposit(deposit: DepositCalculationEntity)

    @Query("select * from deposit_calculations order by calculationDate desc")
    fun GetAll(): Flow<List<DepositCalculationEntity>>

    @Query("select * from deposit_calculations where initialAmount=:initialAmount and periodMonths=:periodMonths and interestRate=:interestRate  AND ((monthlyTopUp IS NULL AND :monthlyTopUp IS NULL) OR monthlyTopUp = :monthlyTopUp) LIMIT 1")
    suspend fun findDuplication(initialAmount: Double, periodMonths: Int, interestRate: Int, monthlyTopUp: Double?): DepositCalculationEntity?
}