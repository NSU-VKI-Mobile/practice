package ci.nsu.mobile.main

// Состояние нашего экрана. Содержит текущее число и список строк с историей.
data class CounterUiState(
    val count: Int = 0,
    val history: List<String> = emptyList()
)