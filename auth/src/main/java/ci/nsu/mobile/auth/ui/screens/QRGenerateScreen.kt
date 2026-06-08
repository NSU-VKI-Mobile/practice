package ci.nsu.mobile.auth.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.mobile.auth.viewModels.code.CodeEvents
import ci.nsu.mobile.auth.viewModels.code.CodeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrGenerateScreen(
    viewModel: CodeViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    if (state.showSaveDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(CodeEvents.DismissSaveDialog) },
            title = { Text("QR-код создан") },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    state.qrCode?.let { bitmap ->
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "QR-код",
                            modifier = Modifier.size(200.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Сохранить изображение в галерею?")
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.onEvent(CodeEvents.SaveToGallery) }) {
                    Text("Сохранить")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(CodeEvents.DismissSaveDialog) }) {
                    Text("Отмена")
                }
            }
        )
    }

    state.errorMessage?.let { error ->
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(CodeEvents.DismissError) },
            title = { Text("Ошибка") },
            text = { Text(error) },
            confirmButton = {
                TextButton(onClick = { viewModel.onEvent(CodeEvents.DismissError) }) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("QR-код авторизации") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = state.password,
                onValueChange = { viewModel.onEvent(CodeEvents.UpdatePassword(it)) },
                label = { Text("Пароль для QR-кода") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.onEvent(CodeEvents.GenerateQr) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isGenerating && state.password.isNotEmpty()
            ) {
                if (state.isGenerating) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Создать QR-код авторизации")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            state.qrCode?.let { bitmap ->
                Card(modifier = Modifier.padding(16.dp)) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "QR-код",
                        modifier = Modifier
                            .size(250.dp)
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}