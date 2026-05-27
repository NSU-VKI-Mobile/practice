package ci.nsu.mobile.main.calculations.data.repository

import ci.nsu.mobile.main.auth.data.datasource.local.TokenManager
import ci.nsu.mobile.main.calculations.data.database.DepositCalculationEntity
import ci.nsu.mobile.main.calculations.data.database.DepositDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DepositRepository @Inject constructor(
    private val depositDao: DepositDao,
    private val tokenManager: TokenManager
) {

    suspend fun saveDeposit(deposit: DepositCalculationEntity) {
        depositDao.insert(deposit)
    }

    suspend fun saveDepositForCurrentUser(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double,
        finalAmount: Double,
        interestEarned: Double
    ) {
        val userId = tokenManager.userId
            ?: throw IllegalStateException("No logged in user")

        val deposit = DepositCalculationEntity(
            userId = userId,
            initialAmount = initialAmount,
            periodMonths = periodMonths,
            interestRate = interestRate,
            monthlyTopUp = monthlyTopUp,
            finalAmount = finalAmount,
            interestEarned = interestEarned,
            calculationDate = System.currentTimeMillis()
        )
        depositDao.insert(deposit)
    }

    suspend fun updateDeposit(deposit: DepositCalculationEntity) {
        depositDao.update(deposit)
    }

    suspend fun deleteDeposit(deposit: DepositCalculationEntity) {
        depositDao.delete(deposit)
    }

    suspend fun deleteDepositById(id: Long) {
        depositDao.deleteById(id)
    }

    suspend fun deleteAllForUser(userId: Long) {
        depositDao.deleteAllForUser(userId)
    }

    fun getAllDeposits(): Flow<List<DepositCalculationEntity>> {
        return depositDao.getAllCalculations()
    }

    fun getDepositsForUser(userId: Long): Flow<List<DepositCalculationEntity>> {
        return depositDao.getCalculationsForUser(userId)
    }

    fun getDepositsForCurrentUser(): Flow<List<DepositCalculationEntity>> {
        val userId = tokenManager.userId
            ?: throw IllegalStateException("No logged in user")
        return depositDao.getCalculationsForUser(userId)
    }

    suspend fun getDepositById(id: Long): DepositCalculationEntity? {
        return depositDao.getCalculationById(id)
    }
}