package ci.nsu.mobile.main.core.base

sealed class AppEvent {
    data class Navigate(val route: String) : AppEvent()
    class Exit() : AppEvent()
}