package ci.nsu.mobile.main

import java.io.Serializable

data class DepositData(
    val initialAmount: Double = 0.0,
    val periodMonths: Int = 0,
    var interestRate: Double = 0.0,
    var monthlyTopUp: Double? = null,
    var finalAmount: Double = 0.0,
    var interestEarned: Double = 0.0
) : Serializable