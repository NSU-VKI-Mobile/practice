package ci.nsu.moble.main.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ci.nsu.moble.main.data.model.ColorItem
import ci.nsu.moble.main.R
import ci.nsu.moble.main.ui.theme.PurpleGrey80
import ci.nsu.moble.main.viewmodel.ColorsFinderVM

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val viewModel : ColorsFinderVM = viewModel()
    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.systemBars
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            var text by rememberSaveable { mutableStateOf("") }
            val colorItems = remember { viewModel.getColorItems() }
            var buttonColor by remember { mutableStateOf(PurpleGrey80) }
            var items by remember { mutableStateOf(colorItems) }
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth(),
                label = { stringResource(R.string.input_color_text_field) },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val foundColor = viewModel.findColorByName(text)
                    if (foundColor == null)
                        Log.w(
                            "IsColorFound",
                            "$text " + context.getString(R.string.color_not_found_error)
                        )
                    buttonColor = foundColor ?: PurpleGrey80
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                )
            ) {
                Text(stringResource(R.string.apply_color))
            }
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
                    ColorListItem(item = item)
                }
            }
        }
    }
}
@Composable
fun ColorListItem(item: ColorItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        colors = CardDefaults.cardColors(
            containerColor = item.color
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.colorInfo,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White
            )
        }
    }
}