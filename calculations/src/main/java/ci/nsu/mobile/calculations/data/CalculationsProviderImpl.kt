package ci.nsu.mobile.calculations.data

import ci.nsu.mobile.domain.calculations.CalculationsProvider
import ci.nsu.mobile.domain.model.DepositCalculation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CalculationsProviderImpl(private val dao: DepositDao) : CalculationsProvider {

    override fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>> {
        return dao.getCalculationsByUser(userId).map { list ->
            list.map { it.toDomainModel() }
        }
    }

    override suspend fun saveCalculation(calculation: DepositCalculation) {
        dao.insert(calculation.toEntity())
    }

    override suspend fun deleteCalculation(id: Long) {
        dao.deleteById(id)
    }

    override fun calculateDeposit(
        amount: Double,
        months: Int,
        rate: Double,
        topUp: Double,
        userId: Long
    ): DepositCalculation {
        var total = amount
        var earned = 0.0
        val monthlyRate = rate / 100 / 12

        for (i in 1..months) {
            val interest = total * monthlyRate
            earned += interest
            total += interest + topUp
        }

        return DepositCalculation(
            userId = userId,
            initialAmount = amount,
            periodMonths = months,
            interestRate = rate,
            monthlyTopUp = topUp,
            finalAmount = total,
            interestEarned = earned,
            calculationDate = System.currentTimeMillis()
        )
    }
}