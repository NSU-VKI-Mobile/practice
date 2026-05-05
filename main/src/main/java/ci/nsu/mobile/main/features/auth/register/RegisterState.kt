package ci.nsu.mobile.main.features.auth.register

import java.time.LocalDate

import ci.nsu.mobile.main.core.ui.FieldValue
import ci.nsu.mobile.main.core.utils.Field
import ci.nsu.mobile.main.domain.Gender
import ci.nsu.mobile.main.model.dto.GroupDto
import ci.nsu.mobile.main.model.dto.PersonDto
import ci.nsu.mobile.main.model.dto.RegisterRequest
import java.time.format.DateTimeFormatter

data class RegisterState(
    val isLoading: Boolean = false,
    val isButtonEnabled: Boolean = false,

    val error: String? = null,

    val genders: List<Gender> = Gender.entries.toList(),
    val groups: List<GroupDto> = emptyList(),

    val fields: Map<Field, FieldValue> = mapOf(
        Field.USERNAME to FieldValue.Text(""),
        Field.PASSWORD to FieldValue.Text(""),
        Field.EMAIL to FieldValue.Text(""),
        Field.PHONE to FieldValue.Text(""),

        Field.FIRSTNAME to FieldValue.Text(""),
        Field.LASTNAME to FieldValue.Text(""),
        Field.MIDDLENAME to FieldValue.Text(""),

        Field.GENDER to FieldValue.GenderValue(null),
        Field.BIRTHDATE to FieldValue.Date(null),
        Field.GROUP to FieldValue.Group(null)
    ),

    val fieldErrors: Map<Field, List<String>> = mapOf(
        Field.USERNAME to emptyList(),
        Field.PASSWORD to emptyList(),
        Field.EMAIL to emptyList(),
        Field.PHONE to emptyList(),

        Field.FIRSTNAME to emptyList(),
        Field.LASTNAME to emptyList(),
        Field.MIDDLENAME to emptyList(),

        Field.GENDER to emptyList(),
        Field.BIRTHDATE to emptyList(),
        Field.GROUP to emptyList(),

    ),
)

fun RegisterState.toRegisterRequest(): RegisterRequest {
    val person = PersonDto(
        (fields.getValue(Field.FIRSTNAME) as? FieldValue.Text)?.value ?: "",
        (fields.getValue(Field.LASTNAME) as? FieldValue.Text)?.value ?: "",
        (fields.getValue(Field.MIDDLENAME) as? FieldValue.Text)?.value ?: "",
        (fields.getValue(Field.BIRTHDATE) as? FieldValue.Date)?.value?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))?: "",
        (fields.getValue(Field.GENDER) as? FieldValue.GenderValue)?.value?.value?: "",
        (fields.getValue(Field.GROUP) as? FieldValue.Group)?.value?.id?: -1,
    )
    return RegisterRequest(
        username = (fields.getValue(Field.USERNAME) as? FieldValue.Text)?.value ?: "",
        password = (fields.getValue(Field.PASSWORD) as? FieldValue.Text)?.value ?: "",
        email = (fields.getValue(Field.EMAIL) as? FieldValue.Text)?.value ?: "",
        phoneNumber = (fields.getValue(Field.PHONE) as? FieldValue.Text)?.value ?: "",
        person = person
    )
}

fun RegisterState.getText(field: Field): String {
    return (fields[field] as? FieldValue.Text)?.value.orEmpty()
}
fun RegisterState.getDate(field: Field): LocalDate? {
    return (fields[field] as? FieldValue.Date)?.value
}
fun RegisterState.getGender(field: Field): Gender? {
    return (fields[field] as? FieldValue.GenderValue)?.value
}
fun RegisterState.getGroup(field: Field): GroupDto? {
    return (fields[field] as? FieldValue.Group)?.value
}