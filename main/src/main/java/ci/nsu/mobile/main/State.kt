package ci.nsu.mobile.main

sealed class State (val route: String) {
    object Main : State("Main")
    object Second : State("Second")
}