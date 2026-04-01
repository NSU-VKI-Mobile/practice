package ci.nsu.mobile.main

data class CounterUiState(
    val count: Int = 0,
    val history: List<String> = emptyList()
)