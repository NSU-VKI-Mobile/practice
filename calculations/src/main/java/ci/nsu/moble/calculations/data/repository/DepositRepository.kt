package ci.nsu.moble.calculations.data.repository

import ci.nsu.moble.calculations.data.database.DepositDao
import ci.nsu.moble.calculations.data.database.DepositEntity
import ci.nsu.moble.domain.models.DepositCalculation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DepositRepository(private val dao: DepositDao) {
    fun getDeposits(userId: Long): Flow<List<DepositCalculation>> {
        return dao.getDepositsByUserId(userId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun saveDeposit(calculation: DepositCalculation) {
        dao.insert(DepositEntity.fromDomain(calculation))
    }

    suspend fun deleteDeposit(calculationId: Long) {
        dao.insert(DepositEntity(id = calculationId, userId = 0, initialAmount = 0.0, months = 0, rate = 0.0, monthlyTopUp = null, finalAmount = 0.0, profit = 0.0, calculationDate = java.util.Date()))
    }
}