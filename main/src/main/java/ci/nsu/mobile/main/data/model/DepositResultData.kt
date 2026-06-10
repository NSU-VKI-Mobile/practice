package ci.nsu.mobile.main.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DepositResultData(
    val initialAmount: Double,
    val periodMonths: Int,
    val interestRate: Double,
    val monthlyTopUp: Double?,
    var finalAmount: Double = 0.0,
    var interestEarned: Double = 0.0
) : Parcelable