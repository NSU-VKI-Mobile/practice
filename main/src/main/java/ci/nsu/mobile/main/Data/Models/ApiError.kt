package ci.nsu.mobile.main.Data.Models

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ApiError(
    val message: String? = null,
    val error: String? = null
)