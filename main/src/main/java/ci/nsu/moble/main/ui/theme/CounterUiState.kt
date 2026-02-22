package ci.nsu.moble.main.ui.theme

data class CounterUiState(
    val count: Int = 0,
    val history: List<String> = emptyList()
)
