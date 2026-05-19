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
    val selectedInterestRate: Int = 0
)