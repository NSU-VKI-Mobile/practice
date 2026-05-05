package ci.nsu.mobile.main.features.auth.login

import ci.nsu.mobile.main.core.utils.Field
import ci.nsu.mobile.main.core.ui.FieldValue
import ci.nsu.mobile.main.model.dto.LoginRequest

data class LoginState(
    val isLoading: Boolean = false,
    val isButtonEnabled: Boolean = false,

    val error: String? = null,

    val fields: Map<Field, FieldValue> = mapOf(
        Field.USERNAME to FieldValue.Text(""),
        Field.PASSWORD to FieldValue.Text(""),
    ),

    val fieldErrors: Map<Field, List<String>> = mapOf(
        Field.USERNAME to emptyList(),
        Field.PASSWORD to emptyList(),
    ),
)

fun LoginState.toLoginRequest(): LoginRequest {
    return LoginRequest(
        username = (fields.getValue(Field.USERNAME) as? FieldValue.Text)?.value ?: "",
        password = (fields.getValue(Field.PASSWORD) as? FieldValue.Text)?.value ?: "",
    )
}

fun LoginState.getText(field: Field): String {
    return (fields[field] as? FieldValue.Text)?.value.orEmpty()
}