package ci.nsu.mobile.main.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.sl.ServiceLocator
import com.example.auth.vm.LoginAndRegViewModel
import com.example.calculations.vm.DepositsViewModel

class ViewModelFactory(
    private val serviceLocator: ServiceLocator
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginAndRegViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                LoginAndRegViewModel(
                    application = serviceLocator.appContext,
                    authRepository = serviceLocator.authRepository
                ) as T
            }
            modelClass.isAssignableFrom(DepositsViewModel::class.java) -> {
                @Suppress("UNCHECKED_CAST")
                DepositsViewModel(
                    application = serviceLocator.appContext,
                    depositRepository = serviceLocator.depositRepository
                ) as T
            }
            else -> throw IllegalArgumentException("Неизвестный ViewModel класс: ${modelClass.name}")
        }
    }
}