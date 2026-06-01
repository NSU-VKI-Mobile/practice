package com.example.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextFieldWithOptionalStar(
    modifier: Modifier = Modifier,
    hasStar: Boolean = false,
    value: String?,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isError: Boolean = false,
    supportingText: String = "Заполните поле"
) {
    Row(modifier = modifier.fillMaxWidth()) {
        TextField(
            value = value ?: "",
            onValueChange = { onValueChange(it)},
            modifier = Modifier.weight(1f).padding(bottom = 10.dp).padding(start = 33.dp),
            placeholder = { Text(placeholder)},
            keyboardOptions = keyboardOptions,
            readOnly = readOnly,
            label = { Text(placeholder)},
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            isError = isError,
            supportingText = {
                if (isError) {
                    Text(supportingText ?: "", color = Color.Red)
                }
            }
        )
        if (hasStar) {
            Spacer(Modifier.width(3.dp))
            Text("*", color = Color.Red, fontSize = 23.sp, modifier = Modifier.width(30.dp))
        } else {
            Spacer(Modifier.width(33.dp))
        }
    }
}