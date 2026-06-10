package ci.nsu.mobile.calculations

import ci.nsu.mobile.calculations.data.repository.DepositRepository
import ci.nsu.mobile.domain.CalculationsProvider
import ci.nsu.mobile.domain.DepositCalculation
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CalculationsProviderImpl(private val repository: DepositRepository) : CalculationsProvider {
    override fun getCalculationsForUser(userId: Long): Flow<List<DepositCalculation>> {
        return repository.getCalculationsForUser(userId).map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun saveCalculation(calculation: DepositCalculation) {
        repository.saveCalculation(calculation.toEntity())
    }

    override suspend fun deleteCalculation(calculationId: Long) {
        // Implementation for deleting by ID if needed, 
        // or we can fetch first then delete.
    }
}

// Mapper extensions
fun ci.nsu.mobile.calculations.data.local.entities.DepositCalculation.toDomain() = DepositCalculation(
    id = id,
    userId = userId,
    amount = initialAmount,
    periodMonths = periodMonths,
    interestRate = interestRate,
    result = finalAmount,
    date = calculationDate
)

fun DepositCalculation.toEntity() = ci.nsu.mobile.calculations.data.local.entities.DepositCalculation(
    id = id,
    userId = userId,
    initialAmount = amount,
    periodMonths = periodMonths,
    interestRate = interestRate,
    monthlyTopUp = null, // Simplified for domain model
    finalAmount = result,
    interestEarned = result - amount,
    calculationDate = date
)