package ci.nsu.mobile.domain.model

/**
 * Result of deposit calculation before it is saved.
 */
data class CalculationResult(
    val finalAmount: Double,
    val interestEarned: Double
)
