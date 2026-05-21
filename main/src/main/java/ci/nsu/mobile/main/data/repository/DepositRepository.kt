package ci.nsu.mobile.main.data.repository

import ci.nsu.mobile.main.TokenManager
import ci.nsu.mobile.main.data.local.dao.DepositDao
import ci.nsu.mobile.main.data.local.entities.DepositCalculation
import kotlinx.coroutines.flow.Flow

interface DepositRepository {
    suspend fun saveCalculation(calculation: DepositCalculation)
    fun getMyCalculations(): Flow<List<DepositCalculation>>
    suspend fun getCalculationById(id: Long): DepositCalculation?
    suspend fun deleteCalculation(calculation: DepositCalculation)
    suspend fun deleteAllMyCalculations()
}

class DepositRepositoryImpl(
    private val depositDao: DepositDao
) : DepositRepository {

    override suspend fun saveCalculation(calculation: DepositCalculation) {
        val userId = TokenManager.userId ?: throw IllegalStateException("User not authenticated")
        val calculationWithUser = calculation.copy(userId = userId)
        depositDao.insert(calculationWithUser)
    }

    override fun getMyCalculations(): Flow<List<DepositCalculation>> {
        val userId = TokenManager.userId ?: throw IllegalStateException("User not authenticated")
        return depositDao.getCalculationsForUser(userId)
    }

    override suspend fun getCalculationById(id: Long): DepositCalculation? {
        val userId = TokenManager.userId ?: return null
        return depositDao.getCalculationById(id, userId)
    }

    override suspend fun deleteCalculation(calculation: DepositCalculation) {
        depositDao.delete(calculation)
    }

    override suspend fun deleteAllMyCalculations() {
        val userId = TokenManager.userId ?: return
        depositDao.deleteAllForUser(userId)
    }
}