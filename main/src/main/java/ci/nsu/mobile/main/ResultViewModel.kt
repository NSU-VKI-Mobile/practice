package ci.nsu.mobile.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

//класс для управления UI-данными
class ResultViewModel(application: Application) : AndroidViewModel(application) {
    //(application: Application) — принимает контекст приложения
    //: AndroidViewModel(application) — наследуется от AndroidViewModel (имеет доступ к контексту)
    private val database = AppDatabase.getDatabase(application) //Получаем экземпляр базы данных
    private val repository = DepositRepository(database.depositDao()) //Создаём репозиторий, передаём ему DAO

    fun saveCalculation(
        initialAmount: Double,
        periodMonths: Int,
        interestRate: Double,
        monthlyTopUp: Double,
        finalAmount: Double,
        interestEarned: Double,
        onSuccess: (Long) -> Unit
    ) {
        viewModelScope.launch { // корутина для фоновой работы
            val calculation = DepositCalculation(
                initialAmount = initialAmount,
                periodMonths = periodMonths,
                interestRate = interestRate,
                monthlyTopUp = monthlyTopUp,
                finalAmount = finalAmount,
                interestEarned = interestEarned
            )
            val id = repository.saveCalculation(calculation)  // ← получаем ID
            android.util.Log.d("ResultViewModel", "Сохранение завершено, получен ID = $id")
            onSuccess(id)
        }
    }
}