package ci.nsu.mobile.domain.navigation

interface IBottomNavManager {
    fun isBottomBarVisible(currentRoute: String?): Boolean
    fun isLogoutVisible(currentRoute: String?): Boolean
}