package ci.nsu.mobile.calculations.provider

import ci.nsu.mobile.calculations.data.DepositRepository
import ci.nsu.mobile.domain.calculations.CalculationsProvider
import ci.nsu.mobile.domain.models.DepositCalculation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CalculationsProviderImpl(private val repository: DepositRepository) : CalculationsProvider {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>> {
        return repository.getHistory(userId.toInt())
    }

    override fun saveCalculation(calculation: DepositCalculation) {
        scope.launch {
            repository.insert(calculation)
        }
    }

    override fun deleteCalculation(calculationId: Long) {
        scope.launch {
            repository.delete(calculationId)
        }
    }
}