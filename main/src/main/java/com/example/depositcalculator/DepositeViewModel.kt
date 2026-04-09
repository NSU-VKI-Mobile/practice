package com.example.depositcalculator.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.depositcalculator.AppDatabase
import com.example.depositcalculator.DepositEntity
import com.example.depositcalculator.repository.DepositRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DepositeViewModel(application: Application) : AndroidViewModel(application){
    private val repository: DepositRepository
    val history: StateFlow<List<DepositEntity>>

    init {
        val dao = AppDatabase.getDatabase(application).depositDao()
        repository = DepositRepository(dao)

        history = repository.getAll()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    }

    fun saveDeposit(deposit: DepositEntity){
        viewModelScope.launch {
            repository.insert(deposit)
        }
    }
}