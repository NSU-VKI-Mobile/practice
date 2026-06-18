package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.data.local.dao.DepositDao
import ci.nsu.mobile.main.data.local.entity.DepositEntity
import ci.nsu.mobile.main.domain.model.DepositCalculation
import ci.nsu.mobile.main.domain.repository.DepositRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DepositRepositoryImpl(
    private val depositDao: DepositDao
) : DepositRepository {

    override suspend fun saveCalculation(userId: Long, calculation: DepositCalculation): Long {
        val entity = DepositEntity(
            id = calculation.id,
            userId = userId,
            initialAmount = calculation.initialAmount,
            periodMonths = calculation.periodMonths,
            interestRate = calculation.interestRate,
            monthlyTopUp = calculation.monthlyTopUp,
            finalAmount = calculation.finalAmount,
            interestEarned = calculation.interestEarned,
            calculationDate = calculation.calculationDate
        )
        return depositDao.insert(entity)
    }

    override fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>> {
        return depositDao.getDepositsForUser(userId).map { entities ->
            entities.map { entity ->
                DepositCalculation(
                    id = entity.id,
                    userId = entity.userId,
                    initialAmount = entity.initialAmount,
                    periodMonths = entity.periodMonths,
                    interestRate = entity.interestRate,
                    monthlyTopUp = entity.monthlyTopUp,
                    finalAmount = entity.finalAmount,
                    interestEarned = entity.interestEarned,
                    calculationDate = entity.calculationDate
                )
            }
        }
    }

    override suspend fun getCalculationById(id: Long): DepositCalculation? {
        return depositDao.getDepositById(id)?.let { entity ->
            DepositCalculation(
                id = entity.id,
                userId = entity.userId,
                initialAmount = entity.initialAmount,
                periodMonths = entity.periodMonths,
                interestRate = entity.interestRate,
                monthlyTopUp = entity.monthlyTopUp,
                finalAmount = entity.finalAmount,
                interestEarned = entity.interestEarned,
                calculationDate = entity.calculationDate
            )
        }
    }

    override suspend fun deleteCalculation(id: Long) {
        depositDao.deleteById(id)
    }
}