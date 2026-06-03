package ci.nsu.mobile.main.utils

object Validator {

    fun validateInitialAmount(amount: String): Result<Double> {
        return try {
            val value = amount.toDouble()
            if (value <= 0) {
                Result.Error("Стартовый взнос должен быть больше 0")
            } else {
                Result.Success(value)
            }
        } catch (e: NumberFormatException) {
            Result.Error("Введите корректное число")
        }
    }

    fun validatePeriodMonths(period: String): Result<Int> {
        return try {
            val value = period.toInt()
            if (value <= 0) {
                Result.Error("Срок должен быть больше 0 месяцев")
            } else {
                Result.Success(value)
            }
        } catch (e: NumberFormatException) {
            Result.Error("Введите корректное число месяцев")
        }
    }

    fun validateMonthlyTopUp(amount: String): Result<Double?> {
        if (amount.isEmpty()) {
            return Result.Success(null)
        }
        return try {
            val value = amount.toDouble()
            if (value < 0) {
                Result.Error("Ежемесячное пополнение не может быть отрицательным")
            } else {
                Result.Success(value)
            }
        } catch (e: NumberFormatException) {
            Result.Error("Введите корректное число")
        }
    }

    fun calculateInterestRate(periodMonths: Int): Double {
        return when {
            periodMonths < 6 -> 15.0
            periodMonths < 12 -> 10.0
            else -> 5.0
        }
    }

    fun calculateDeposit(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double?
    ): DepositResult {
        val monthlyRate = interestRate / 100 / 12
        var currentAmount = initialAmount

        repeat(periodMonths) {
            val interest = currentAmount * monthlyRate
            currentAmount += interest
            monthlyTopUp?.let {
                currentAmount += it
            }
        }

        val finalAmount = currentAmount
        val totalTopUp = (monthlyTopUp ?: 0.0) * periodMonths
        val interestEarned = finalAmount - initialAmount - totalTopUp

        return DepositResult(finalAmount, interestEarned)
    }

    data class DepositResult(val finalAmount: Double, val interestEarned: Double)

    sealed class Result<T> {
        data class Success<T>(val data: T) : Result<T>()
        data class Error<T>(val message: String) : Result<T>()
    }
}