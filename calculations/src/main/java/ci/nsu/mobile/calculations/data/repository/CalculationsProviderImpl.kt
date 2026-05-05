package ci.nsu.mobile.calculations.data.repository

import ci.nsu.mobile.domain.calculations.CalculationsProvider
import ci.nsu.mobile.domain.model.DepositCalculation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CalculationsProviderImpl(
    private val repository: DepositRepository
) : CalculationsProvider {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>> {
        return repository.getCalculationsForUser(userId)
    }

    override fun saveCalculation(calculation: DepositCalculation) {
        scope.launch { repository.saveCalculation(calculation) }
    }

    override fun deleteCalculation(calculationId: Long) {
        scope.launch { repository.deleteCalculation(calculationId) }
    }
}
