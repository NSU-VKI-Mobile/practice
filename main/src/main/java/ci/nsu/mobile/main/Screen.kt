package ci.nsu.mobile.main

sealed class Screen(
    val route: String
) {

    object Home : Screen("home")

    object Step1 : Screen("step1")

    object Step2 : Screen("step2")

    object Result : Screen("result")

    object History : Screen("history")
}