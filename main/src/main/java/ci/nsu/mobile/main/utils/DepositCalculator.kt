package ci.nsu.mobile.main.utils

object DepositCalculator {
    fun calculate(
        initialAmount: Double,
        periodMonths: Int,
        annualRate: Double,
        monthlyTopUp: Double?
    ): Pair<Double, Double> {
        val monthlyRate = annualRate / 100 / 12
        var total = initialAmount
        val topUp = monthlyTopUp ?: 0.0

        for (month in 1..periodMonths) {
            total += topUp
            total *= (1 + monthlyRate)
        }

        val totalTopUp = topUp * periodMonths
        val interestEarned = total - initialAmount - totalTopUp
        return Pair(total, interestEarned)
    }
}
