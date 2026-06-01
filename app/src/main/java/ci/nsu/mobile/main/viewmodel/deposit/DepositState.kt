package ci.nsu.mobile.main.viewmodel.deposit

data class DepositUIState (
    /** стартовый взнос **/
    val initialAmount: String = "",
    /** срок вклада **/
    val periodMonths: String = "",
    /** процентная ставка **/
    val interestRate: String = "",
    /** ежемесячное пополнение **/
    val monthlyTopUp: String? = null,
    /** итоговая сумма **/
    val finalAmount: Double = 0.0,
    /** начисленные проценты **/
    val interestEarned: Double = 0.0,
    /** дата и время рассчета **/
    val calculationDate: Long = 0,
    /** выбрано ли ежемесячное пополнение **/
    val monthlyTopUpCheck: Boolean = false,
    /** выбранная ставка **/
    val selectedInterestRate: Int = 0,
    val errorFieldsFirstScreen: Set<String> = emptySet(),
    val errorFieldsSecondScreen: Set<String> = emptySet(),
    val goToSecondScreen: Boolean = false,
    val goToResultScreen: Boolean = false,
    val errorMessage: String? = null
)

sealed class DepositEvents {
    data class InitialAmountChanged(val newInitialAmount: String): DepositEvents()
    data class PeriodMonthsChanged(val newPeriodMonths: String): DepositEvents()
    data class InterestRateChanged(val newInterestRate: String): DepositEvents()
    data class MonthlyTopUpChanged(val newMonthlyTopUp: String?): DepositEvents()
    data class GoToSecondScreen(val value: Boolean): DepositEvents()
    data class GoToResultScreen(val value: Boolean): DepositEvents()
    data class SelectedRateUpdate(val newRate: Int): DepositEvents()
    data class IsMonthlyTopUpCheck(val newCheck: Boolean): DepositEvents()
    object CleanAll: DepositEvents()
    object ValidationFirstScreen: DepositEvents()
    data class ValidationSecondScreen(val isChecked: Boolean): DepositEvents()
    data class CalculationFinalAmount(val initialAmount: Double, val interestRate: Int,
                                      val periodMonths: Int, val monthlyTopUp: Double?, val date: Long): DepositEvents()
    object SaveEntity: DepositEvents()
}