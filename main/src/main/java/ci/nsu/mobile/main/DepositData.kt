package ci.nsu.mobile.main

import java.io.Serializable

data class DepositData(
    val initialAmount: Double = 0.0,
    val periodMonths: Int = 0,
    val interestRate: Double = 0.0,
    val monthlyTopUp: Double? = null,
    val finalAmount: Double = 0.0,
    val interestEarned: Double = 0.0
)  : Serializable
