package ci.nsu.mobile.calculations.data.repository

import ci.nsu.mobile.calculations.data.database.DepositCalculationEntity
import ci.nsu.mobile.calculations.data.database.DepositDao
import ci.nsu.mobile.domain.auth.AuthManager
import ci.nsu.mobile.domain.calculations.CalculationsProvider
import ci.nsu.mobile.domain.calculations.DepositCalculation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DepositRepository @Inject constructor(
    private val depositDao: DepositDao,
    private val authManager: AuthManager
) : CalculationsProvider {

    override fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>> {
        return depositDao.getCalculationsForUser(userId).map { entities ->
            entities.map { it.toDomain() }  // Entity → Domain
        }
    }

    override suspend fun saveCalculation(calculation: DepositCalculation) {
        val entity = DepositCalculationEntity.fromDomain(calculation)  // Domain → Entity
        depositDao.insert(entity)
    }

    override suspend fun deleteCalculation(calculationId: Long) {
        depositDao.deleteById(calculationId)
    }

    // для текущего пользователя
    override fun getCalculationsForCurrentUser(): Flow<List<DepositCalculation>> {
        val userId = authManager.getCurrentUserId()
            ?: throw IllegalStateException("No logged in user")
        return getCalculationsForUser(userId)
    }

    // без передачи userId
    override suspend fun saveCalculationForCurrentUser(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double,
        finalAmount: Double,
        interestEarned: Double
    ) {
        val userId = authManager.getCurrentUserId()
            ?: throw IllegalStateException("No logged in user")

        val calculation = DepositCalculation(
            userId = userId,
            initialAmount = initialAmount,
            periodMonths = periodMonths,
            interestRate = interestRate,
            monthlyTopUp = monthlyTopUp,
            finalAmount = finalAmount,
            interestEarned = interestEarned,
            calculationDate = System.currentTimeMillis()
        )
        saveCalculation(calculation)
    }
}