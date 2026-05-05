package ci.nsu.mobile.main.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import ci.nsu.mobile.main.core.utils.Field
import ci.nsu.mobile.main.core.ui.FieldValue
import ci.nsu.mobile.main.features.auth.login.PADDING
import kotlin.collections.isNotEmpty
import kotlin.text.orEmpty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextField(
    label: String,
    field: Field,
    value: String,
    errors: List<String>,
    onValueChange: (field: Field, value: FieldValue) -> Unit,
) {
    val isError = errors.isNotEmpty()
    OutlinedTextField(
        label = { Text(label) },
        value = value,

        onValueChange = { onValueChange(field, FieldValue.Text(it)) },
        isError = isError,

        modifier = Modifier
            .fillMaxWidth()
            .padding(start = PADDING, end = PADDING),
        singleLine = true,
    )
    if (isError) {
        ErrorsCard(errors)
    }
}