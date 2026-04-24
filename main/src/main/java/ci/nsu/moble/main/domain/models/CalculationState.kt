package ci.nsu.moble.main.domain.models

data class CalculationState(
    val initialAmount: Double = 0.0,
    val periodMonths: Int = 0,
    val interestRate: Double = 0.0,
    val monthlyTopUp: Double? = null
)