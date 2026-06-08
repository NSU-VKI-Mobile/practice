package ci.nsu.mobile.auth.viewModels.userOwn

data class UserOwnState(
    val userLogin: String = "",
    val userId: Int = 0,
    val userEmail: String? = null,
    val userCreatedDate: String = "",
    val userPhone: String? = null,
    val userPersonId: Int = 0,
    val userLastLoginDate: String? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)
