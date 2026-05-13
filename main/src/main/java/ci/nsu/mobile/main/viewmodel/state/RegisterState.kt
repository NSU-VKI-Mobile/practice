package ci.nsu.mobile.main.viewmodel.state

import ci.nsu.mobile.main.data.network.model.GroupDto

data class RegisterState(
    val firstName: String = "",
    val lastName: String = "",
    val middleName: String? = null,
    val birthDate: String? = null,
    val gender: String? = null,
    val groupId: Int = 0,
    val login: String = "",
    val password: String = "",
    val email: String = "",
    val phoneNumber: String? = null,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val radioButtonsState: Boolean = true,
    val groupName: String = "",
    val groups: List<GroupDto> = emptyList(),
    val showDDMenu: Boolean = false
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
    data class RBStateChanged(val newState: Boolean): RegisterEvents()
    data class MenuStateChanged(val newState: Boolean): RegisterEvents()
    object SubmitRegister: RegisterEvents()
    object ValidationScreen: RegisterEvents()
    object CleanAll: RegisterEvents()
}