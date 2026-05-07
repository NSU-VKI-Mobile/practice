package ci.nsu.mobile.domain.models

data class User(
    val userId: Int,
    val login: String,
    val email: String,
    val phoneNumber: String? = null,
    val roleId: Int,
    val authAllowed: Boolean,
    val personId: Int,
    val createdDate: String? = null,
    val lastLoginDate: String? = null
)