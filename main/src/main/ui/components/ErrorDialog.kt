package com.example.userapp.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.window.Dialog

@Composable
fun ErrorDialog(
    message: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = androidx.compose.ui.Modifier.padding(24.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ошибка",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))
                Text(message, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = androidx.compose.ui.Modifier.height(24.dp))
                Button(onClick = onDismiss) {
                    Text("OK")
                }
            }
        }
    }
}