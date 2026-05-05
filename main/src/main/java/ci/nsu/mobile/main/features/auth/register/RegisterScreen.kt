package ci.nsu.mobile.main.features.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.main.core.ui.Dropdown
import ci.nsu.mobile.main.core.ui.BaseScreen
import ci.nsu.mobile.main.core.ui.BirthDateField
import ci.nsu.mobile.main.core.ui.ButtonsPanel
import ci.nsu.mobile.main.core.ui.ErrorsCard
import ci.nsu.mobile.main.core.ui.FieldValue
import ci.nsu.mobile.main.core.ui.Link
import ci.nsu.mobile.main.core.ui.TextField
import ci.nsu.mobile.main.core.utils.Field
import ci.nsu.mobile.main.domain.Gender

val PADDING = 12.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    state: RegisterState,
    onButtonClick: () -> Unit,
    onLinkClick: () -> Unit,
    onFieldChange: (field: Field, value: FieldValue) -> Unit,
) {
    BaseScreen(
        title = "Регистрация",
        bottomBar = {
            Column(
                modifier = Modifier
                    .imePadding()
                    .padding(PADDING),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(PADDING)
            ) {
                ButtonsPanel {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = onButtonClick
                    ) {
                        Text("Зарегистрироваться")
                    }
                }
                Link(
                    text = "Вход",
                    onClicked = onLinkClick
                )

            }
        }
    ) {
        run {
            val field = Field.LASTNAME
            TextField(
                label = "Фамилия",
                field = field,
                value = state.getText(field),
                errors = state.fieldErrors.getValue(field),
                onValueChange = onFieldChange
            )
        }
        run {
            val field = Field.FIRSTNAME
            TextField(
                label = "Имя",
                field = field,
                value = state.getText(field),
                errors = state.fieldErrors.getValue(field),
                onValueChange = onFieldChange
            )
        }
        run {
            val field = Field.MIDDLENAME
            TextField(
                label = "Отчество",
                field = field,
                value = state.getText(field),
                errors = state.fieldErrors.getValue(field),
                onValueChange = onFieldChange
            )
        }
        run {
            val field = Field.BIRTHDATE
            BirthDateField(
                value = state.getDate(field),
                errors = state.fieldErrors.getValue(field),
                onDateSelect = { onFieldChange(field, FieldValue.Date(it)) }
            )
        }
        run {
            val field = Field.BIRTHDATE
            Dropdown (
                label = "Пол",
                items = state.genders,
                errors = state.fieldErrors.getValue(field),
                selected = state.getGender(Field.GENDER),
                onSelect = {
                    onFieldChange(Field.GENDER, FieldValue.GenderValue(it))
                },
                itemLabel = { it.value }
            )
        }
        run {
            val field = Field.GROUP
            Dropdown (
                label = "Группа",
                items = state.groups,
                errors = state.fieldErrors.getValue(field),
                selected = state.getGroup(field),
                onSelect = {
                    onFieldChange(Field.GROUP, FieldValue.Group(it))
                },
                itemLabel = { it.name }
            )
        }
        run {
            val field = Field.EMAIL
            TextField(
                label = "Почта",
                field = field,
                value = state.getText(field),
                errors = state.fieldErrors.getValue(field),
                onValueChange = onFieldChange
            )
        }
        run {
            val field = Field.PHONE
            TextField(
                label = "Телефон",
                field = field,
                value = state.getText(field),
                errors = state.fieldErrors.getValue(field),
                onValueChange = onFieldChange
            )
        }
        run {
            val field = Field.USERNAME
            TextField(
                label = "Логин",
                field = field,
                value = state.getText(field),
                errors = state.fieldErrors.getValue(field),
                onValueChange = onFieldChange
            )
        }
        run {
            val field = Field.PASSWORD
            TextField(
                label = "Пароль",
                field = field,
                value = state.getText(field),
                errors = state.fieldErrors.getValue(field),
                onValueChange = onFieldChange
            )
        }
    }
}