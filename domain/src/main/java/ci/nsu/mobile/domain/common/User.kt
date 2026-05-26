    package ci.nsu.mobile.domain.common

data class User(
    val id: Long,
    val login: String,
    val email: String? = null
)