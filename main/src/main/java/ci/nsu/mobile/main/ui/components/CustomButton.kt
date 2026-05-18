package ci.nsu.mobile.main.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    onClick: () -> Unit,
    title: String
) {
    Button(onClick = onClick,
        modifier = Modifier.padding(10.dp)) {
        Text(title)
    }
}