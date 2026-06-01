package com.example.auth.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.auth.R
import com.example.auth.vm.LoginAndRegViewModel

@Composable
fun LogInScreen(
    onRegClick: () -> Unit,
    onLogInClick: () -> Unit,
    onQrCodeClick: () -> Unit,
    onExitClick: () -> Unit,
    viewModel: LoginAndRegViewModel = viewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column() {
        OutlinedTextField(
            value =  uiState.login,
            onValueChange = { newText -> viewModel.setLogin(newText) },
            label = { Text(text = stringResource(R.string.text_login)) }
        )
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { newText -> viewModel.setPassword(newText) },
            label = { Text(stringResource(R.string.text_password)) }
        )
        Button(onClick = onLogInClick) {
            Text(stringResource(R.string.text_logIn))
        }
        Button(onClick = onQrCodeClick) {
            Text(stringResource(R.string.text_logIn_qrcode))
        }
        Button(onClick = onRegClick) {
            Text(stringResource(R.string.text_registry))
        }
        Button(onClick = onExitClick) {
            Text(stringResource(R.string.text_exit))
        }
    }
}