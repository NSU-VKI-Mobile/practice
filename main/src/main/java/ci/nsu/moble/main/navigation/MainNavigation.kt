package ci.nsu.moble.main.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.MutableValue
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
sealed class Screen : Parcelable {
    object Home : Screen()
    object Info : Screen()
    object Settings : Screen()
    data class Fourth(val text: String) : Screen()
}

interface MainNavigator {
    val currentScreen: Value<Screen>

    fun goToFourth(data: String)
    fun goBack()
    fun goHome()
    fun goToInfo()
    fun goToSettings()
}

class MainNavigatorImpl(
    componentContext: ComponentContext
) : MainNavigator, ComponentContext by componentContext {

    private val _currentScreen = MutableValue<Screen>(Screen.Home)
    override val currentScreen: Value<Screen> = _currentScreen

    override fun goToFourth(data: String) {
        _currentScreen.value = Screen.Fourth(data)
    }

    override fun goBack() {
        _currentScreen.value = Screen.Home
    }

    override fun goHome() {
        _currentScreen.value = Screen.Home
    }

    override fun goToInfo() {
        _currentScreen.value = Screen.Info
    }

    override fun goToSettings() {
        _currentScreen.value = Screen.Settings
    }
}