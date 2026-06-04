package ci.nsu.mobile.domain.model

data class DepositCalculation(
    val id: Int = 0,
    val userId: Int = 0,
    val startAmount: Double,
    val months: Int,
    val rate: Double,
    val monthlyTopUp: Double?,
    val finalAmount: Double,
    val profit: Double,
    val date: Long
)