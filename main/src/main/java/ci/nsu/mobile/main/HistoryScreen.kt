package ci.nsu.mobile.main

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    onNavigateToDetail: (Long) -> Unit,
    viewModel: HistoryViewModel = viewModel()
) {
    val context = LocalContext.current
    val calculations by viewModel.allCalculations.collectAsState(initial = emptyList())

    // Состояние для диалога подтверждения удаления
    var showDeleteDialog by remember { mutableStateOf(false) }
    var calculationToDelete by remember { mutableStateOf<DepositCalculation?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "История расчётов",
            fontSize = 24.sp,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (calculations.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Нет сохранённых расчётов",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(calculations) { calculation ->
                    HistoryItem(
                        calculation = calculation,
                        onClick = { onNavigateToDetail(calculation.id) },
                        onDeleteClick = {
                            calculationToDelete = calculation
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }
    }

    // Диалог подтверждения удаления
    if (showDeleteDialog && calculationToDelete != null) {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
                calculationToDelete = null
            },
            title = { Text("Удаление расчёта") },
            text = {
                Text("Вы действительно хотите удалить расчёт от ${dateFormat.format(Date(calculationToDelete!!.calculationDate))} на сумму ${String.format("%.2f", calculationToDelete!!.initialAmount)} ₽?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteCalculation(
                            calculation = calculationToDelete!!,
                            onSuccess = {
                                Toast.makeText(context, "Расчёт удалён", Toast.LENGTH_SHORT).show()
                                showDeleteDialog = false
                                calculationToDelete = null
                            }
                        )
                    }
                ) {
                    Text("Удалить", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        calculationToDelete = null
                    }
                ) {
                    Text("Отмена")
                }
            }
        )
    }
}

@Composable
fun HistoryItem(
    calculation: DepositCalculation,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Левая часть: информация о расчёте
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "ID: ${calculation.id}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = String.format("%.2f ₽", calculation.initialAmount),
                    fontSize = 18.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                Text(
                    text = "→ ${String.format("%.2f ₽", calculation.finalAmount)}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Правая часть: срок, дата и иконка удаления
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${calculation.periodMonths} мес.",
                    fontSize = 14.sp
                )
                Text(
                    text = dateFormat.format(Date(calculation.calculationDate)),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Иконка корзины для удаления
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Удалить",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}