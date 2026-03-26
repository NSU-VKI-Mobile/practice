package ci.nsu.mobile.main.ui

data class CounterUiState(
    val count: Int = 0,
    val history: List<String> = emptyList()
)