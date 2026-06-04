package ci.nsu.mobile.domain.model

data class DepositCalculation(
    val id: Long = 0L,
    val userId: Long = 0L,
    val startAmount: Double,
    val months: Int,
    val rate: Double,
    val monthlyTopUp: Double?,
    val finalAmount: Double,
    val profit: Double,
    val date: Long
)