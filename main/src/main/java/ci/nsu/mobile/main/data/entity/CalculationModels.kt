package ci.nsu.mobile.main.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class CalculationInput(
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double
)

@Parcelize
data class CalculationResult(
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double,
    val finalAmount: Double,
    val interestEarned: Double
) : Parcelable
