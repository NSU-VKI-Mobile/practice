package ci.nsu.mobile.domain.navigation


class BottomNavManagerImpl : IBottomNavManager {
    override fun isBottomBarVisible(currentRoute: String?): Boolean {
        return currentRoute in Screens.bottomBarScreens
    }

    override fun isLogoutVisible(currentRoute: String?): Boolean {
        return currentRoute in Screens.logoutScreens
    }
}