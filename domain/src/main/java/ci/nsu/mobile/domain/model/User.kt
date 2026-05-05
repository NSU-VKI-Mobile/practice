package ci.nsu.mobile.domain.model

/**
 * Stable user model used outside the auth module.
 */
data class User(
    val id: Long,
    val login: String,
    val email: String? = null
)
