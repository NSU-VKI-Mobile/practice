package ci.nsu.mobile.main.core.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ci.nsu.mobile.main.features.auth.login.PADDING
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthDateField(
    value: LocalDate?,
    errors: List<String>,
    onDateSelect: (LocalDate) -> Unit
) {
    val isError = errors.isNotEmpty()
    var open by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value?.toString() ?: "",
        onValueChange = {},
        isError = isError,
        readOnly = true,
        label = { Text("Дата рождения") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = PADDING, end = PADDING),
        trailingIcon = {
            IconButton(onClick = { open = true }) {
                Icon(Icons.Default.DateRange, contentDescription = null)
            }
        }
    )

    if (open) {
        val state = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { open = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val millis = state.selectedDateMillis
                        if (millis != null) {
                            val date = Instant
                                .ofEpochMilli(millis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()

                            onDateSelect(date)
                        }
                        open = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { open = false }) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(state = state)
        }
    }
    if (isError) {
        ErrorsCard(errors)
    }
}