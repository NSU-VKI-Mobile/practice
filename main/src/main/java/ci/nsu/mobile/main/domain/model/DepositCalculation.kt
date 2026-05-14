package ci.nsu.mobile.main.domain.model

data class DepositCalculation(
    val id: Long = 0,
    val userId: Long = 0,
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double?,
    val finalAmount: Double,
    val interestEarned: Double,
    val calculationDate: Long = System.currentTimeMillis()
)