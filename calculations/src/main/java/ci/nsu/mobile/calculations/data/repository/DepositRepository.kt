package ci.nsu.mobile.calculations.data.repository

import ci.nsu.mobile.calculations.data.dao.DepositDao
import ci.nsu.mobile.calculations.data.entity.DepositEntity
import ci.nsu.mobile.calculations.data.mapper.toDomain
import ci.nsu.mobile.domain.model.DepositCalculation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DepositRepository(private val dao: DepositDao) {

    fun getByUser(userId: Long): Flow<List<DepositCalculation>> =
        dao.getByUser(userId).map { list -> list.map { it.toDomain() } }

    suspend fun save(entity: DepositEntity) = dao.insert(entity)

    suspend fun delete(entity: DepositEntity) = dao.delete(entity)
}