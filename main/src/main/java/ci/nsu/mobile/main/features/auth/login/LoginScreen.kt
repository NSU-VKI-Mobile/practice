package ci.nsu.mobile.main.features.auth.login

import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Button
import androidx.compose.material3.Text

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import ci.nsu.mobile.main.core.ui.BaseScreen
import ci.nsu.mobile.main.core.ui.ButtonsPanel
import ci.nsu.mobile.main.core.ui.Link
import ci.nsu.mobile.main.core.ui.TextField
import ci.nsu.mobile.main.core.utils.Field
import ci.nsu.mobile.main.core.ui.FieldValue


val PADDING = 12.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    state: LoginState,
    onButtonClick: () -> Unit,
    onLinkClick: () -> Unit,
    onFieldChange: (field: Field, value: FieldValue) -> Unit,
) {
    BaseScreen(
        title = "Вход",
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
                        Text("Войти")
                    }
                }
                Link(
                    text = "Регистрация",
                    onClicked = onLinkClick
                )

            }
        }
    ) {
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
                label = "Логин",
                field = field,
                value = state.getText(field),
                errors = state.fieldErrors.getValue(field),
                onValueChange = onFieldChange
            )
        }
    }
}