package ci.nsu.mobile.calculations.di

import ci.nsu.mobile.calculations.repository.DepositRepository
import ci.nsu.mobile.domain.interfaces.CalculationsProvider
import ci.nsu.mobile.domain.models.DepositCalculation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class CalculationsProviderImpl(
    private val repository: DepositRepository
) : CalculationsProvider {

    override fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>> {
        return flow {
            val roomCalculations = repository.getCalculationsByUserId(userId)
            emit(roomCalculations)
        }.map { roomList ->
            roomList.map { roomCalc ->
                DepositCalculation(
                    id = roomCalc.id,
                    userId = roomCalc.userId,
                    initialAmount = roomCalc.initialAmount,
                    periodMonths = roomCalc.periodMonths,
                    interestRate = roomCalc.interestRate,
                    monthlyTopUp = roomCalc.monthlyTopUp,
                    finalAmount = roomCalc.finalAmount,
                    interestEarned = roomCalc.interestEarned,
                    calculationDate = roomCalc.calculationDate
                )
            }
        }
    }

    override suspend fun saveCalculation(calculation: DepositCalculation) {
        val roomCalculation = ci.nsu.mobile.calculations.data.DepositCalculation(
            id = calculation.id,
            userId = calculation.userId,
            initialAmount = calculation.initialAmount,
            periodMonths = calculation.periodMonths,
            interestRate = calculation.interestRate,
            monthlyTopUp = calculation.monthlyTopUp,
            finalAmount = calculation.finalAmount,
            interestEarned = calculation.interestEarned,
            calculationDate = calculation.calculationDate
        )
        repository.saveCalculation(roomCalculation)
    }

    override suspend fun deleteCalculation(calculationId: Long) {
        val userId = 1L // заглушка
        repository.deleteCalculationById(calculationId, userId)
    }
}