package ci.nsu.mobile.main.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ci.nsu.mobile.main.R

@Composable
fun ErrorScreen(
    onDismiss: () -> Unit,
    onExit: () -> Unit,
    error: String
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column() {
                Text(error)
                Button(onClick = onExit) {
                    Text(stringResource(R.string.text_exit))
                }
                Button(onClick = onDismiss) {
                    Text(stringResource(R.string.ok))
                }
            }
        }
    }
}