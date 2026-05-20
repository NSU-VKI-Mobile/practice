package ci.nsu.mobile.main.data.model

data class DepositCalculation (
    val id: Int,
    val date: String,
    val startAmount: Double,
    val months: Int,
    val percent: Int,
    val monthlyTopUp: Double,
    val finalAmount: Double,
    val earned: Double
)