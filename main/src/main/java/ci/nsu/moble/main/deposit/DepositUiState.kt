package ci.nsu.moble.main.deposit

data class DepositUiState(
    val initialAmount: String = "",
    val months: String = "",
    val ratePercent: Int? = null,
    val monthlyTopUp: String = "",
    val finalAmount: Double? = null,
    val interestAmount: Double? = null,
    val errorMessage: String? = null,
    val saveMessage: String? = null
)