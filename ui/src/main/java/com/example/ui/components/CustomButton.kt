package com.example.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    onClick: () -> Unit,
    title: String,
    modifier: Modifier = Modifier
) {
    Button(onClick = onClick,
        modifier = modifier.padding(bottom = 10.dp)) {
        Text(title)
    }
}