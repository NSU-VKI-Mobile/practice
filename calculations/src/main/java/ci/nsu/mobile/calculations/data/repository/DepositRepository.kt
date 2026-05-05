package ci.nsu.mobile.calculations.data.repository

import ci.nsu.mobile.calculations.data.local.DepositDao
import ci.nsu.mobile.calculations.data.local.DepositEntity
import ci.nsu.mobile.domain.model.DepositCalculation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DepositRepository(private val depositDao: DepositDao) {
    fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>> {
        return depositDao.getDepositsForUser(userId)
            .map { entities -> entities.map { it.toDomain() } }
    }

    suspend fun saveCalculation(calculation: DepositCalculation) {
        depositDao.insert(DepositEntity.fromDomain(calculation))
    }

    suspend fun deleteCalculation(calculationId: Long) {
        depositDao.deleteById(calculationId)
    }
}
