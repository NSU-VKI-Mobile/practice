package ci.nsu.mobile.main.viewmodel

data class FirstStepState(
    val initialAmount: String = "",
    val periodMonths: String = "",
    val initialAmountError: String? = null,
    val periodMonthsError: String? = null,
    val isNextEnabled: Boolean = false
)

data class SecondStepState(
    val initialAmount: Double = 0.0,
    val periodMonths: Int = 0,
    val interestRate: Double = 0.0,
    val monthlyTopUp: String = "",
    val availableRates: List<Pair<Double, Int>> = emptyList(),  
    val selectedRate: Double = 0.0,
    val selectedPeriodMonths: Int = 0,
    val monthlyTopUpError: String? = null,
    val isCalculateEnabled: Boolean = true
)

data class ResultState(
    val initialAmount: Double = 0.0,
    val periodMonths: Int = 0,
    val interestRate: Double = 0.0,
    val monthlyTopUp: Double? = null,
    val finalAmount: Double = 0.0,
    val interestEarned: Double = 0.0,
    val showResult: Boolean = false
)