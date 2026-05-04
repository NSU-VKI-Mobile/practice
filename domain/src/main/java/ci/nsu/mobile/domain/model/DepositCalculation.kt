package ci.nsu.mobile.domain.model

/**
 * Deposit calculation visible through the calculations module API.
 */
data class DepositCalculation(
    val id: Long = 0,
    val userId: Long,
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double?,
    val finalAmount: Double,
    val interestEarned: Double,
    val calculationDate: Long = System.currentTimeMillis()
)
