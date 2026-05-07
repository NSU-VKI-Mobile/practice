package ci.nsu.mobile.domain.models

data class DepositCalculation(
    val id: Long = 0,
    val userId: Int,
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double,
    val finalAmount: Double,
    val interestEarned: Double,
    val calculationDate: Long
)