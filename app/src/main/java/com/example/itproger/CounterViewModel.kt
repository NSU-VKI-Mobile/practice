package com.example.itproger

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Весь экран описан одним объектом — "снимок" того, что видит пользователь прямо сейчас.
// data class удобен тем, что у него есть метод copy() — можно менять одно поле, не трогая остальные.
data class CounterUiState(
    val count: Int = 0,                     // текущее значение счётчика
    val history: List<String> = emptyList() // список последних действий
)

// ViewModel — это "мозг" экрана. Он живёт дольше, чем сам экран:
// при повороте телефона Activity пересоздаётся, а ViewModel — нет. Поэтому данные не теряются.
class CounterViewModel : ViewModel() {

    // _uiState — приватный, только ViewModel может в него писать (Mutable = можно менять)
    private val _uiState = MutableStateFlow(CounterUiState())

    // uiState — публичный для UI, но только для чтения
    // asStateFlow() как раз "запрещает" запись снаружи — UI может только смотреть, но не трогать
    val uiState: StateFlow<CounterUiState> = _uiState.asStateFlow()

    fun increment() {
        // update{} берёт текущее состояние и возвращает новое
        _uiState.update { currentState ->
            val newCount = currentState.count + 1
            // новая запись идёт в начало, take(4) берёт только первые 4 из старых — итого 5 штук максимум
            val newHistory = listOf("+1 (итого: $newCount)") + currentState.history.take(4)
            // copy() создаёт новый объект, меняя только то, что указали — остальное остаётся как было
            currentState.copy(count = newCount, history = newHistory)
        }
    }

    fun decrement() {
        _uiState.update { currentState ->
            val newCount = currentState.count - 1
            val newHistory = listOf("-1 (итого: $newCount)") + currentState.history.take(4)
            currentState.copy(count = newCount, history = newHistory)
        }
    }

    fun reset() {
        _uiState.update { currentState ->
            val newHistory = listOf("Сброс (итого: 0)") + currentState.history.take(4)
            currentState.copy(count = 0, history = newHistory)
        }
    }
}