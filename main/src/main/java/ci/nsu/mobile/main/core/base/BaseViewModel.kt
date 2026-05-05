package ci.nsu.mobile.main.core.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

import ci.nsu.mobile.main.core.utils.Field
import ci.nsu.mobile.main.core.utils.Validator
import ci.nsu.mobile.main.core.ui.FieldValue


open class BaseViewModel<E, S> (
    initialState: S
): ViewModel() {
    internal val _event = MutableSharedFlow<E>()
    val event = _event.asSharedFlow()

    internal val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    val validators: Map<Field, (String) -> List<String>> = mapOf(
        Field.USERNAME to Validator::validateUsername,
        Field.PASSWORD to Validator::validatePassword,
        Field.EMAIL to Validator::validateEmail,
        Field.PHONE to Validator::validatePhone,

        Field.FIRSTNAME to Validator::validateBlank,
        Field.LASTNAME to Validator::validateBlank,
        Field.MIDDLENAME to Validator::validateBlank,

        Field.GROUP to Validator::validateBlank,
        Field.GENDER to Validator::validateBlank,
        Field.BIRTHDATE to Validator::validateBlank,
    )

    fun validateField(field: Field, value: FieldValue): List<String> {
        return when (value) {
            is FieldValue.Text -> {
                validators.getValue(field).invoke(value.value)
            }
            is FieldValue.Date -> {
                if (value.value == null) listOf("обязательное поле") else emptyList()
            }
            is FieldValue.GenderValue -> {
                if (value.value == null) listOf("обязательное поле") else emptyList()
            }
            is FieldValue.Group -> {
                if (value.value == null) listOf("обязательное поле") else emptyList()
            }
//            else -> {
//                emptyList()
//            }
        }
    }
}