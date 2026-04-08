package ci.nsu.moble.main

sealed class Screens(val route: String, val title: String) {
    object Home : Screens("home", "Home")
    object First : Screens("first", "Screen One")
    object Second : Screens("second", "Screen Two")
}