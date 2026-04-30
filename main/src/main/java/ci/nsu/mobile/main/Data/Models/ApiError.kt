package ci.nsu.mobile.main.Data.Models

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(
    val message: String? = null,
    val error: String? = null
)