package ci.nsu.mobile.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import ci.nsu.mobile.main.data.db.AppDatabase
import ci.nsu.mobile.main.data.db.DepositCalculationEntity
import ci.nsu.mobile.main.data.repository.DepositRepository

class DepositDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DepositRepository by lazy {
        val db = AppDatabase.getDatabase(getApplication())
        DepositRepository(db.depositCalculationDao())
    }

    private val idLiveData = MutableLiveData<Long?>(null)
    val calculation: LiveData<DepositCalculationEntity?> = idLiveData.switchMap { id ->
        if (id == null) MutableLiveData(null) else repository.observeCalculationById(id)
    }

    private var initialized = false

    fun setIdOnce(id: Long) {
        if (initialized) return
        initialized = true
        idLiveData.value = id
    }
}

