package ci.nsu.mobile.main.ui

sealed class Screen {
    object Main : Screen()
    object Stage1 : Screen()
    object Stage2 : Screen()
    object Result : Screen()
    object History : Screen()
    object HistoryDetail : Screen()
}