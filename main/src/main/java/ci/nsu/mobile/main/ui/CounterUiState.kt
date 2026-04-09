package ci.nsu.mobile.main.ui

import androidx.compose.runtime.Immutable

@Immutable
data class CounterUiState(
    val count: Int = 0,
    val history: List<String> = emptyList()
)