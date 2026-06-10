package ci.nsu.mobile.auth.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ci.nsu.mobile.auth.utils.ImageSaver
import ci.nsu.mobile.auth.utils.QrCodeGenerator
import ci.nsu.mobile.domain.QrAuthData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun QrCodeDialog(
    login: String,
    password: String,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isSaving by remember { mutableStateOf(false) }
    var showMessage by remember { mutableStateOf(false) }
    var messageText by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val qrData = QrAuthData(login, password)
        val data = qrData.toSimpleString()
        qrBitmap = QrCodeGenerator.generateQrCode(data)
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "QR-код авторизации",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (qrBitmap != null) {
                    Image(
                        bitmap = qrBitmap!!.asImageBitmap(),
                        contentDescription = "QR Code",
                        modifier = Modifier.size(250.dp)
                    )
                } else {
                    CircularProgressIndicator(modifier = Modifier.size(50.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Логин: $login",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Закрыть")
                    }

                    Button(
                        onClick = {
                            scope.launch {
                                isSaving = true
                                val bitmap = qrBitmap
                                if (bitmap != null) {
                                    val success = ImageSaver.saveToGallery(
                                        context,
                                        bitmap,
                                        "qr_auth_${System.currentTimeMillis()}.png"
                                    )
                                    messageText = if (success) {
                                        isSuccess = true
                                        "QR-код сохранён в галерею"
                                    } else {
                                        isSuccess = false
                                        "Ошибка сохранения"
                                    }
                                    showMessage = true
                                }
                                isSaving = false
                                delay(2000)
                                showMessage = false
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !isSaving && qrBitmap != null
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp))
                        } else {
                            Text("Сохранить")
                        }
                    }
                }

                if (showMessage) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = messageText,
                        color = if (isSuccess) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}