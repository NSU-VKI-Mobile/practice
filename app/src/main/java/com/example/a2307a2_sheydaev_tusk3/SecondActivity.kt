package com.example.sheydaevtusk3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val receivedData = intent.getStringExtra("key_data") ?: "Нет данных"

        setContent {
            SecondActivityContent(receivedData)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondActivityContent(receivedData: String) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Second Activity") },
                colors = TopAppBarDefaults.topAppBarColors(),
                navigationIcon = {
                    Button(
                        onClick = { (context as? SecondActivity)?.finish() },
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text("Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        // paddingValues используется здесь
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Получено: $receivedData")

            Button(
                onClick = { (context as? SecondActivity)?.finish() },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Вернуться в Main")
            }
        }
    }
}