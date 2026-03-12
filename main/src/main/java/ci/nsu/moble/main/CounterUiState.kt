package ci.nsu.moble.main

data class CounterUiState(
    val count: Int = 0, //история последних действий и счетчик
    val history: List<String> = emptyList()
)