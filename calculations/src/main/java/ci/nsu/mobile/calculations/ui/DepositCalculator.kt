package ci.nsu.mobile.calculations.ui

class DepositCalculator {
    data class Result(val finalAmount: Double, val interestEarned: Double)

    fun calculate(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double
    ): Result {
        var total = initialAmount
        var earned = 0.0
        val monthlyRate = interestRate / 100 / 12

        for (i in 1..periodMonths) {
            total += monthlyTopUp
            val currentMonthInterest = total * monthlyRate
            earned += currentMonthInterest
            total += currentMonthInterest
        }
        return Result(finalAmount = total, interestEarned = earned)
    }
}