package ci.nsu.mobile.calculations

import ci.nsu.mobile.calculations.data.dao.DepositDao
import ci.nsu.mobile.calculations.data.mapper.toDomain
import ci.nsu.mobile.calculations.data.mapper.toEntity
import ci.nsu.mobile.domain.interfaces.CalculationsProvider
import ci.nsu.mobile.domain.model.DepositCalculation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CalculationsProviderImpl(private val dao: DepositDao) : CalculationsProvider {

    override fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>> =
        dao.getByUser(userId).map { list -> list.map { it.toDomain() } }

    override suspend fun saveCalculation(calculation: DepositCalculation) {
        dao.insert(calculation.toEntity())
    }

    override suspend fun deleteCalculation(calculationId: Long) {
        dao.deleteById(calculationId)
    }
}