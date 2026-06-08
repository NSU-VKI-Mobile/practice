package ci.nsu.mobile.main

// sealed class хранит экраны нижнего меню.
// У каждого экрана есть route — путь для навигации,
// и title — название, которое видно пользователю.
sealed class BottomNavItem(
    val route: String,
    val title: String
) {
    object Home : BottomNavItem(
        route = "home",
        title = "Главная"
    )

    object Profile : BottomNavItem(
        route = "profile",
        title = "Профиль"
    )

    object Settings : BottomNavItem(
        route = "settings",
        title = "Настройки"
    )
}