package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ci.nsu.mobile.main.R

@Composable
fun LogInScreen(
    onRegClick: () -> Unit,
    onLogInClick: () -> Unit
){
    val spacer = ""
    val boolSpacer = true
    Column() {
        OutlinedTextField(
            value =  spacer,
            onValueChange = { newText -> spacer },
            label = { Text(text = stringResource(R.string.text_login)) },
            isError = boolSpacer
        )
        OutlinedTextField(
            value = spacer,
            onValueChange = { newText -> spacer },
            label = { Text(stringResource(R.string.text_password)) },
            isError = boolSpacer
        )
        Button(onClick = onLogInClick) {
            Text(stringResource(R.string.text_logIn))
        }
        Button(onClick = onRegClick) {
            Text(stringResource(R.string.text_registry))
        }
    }
}