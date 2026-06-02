package ci.nsu.mobile.depositcalculator.model
data class DepositCalculation(
    val amount: Double,
    val termMonths: Int,
    val interestRate: Double,
    val capitalization: Boolean,
    val finalAmount: Double,
    val profit: Double
)