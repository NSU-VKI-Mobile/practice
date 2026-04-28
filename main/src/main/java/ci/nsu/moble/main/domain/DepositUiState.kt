package ci.nsu.moble.main.domain

data class DepositUiState(
    // Поля ввода (хранятся как String, чтобы удобно работать с TextField)
    val initialAmount: String = "",
    val periodMonths: String = "",
    val monthlyTopUp: String = "",

    // Поля для отображения результатов
    val interestRate: Double = 0.0,       // Ставка
    val finalAmount: Double = 0.0,        // Итоговая сумма
    val interestEarned: Double = 0.0,     // Начисленные проценты

    // Вспомогательные поля
    val error: String? = null,            // Текст ошибки (если есть)
    val isCalculated: Boolean = false,    // Флаг: выполнен ли расчёт?

    val calculationDate: Long = 0L        // Дата расчёта (timestamp)
)