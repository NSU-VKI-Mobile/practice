package ci.nsu.moble.main

data class CounterUiState(
    val count: Int = 0,
    val history: List<String> = emptyList()
)