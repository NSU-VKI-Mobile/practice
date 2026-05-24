package ci.nsu.mobile.main.ui.depositScreens

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ci.nsu.mobile.main.ui.components.CustomButton
import ci.nsu.mobile.main.ui.components.ShortHistoryItemCard
import ci.nsu.mobile.main.ui.components.TextFieldWithOptionalStar
import ci.nsu.mobile.main.viewmodel.historyDeposits.HistoryDepositsViewModel
import ci.nsu.mobile.main.viewmodel.historyDeposits.HistoryEvents
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreenContent(navToScreen: (String) -> Unit,
                         viewModel: HistoryDepositsViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val selectedState = state.selectedDeposit
    val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    val openDialog = remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.historyEvent(HistoryEvents.ResetFilter)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.historyEvent(HistoryEvents.LoadHistory)
    }
    Scaffold() { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Column(modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(modifier = Modifier.padding(horizontal = 30.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Фильтры")
                    Icon(
                        imageVector = if (state.isFilterOpen) {
                            Icons.Default.KeyboardArrowUp
                        } else {
                            Icons.Default.KeyboardArrowDown
                        },
                        contentDescription = if (state.isFilterOpen) "Свернуть" else "Развернуть",
                        modifier = Modifier
                            .clickable {
                                viewModel.historyEvent(HistoryEvents.IsFilterOpenUpdate(!state.isFilterOpen))
                            }
                    )
                }
                if (state.isFilterOpen) {
                    Column(modifier = Modifier.fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp, top = 10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                        .padding(10.dp),
                        horizontalAlignment = Alignment.Start) {
                        Text("Итоговая сумма", modifier = Modifier.padding(bottom = 5.dp), color = MaterialTheme.colorScheme.onSurface)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextField(
                                value = state.startAmount,
                                onValueChange = { viewModel.historyEvent(HistoryEvents.StartAmountChanged(it)) },
                                placeholder = { Text("От")},
                                label = { Text("От")},
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.width(120.dp),
                                isError = "start" in state.errorFieldsAmount,
                                supportingText = {
                                    if (state.errorFieldsAmount.contains("start")) {
                                        Text("Проверьте поле")
                                    }
                                }
                            )
                            TextField(
                                value = state.endAmount,
                                onValueChange = { viewModel.historyEvent(HistoryEvents.EndAmountChanged(it)) },
                                placeholder = { Text("До") },
                                label = { Text("До") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.width(120.dp),
                                isError = "end" in state.errorFieldsAmount,
                                supportingText = {
                                    if (state.errorFieldsAmount.contains("end")) {
                                        Text("Проверьте поле", fontSize = 10.sp)
                                    }
                                }
                            )
                        }
                    }
                    Column(modifier = Modifier.fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp, top = 10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                        .padding(10.dp),
                        horizontalAlignment = Alignment.Start) {
                        Text("Дата", modifier = Modifier.padding(bottom = 5.dp), color = MaterialTheme.colorScheme.onSurface)
                        TextFieldWithOptionalStar(
                            value = state.date,
                            onValueChange = {
                                viewModel.historyEvent(HistoryEvents.DateChanged(it))
                            },
                            hasStar = false,
                            placeholder = "Дата",
                            readOnly = true,
                            trailingIcon = {
                                IconButton({viewModel.historyEvent(HistoryEvents.DatePickerVisibilityChanged(!state.showDatePicker))}) {
                                    Icon(
                                        Icons.Filled.DateRange, "Выбрать дату"
                                    )
                                }
                            }
                        )
                        if (state.showDatePicker) {
                            DatePickerDialog(
                                onDismissRequest = {
                                    viewModel.historyEvent(HistoryEvents.DatePickerVisibilityChanged(false))
                                },
                                confirmButton = {
                                    CustomButton(
                                        onClick = {
                                            datePickerState.selectedDateMillis?.let { millis ->
                                                val formattedDate = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                                                    .format(Date(millis))
                                                viewModel.historyEvent(HistoryEvents.DateChanged(formattedDate))
                                                viewModel.historyEvent(HistoryEvents.DateMillisChanged(millis))
                                            }
                                            viewModel.historyEvent(HistoryEvents.DatePickerVisibilityChanged(false))
                                        },
                                        title = "OK"
                                    )
                                },
                                dismissButton = {
                                    CustomButton(
                                        onClick = {
                                            viewModel.historyEvent(HistoryEvents.DatePickerVisibilityChanged(false))
                                        },
                                        title = "Отмена"
                                    )
                                }
                            ) {
                                DatePicker(
                                    state = datePickerState,
                                    modifier = Modifier.sizeIn(maxWidth = 350.dp)
                                )
                            }
                        }
                    }
                    Column(modifier = Modifier.fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp, top = 10.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                        .padding(10.dp),
                        horizontalAlignment = Alignment.Start) {
                        Text("Процентная ставка", modifier = Modifier.padding(bottom = 5.dp), color = MaterialTheme.colorScheme.onSurface)
                        ExposedDropdownMenuBox(
                            expanded = state.showDDMenu,
                            onExpandedChange = { viewModel.historyEvent(HistoryEvents.MenuStateChanged(!state.showDDMenu)) },
                        ) {
                            TextField(
                                value = state.selectedRate.toString(),
                                onValueChange = {},
                                placeholder = { Text("Процентая ставка")},
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.showDDMenu)
                                },
                                modifier = Modifier.menuAnchor(
                                    ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                    enabled = true).padding(bottom = 20.dp).fillMaxWidth(0.81f),
                                readOnly = true
                            )
                            ExposedDropdownMenuBox(
                                expanded = state.showDDMenu,
                                onExpandedChange = { viewModel.historyEvent(HistoryEvents.MenuStateChanged(!state.showDDMenu)) },
                            ) {
                                TextField(
                                    value = when {
                                        state.selectedRate != null -> "${state.selectedRate}%"
                                        else -> "Все"
                                    },
                                    onValueChange = {},
                                    enabled = false,
                                    placeholder = { Text("Процентная ставка") },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.showDDMenu)
                                    },
                                    modifier = Modifier
                                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
                                )
                                ExposedDropdownMenu(
                                    expanded = state.showDDMenu,
                                    onDismissRequest = { viewModel.historyEvent(HistoryEvents.MenuStateChanged(false)) }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Все") },
                                        onClick = {
                                            viewModel.historyEvent(HistoryEvents.SelectedRateChanged(null))  // ← null = все
                                            viewModel.historyEvent(HistoryEvents.MenuStateChanged(false))
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                    )
                                    state.rates.forEach { rate ->
                                        DropdownMenuItem(
                                            text = { Text("${rate}%") },
                                            onClick = {
                                                viewModel.historyEvent(HistoryEvents.SelectedRateChanged(rate))
                                                viewModel.historyEvent(HistoryEvents.MenuStateChanged(false))
                                            },
                                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth().padding(30.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically) {
                        CustomButton(
                            onClick = {
                                viewModel.historyEvent(HistoryEvents.ResetFilter)
                            },
                            title = "Сбросить"
                        )
                        CustomButton(
                            onClick = {
                                viewModel.historyEvent(HistoryEvents.ValidationFilter)
                                viewModel.historyEvent(HistoryEvents.FilterUp)
                                viewModel.historyEvent(HistoryEvents.IsFilterOpenUpdate(false))
                            },
                            title = "Применить"
                        )
                    }
                }
            }
            LazyColumn(
                modifier = Modifier.padding(10.dp).weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(state.deposits) { deposit ->
                    ShortHistoryItemCard(
                        deposit = deposit,
                        dateFormat = dateFormat,
                        Click = {
                            viewModel.historyEvent(HistoryEvents.SelectedDepositUpdate(deposit))
                            openDialog.value = true
                        }
                    )
                }
            }
        }
        if (openDialog.value && selectedState != null) {
            AlertDialog(
                onDismissRequest = { openDialog.value = false },
                title = { Text(text = "INFO about deposit") },
                text = {
                    Column() {
                        Text(
                            "Стартовый взнос: ${selectedState.initialAmount}₽",
                            Modifier.padding(vertical = 10.dp, horizontal = 20.dp)
                        )
                        Text(
                            "Срок вклада (в месяцах): ${selectedState.periodMonths}",
                            Modifier.padding(20.dp, 0.dp)
                        )
                        Text(
                            "Процентная ставка: ${selectedState.interestRate}%",
                            Modifier.padding(20.dp, 0.dp)
                        )
                        val mot = selectedState.monthlyTopUp
                        if (mot == null) {
                            Text("Ежемесячное пополнение: 0₽", Modifier.padding(20.dp,  0.dp))
                        }
                        else {
                            Text("Ежемесячное пополнение: ${mot}₽", Modifier.padding(20.dp,  0.dp))
                        }
                        Text(
                            "Итоговая сумма: ${String.format("%.2f", selectedState.finalAmount)}₽",
                            Modifier.padding(20.dp, 0.dp)
                        )
                        Text(
                            "Начисленные проценты: ${String.format("%.2f",selectedState.interestEarned)}₽",
                            Modifier.padding(20.dp, 0.dp)
                        )
                        Text(
                            "Дата и время рассчета: ${dateFormat.format(Date(selectedState.calculationDate))}",
                            Modifier.padding(20.dp)
                        )
                    }
                },
                confirmButton = {
                    Row(modifier = Modifier.padding(10.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically) {
                        CustomButton(
                            onClick = {
                                viewModel.historyEvent(HistoryEvents.DeleteDeposit(selectedState))
                                openDialog.value = false
                        },
                            title = "Удалить",
                            modifier = Modifier.width(120.dp)
                        )
                        CustomButton(
                            onClick = { openDialog.value = false },
                            title = "OK",
                            modifier = Modifier.width(120.dp)
                        )
                    }
                }
            )
        }
    }

}

