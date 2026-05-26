package ci.nsu.mobile.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ci.nsu.mobile.main.data.db.AppDatabase
import ci.nsu.mobile.main.data.db.DepositCalculationEntity
import ci.nsu.mobile.main.data.repository.DepositRepository

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DepositRepository by lazy {
        val db = AppDatabase.getDatabase(getApplication())
        DepositRepository(db.depositCalculationDao())
    }

    val calculations: LiveData<List<DepositCalculationEntity>> = repository.observeAllCalculations()
}

