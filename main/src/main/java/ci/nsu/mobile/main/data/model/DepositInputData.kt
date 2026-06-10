package ci.nsu.mobile.main.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DepositInputData(
    val initialAmount: Double,
    val periodMonths: Int
) : Parcelable