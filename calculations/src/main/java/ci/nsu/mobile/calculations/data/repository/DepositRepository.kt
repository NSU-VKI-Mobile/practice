package ci.nsu.mobile.calculations.data.repository

import ci.nsu.mobile.calculations.data.local.dao.DepositDao
import ci.nsu.mobile.calculations.data.local.model.DepositCalculation
import ci.nsu.mobile.domain.CalculationsProvider
import ci.nsu.mobile.domain.DepositCalculationDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DepositRepository(private val dao: DepositDao) : CalculationsProvider {

    override fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculationDomain>> {
        return dao.getByUserId(userId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun saveCalculation(calculation: DepositCalculationDomain) {
        dao.insert(calculation.toEntity())
    }

    override suspend fun deleteCalculation(calculationId: Long) {
        dao.deleteById(calculationId)
    }

    private fun DepositCalculation.toDomain() = DepositCalculationDomain(
        id = id, userId = userId, initialAmount = initialAmount,
        periodMonths = periodMonths, interestRate = interestRate,
        monthlyTopUp = monthlyTopUp, finalAmount = finalAmount,
        interestEarned = interestEarned, calculationDate = calculationDate
    )

    private fun DepositCalculationDomain.toEntity() = DepositCalculation(
        id = id, userId = userId, initialAmount = initialAmount,
        periodMonths = periodMonths, interestRate = interestRate,
        monthlyTopUp = monthlyTopUp, finalAmount = finalAmount,
        interestEarned = interestEarned, calculationDate = calculationDate
    )
}