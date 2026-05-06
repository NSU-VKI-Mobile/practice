package data

import android.app.Application
import viewmodel.DepositViewModel

class SingletonDatabase() : Application()
{
    //lateinit - присовить значение позже (в OnCreate)
    private lateinit var database : AppDatabase
    private lateinit var repository : DepositRepository
    private lateinit var viewModel : DepositViewModel

    override fun onCreate()
    {
        super.onCreate()
        database = AppDatabase.getDatabase(this)
        repository = DepositRepository(database.depositDao())
        viewModel = DepositViewModel(repository)
    }

    fun getViewModel() : DepositViewModel
    {
        return viewModel
    }
}