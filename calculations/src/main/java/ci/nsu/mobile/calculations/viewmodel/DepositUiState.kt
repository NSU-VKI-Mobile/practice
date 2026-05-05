package ci.nsu.mobile.calculations.viewmodel

data class DepositUiState(
    // Данные с первого экрана
    val initialAmount: String = "",
    val periodMonths: String = "",

    // Данные со второго экрана
    val interestRate: String = "",
    val monthlyTopUp: String = "",

    // Результаты расчета
    val finalAmount: Double = 0.0,
    val interestEarned: Double = 0.0
)