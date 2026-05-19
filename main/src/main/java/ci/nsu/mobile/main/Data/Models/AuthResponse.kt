package ci.nsu.mobile.main.Data.Models

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class AuthResponse(
    val token: String
)