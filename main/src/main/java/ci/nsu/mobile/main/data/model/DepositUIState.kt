package ci.nsu.mobile.main.data.model

data class DepositUIState (
    /** стартовый взнос **/
    val initialAmount: String = "",
    /** срок вклада **/
    val periodMonths: String = "",
    /** процентная ставка **/
    val interestRate: String = "",
    /** ежемесячное пополнение **/
    val monthlyTopUp: String? = "",
    /** итоговая сумма **/
    val finalAmount: Double = 0.0,
    /** начисленные проценты **/
    val interestEarned: Double = 0.0,
    /** дата и время рассчета **/
    val calculationDate: Long = 0
)