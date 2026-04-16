package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.database.DepositDao
import ci.nsu.mobile.main.models.DepositCalculation
import kotlinx.coroutines.flow.Flow

class DepositRepository(private val depositDao: DepositDao) {

    fun getAllCalculations(): Flow<List<DepositCalculation>> =
        depositDao.getAllCalculations()

    suspend fun getCalculationById(id: Long): DepositCalculation? =
        depositDao.getCalculationById(id)

    suspend fun insertCalculation(calculation: DepositCalculation): Long =
        depositDao.insertCalculation(calculation)

    suspend fun deleteCalculation(calculation: DepositCalculation) =
        depositDao.deleteCalculation(calculation)

    fun calculateDeposit(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double?
    ): Pair<Double, Double> {
        val monthlyRate = interestRate / 100 / 12
        var finalAmount = initialAmount

        for (month in 1..periodMonths) {
            finalAmount += finalAmount * monthlyRate
            monthlyTopUp?.let { finalAmount += it }
        }

        val interestEarned = finalAmount - initialAmount - (monthlyTopUp ?: 0.0) * periodMonths
        return Pair(finalAmount, interestEarned)
    }

    companion object {
        fun getInterestRate(periodMonths: Int?): Double? {
            return when {
                periodMonths == null -> null
                periodMonths < 6 -> 15.0
                periodMonths < 12 -> 10.0
                else -> 5.0
            }
        }
    }
}