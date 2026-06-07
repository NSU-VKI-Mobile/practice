package ci.nsu.mobile.auth.viewModels.registration

import ci.nsu.mobile.auth.data.network.models.GroupDto

data class RegisterState(
    val firstName: String = "",
    val lastName: String = "",
    val middleName: String? = null,
    val birthDate: String? = null,
    val gender: String? = null,
    val roleId: Int = 1,
    val groupId: Int = 0,
    val login: String = "",
    val password: String = "",
    val email: String = "",
    val genders: List<String> = listOf("Мужской", "Женский"),
    val phoneNumber: String? = null,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val isValid: Boolean = false,
    val groupName: String = "",
    val groups: List<GroupDto> = emptyList(),
    val showDDMenu: Boolean = false,
    val passwordState: Boolean = false,
    val showDatePicker: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingGroups: Boolean = false,
    val errorFields: Set<String> = emptySet()
)

sealed class RegisterEvents {
    data class SurnameChanged(val newSurname: String): RegisterEvents() //lastName
    data class NameChanged(val newName: String): RegisterEvents() //firstName
    data class PatronymicChanged(val newPatronymic: String): RegisterEvents() //middleName
    data class BirthdayChanged(val newDate: String): RegisterEvents() //birthDate
    data class GenderChanged(val newGender: String): RegisterEvents()
    data class GroupChanged(val newGroup: Int): RegisterEvents()
    data class LoginChanged(val newLogin: String): RegisterEvents()
    data class PasswordChanged(val newPassword: String): RegisterEvents()
    data class EmailChanged(val newEmail: String): RegisterEvents()
    data class PhoneNumberChanged(val newPhoneNumber: String): RegisterEvents()
    data class MenuStateChanged(val newState: Boolean): RegisterEvents()
    data class PasswordVisibilityChanged(val newState: Boolean): RegisterEvents()
    data class DatePickerVisibilityChanged(val newState: Boolean): RegisterEvents()
    object SubmitRegister: RegisterEvents()
    object CleanAll: RegisterEvents()
}