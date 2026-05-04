package ci.nsu.mobile.auth.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import ci.nsu.mobile.auth.data.local.TokenManager
import ci.nsu.mobile.auth.qr.QrAuthPayload
import ci.nsu.mobile.auth.qr.QrCodeGenerator
import ci.nsu.mobile.auth.qr.QrImageSaver

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    val context = LocalContext.current
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var pendingSaveBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showMissingPasswordDialog by remember { mutableStateOf(false) }

    val storagePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        val bitmap = pendingSaveBitmap
        pendingSaveBitmap = null
        if (isGranted && bitmap != null) {
            saveQrBitmap(context, bitmap)
        } else {
            Toast.makeText(context, "Сохранение QR-кода отменено", Toast.LENGTH_SHORT).show()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Профиль пользователя", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Логин: ${TokenManager.login ?: "неизвестно"}")
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val login = TokenManager.login.orEmpty()
                        val password = TokenManager.password.orEmpty()
                        if (login.isBlank() || password.isBlank()) {
                            showMissingPasswordDialog = true
                        } else {
                            qrBitmap = QrCodeGenerator.generateBitmap(QrAuthPayload(login, password))
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Создать QR-код авторизации")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Выйти")
                }
            }
        }

        HorizontalDivider()
        Text(
            text = "Пользователи",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            style = MaterialTheme.typography.titleMedium
        )
        Box(modifier = Modifier.weight(1f)) {
            UsersScreen()
        }
    }

    qrBitmap?.let { bitmap ->
        AlertDialog(
            onDismissRequest = { qrBitmap = null },
            title = { Text("QR-код авторизации") },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "QR-код авторизации",
                        modifier = Modifier.size(260.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("QR-код содержит логин и пароль текущего пользователя.")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (needsLegacyStoragePermission(context)) {
                            pendingSaveBitmap = bitmap
                            storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        } else {
                            saveQrBitmap(context, bitmap)
                        }
                    }
                ) {
                    Text("Сохранить в галерею")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { qrBitmap = null }) {
                    Text("Закрыть")
                }
            }
        )
    }

    if (showMissingPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showMissingPasswordDialog = false },
            title = { Text("Пароль не найден") },
            text = {
                Text("Для создания QR-кода нужен пароль из текущей сессии. Выйдите и войдите снова обычным способом, чтобы приложение сохранило данные для QR-авторизации.")
            },
            confirmButton = {
                TextButton(onClick = { showMissingPasswordDialog = false }) {
                    Text("Понятно")
                }
            }
        )
    }
}

private fun needsLegacyStoragePermission(context: android.content.Context): Boolean {
    return Build.VERSION.SDK_INT < Build.VERSION_CODES.Q &&
        ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
}

private fun saveQrBitmap(context: android.content.Context, bitmap: Bitmap) {
    QrImageSaver.saveToGallery(context, bitmap)
        .onSuccess {
            Toast.makeText(context, "QR-код сохранён в галерею", Toast.LENGTH_SHORT).show()
        }
        .onFailure { error ->
            Toast.makeText(context, error.message ?: "Не удалось сохранить QR-код", Toast.LENGTH_LONG).show()
        }
}
