package ci.nsu.mobile.main.Components

import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun AnimatedButton(
    onClick: () -> Unit,
    enabled: Boolean,
    text: String,
    modifier: Modifier = Modifier,
    enabledContainerColor: Color = MaterialTheme.colorScheme.primary,
    disabledContainerColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f),
    enabledContentColor: Color = MaterialTheme.colorScheme.onPrimary,
    disabledContentColor: Color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.38f)
) {
    // Анимируем цвет фона
    val animatedContainerColor by animateColorAsState(
        targetValue = if (enabled) enabledContainerColor else disabledContainerColor,
        label = "buttonContainerColor"
    )
    val animatedContentColor by animateColorAsState(
        targetValue = if (enabled) enabledContentColor else disabledContentColor,
        label = "buttonContentColor"
    )

    // Анимируем цвет текста
    Button(
        onClick = onClick, enabled = enabled, colors = ButtonDefaults.buttonColors(
            containerColor = animatedContainerColor,
            contentColor = animatedContentColor,
            disabledContainerColor = animatedContainerColor,
            disabledContentColor = animatedContentColor
        ), modifier = modifier
    ) {
        Text(text)
    }
}