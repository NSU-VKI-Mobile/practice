package ci.nsu.mobile.main.ui.details

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import ci.nsu.mobile.main.data.db.AppDatabase
import ci.nsu.mobile.main.data.entity.DepositCalculation
import ci.nsu.mobile.main.data.repository.DepositRepository

class DetailsViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val repository: DepositRepository by lazy {
        val dao = AppDatabase.getInstance(application).depositDao()
        DepositRepository(dao)
    }

    private val calculationId: Long = savedStateHandle.get<Long>("calculationId") ?: 0L

    val calculation: LiveData<DepositCalculation?> = repository.getById(calculationId)
}
