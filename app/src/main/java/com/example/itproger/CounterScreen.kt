package com.example.itproger

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

// @Composable — аннотация говорит Kotlin что это функция-экран, а не обычная функция.
// В Compose весь UI строится из таких функций, которые вызывают друг друга.
@Composable
fun CounterScreen(viewModel: CounterViewModel = viewModel()) {
    // Подписываемся на StateFlow из ViewModel.
    // collectAsStateWithLifecycle — "умная" подписка: когда экран свёрнут, обновления не приходят (экономия батареи).
    // by — делегирует чтение, чтобы писать просто uiState.count вместо uiState.value.count
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Column — размещает дочерние элементы вертикально (как LinearLayout vertical в старом Android)
    Column(
        modifier = Modifier
            .fillMaxSize()  // растянуть на весь экран
            .padding(24.dp), // отступы по краям
        horizontalAlignment = Alignment.CenterHorizontally // всё по центру по горизонтали
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Счетчик",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Отображаем текущее значение из uiState — когда ViewModel его меняет, этот Text перерисуется сам
        Text(
            text = "${uiState.count}",
            fontSize = 72.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Row — размещает элементы горизонтально
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp) // промежуток 16dp между кнопками
        ) {
            // Кнопки не меняют UI сами — они только вызывают методы ViewModel,
            // а ViewModel уже меняет uiState, и UI перерисовывается автоматически
            Button(onClick = { viewModel.decrement() }) {
                Text(text = "-", fontSize = 20.sp)
            }
            Button(onClick = { viewModel.reset() }) {
                Text(text = "Сброс")
            }
            Button(onClick = { viewModel.increment() }) {
                Text(text = "+", fontSize = 20.sp)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "История действий",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // LazyColumn: рендерит только видимые элементы, не все сразу.
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            // items() — проходит по списку uiState.history и для каждого элемента вызывает блок
            items(uiState.history) { entry ->
                Text(
                    text = entry,
                    modifier = Modifier.padding(vertical = 4.dp),
                    fontSize = 16.sp
                )
                HorizontalDivider()
            }
        }
    }
}