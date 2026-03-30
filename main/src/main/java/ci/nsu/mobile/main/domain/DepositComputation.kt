package ci.nsu.mobile.main.domain

data class DepositComputation(
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double, // annual, %
    val monthlyTopUp: Double?,
    val finalAmount: Double,
    val interestEarned: Double,
)

fun calculateInterestRateForPeriod(periodMonths: Int): Double {
    return when {
        periodMonths < 6 -> 15.0
        periodMonths < 12 -> 10.0
        else -> 5.0
    }
}

fun computeDeposit(
    initialAmount: Double,
    periodMonths: Int,
    monthlyTopUp: Double?,
    interestRate: Double,
): DepositComputation {
    val topUp = monthlyTopUp ?: 0.0
    val totalPrincipal = initialAmount + topUp * periodMonths

    val interestEarned =
        totalPrincipal * (interestRate / 100.0) * (periodMonths / 12.0)

    val finalAmount = totalPrincipal + interestEarned
    return DepositComputation(
        initialAmount = initialAmount,
        periodMonths = periodMonths,
        interestRate = interestRate,
        monthlyTopUp = monthlyTopUp,
        finalAmount = finalAmount,
        interestEarned = interestEarned,
    )
}

