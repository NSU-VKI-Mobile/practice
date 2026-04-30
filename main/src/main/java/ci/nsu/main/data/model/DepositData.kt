package ci.nsu.mobile.main.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DepositData(
    var initialAmount: Double? = null,
    var periodMonths: Int? = null,
    var interestRate: Double? = null,
    var monthlyTopUp: Double? = null
) : Parcelable {
    fun isValid(): Boolean {
        return initialAmount != null && initialAmount!! > 0 &&
                periodMonths != null && periodMonths!! > 0
    }
}