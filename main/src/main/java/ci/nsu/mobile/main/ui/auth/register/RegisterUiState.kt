package ci.nsu.mobile.main.ui.auth.register

import ci.nsu.mobile.main.data.dto.GroupDto

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}

data class RegisterFormState(
    val firstName: String = "",
    val lastName: String = "",
    val middleName: String = "",
    val birthDate: String = "",
    val gender: String = "",
    val groupId: Int? = null,
    val login: String = "",
    val password: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    // Errors
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val birthDateError: String? = null,
    val genderError: String? = null,
    val groupError: String? = null,
    val loginError: String? = null,
    val passwordError: String? = null,
    val emailError: String? = null
)

data class RegisterUiState(
    val formState: RegisterFormState = RegisterFormState(),
    val groups: List<GroupDto> = emptyList(),
    val groupsLoading: Boolean = false,
    val groupsError: String? = null,
    val registerState: RegisterState = RegisterState.Idle,
    val hasNavigated: Boolean = false
)
