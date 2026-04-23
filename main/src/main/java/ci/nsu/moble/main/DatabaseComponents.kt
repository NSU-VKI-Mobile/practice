package ci.nsu.moble.main

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "deposit_calculations")
data class DepositCalculation(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double?,
    val finalAmount: Double,
    val interestEarned: Double,
    val calculationDate: Long = System.currentTimeMillis()
)

@Dao
interface DepositDao {
    @Query("SELECT * FROM deposit_calculations ORDER BY calculationDate DESC")
    fun getAllCalculations(): Flow<List<DepositCalculation>>

    @Query("SELECT * FROM deposit_calculations WHERE id = :id")
    suspend fun getCalculationById(id: Long): DepositCalculation?

    @Insert
    suspend fun insertCalculation(calculation: DepositCalculation)
}