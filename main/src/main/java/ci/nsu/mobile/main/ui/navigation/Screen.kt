package ci.nsu.mobile.main.ui.navigation

sealed class Screen(val route: String) {
    object Main : Screen("main")

    object Calculation : Screen("calculation")
    object Step1 : Screen("calculation_step1")
    object Step2 : Screen("calculation_step2")
    object Result : Screen("calculation_result")

    object History : Screen("history")
}