package ci.nsu.mobile.calculations.data.repository

import ci.nsu.mobile.calculations.data.local.dao.DepositDao
import ci.nsu.mobile.calculations.data.mapper.toDomain
import ci.nsu.mobile.calculations.data.mapper.toEntity
import ci.nsu.mobile.domain.models.DepositCalculation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DepositRepository(private val depositDao: DepositDao) {

    fun getHistory(userId: Int): Flow<List<DepositCalculation>> =
        depositDao.getHistoryForUser(userId).map { list ->
            list.map { it.toDomain() }
        }

    suspend fun insert(calculation: DepositCalculation) {
        depositDao.insert(calculation.toEntity())
    }

    suspend fun delete(id: Long) {
        depositDao.delete(id)
    }
}