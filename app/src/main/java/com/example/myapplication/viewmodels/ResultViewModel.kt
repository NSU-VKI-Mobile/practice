package com.example.myapplication.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.dbo.AppDatabase
import com.example.myapplication.entities.DepositEntity
import kotlinx.coroutines.launch

class ResultViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.Companion.getDatabase(application).dao()

    fun calculate(p: Double, m: Int, r: Double, add: Double): DepositEntity {
        val monthlyRate = r / 100 / 12
        var total = p
        repeat(m) {
            total = (total + add) * (1 + monthlyRate)
        }
        return DepositEntity(
            amount = p, months = m, rate = r,
            monthlyAdd = add, total = total, interest = total - p - (add * m)
        )
    }

    fun save(deposit: DepositEntity) = viewModelScope.launch {
        dao.insert(deposit)
    }
}