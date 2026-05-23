package com.example.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.auth.vm.LoginAndRegViewModel

@Composable
fun MyProfileScreen(
    viewModel: LoginAndRegViewModel = viewModel()
){
    var showDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.padding(24.dp)
    ) {
        Text(viewModel.getCurLogin() ?: "null")
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {showDialog = true},
            modifier = Modifier.weight(1f)
        ) {
            Text("Создать QR-code")
        }
    }

    if(showDialog){
        QrCodeSaveDialog(
            onDismiss = {showDialog = false},
            viewModel = viewModel
        )
    }
}
