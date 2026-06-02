package ci.nsu.moble.calculations.provider

import ci.nsu.moble.calculations.data.repository.DepositRepository
import ci.nsu.moble.domain.interfaces.CalculationsProvider
import ci.nsu.moble.domain.models.DepositCalculation
import kotlinx.coroutines.flow.Flow

class CalculationsProviderImpl(
    private val repository: DepositRepository
) : CalculationsProvider {
    override fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>> {
        return repository.getDeposits(userId)
    }

    override suspend fun saveCalculation(calculation: DepositCalculation) {
        repository.saveDeposit(calculation)
    }

    override suspend fun deleteCalculation(calculationId: Long) {
        repository.deleteDeposit(calculationId)
    }
}