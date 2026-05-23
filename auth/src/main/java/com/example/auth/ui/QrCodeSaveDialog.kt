package com.example.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.auth.R
import com.example.auth.vm.LoginAndRegViewModel

@Composable
fun QrCodeSaveDialog(
    onDismiss: () -> Unit,
    viewModel: LoginAndRegViewModel = viewModel()
) {
    val qrcode = viewModel.generateQrCode()
    Dialog(onDismissRequest = onDismiss,) {
        Surface(
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Image(
                    bitmap = qrcode.asImageBitmap(),
                    contentDescription = stringResource(R.string.qr_code),
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.text_back))
                    }

                    Button(
                        onClick = { viewModel.saveQrToGallery(qrcode)},
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.save))
                    }
                }
            }
        }
    }
}