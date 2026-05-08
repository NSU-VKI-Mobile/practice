package com.example.depositapp.ui.main

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onCalculate: () -> Unit,
    onHistory:   () -> Unit,
    onClose:     () -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Расчёт вкладов",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Добро пожаловать!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Рассчитайте прибыльность\nвашего вклада",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Кнопка "Рассчитать" — основное действие
            Button(
                onClick = onCalculate,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Рассчитать", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Кнопка "История" — вторичное действие
            OutlinedButton(
                onClick = onHistory,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("История расчётов", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Кнопка "Закрыть" — деструктивное действие, другой цвет
            OutlinedButton(
                onClick = { (context as? ComponentActivity)?.finish() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Закрыть приложение", fontSize = 16.sp)
            }
        }
    }
}
