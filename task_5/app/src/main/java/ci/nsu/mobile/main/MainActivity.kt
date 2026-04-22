package ci.nsu.mobile.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.data.db.DepositDatabase
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.ui.main.DepositViewModel
import ci.nsu.mobile.main.ui.main.MainFragment
import ci.nsu.mobile.main.viewmodel.DepositViewModelFactory

class MainActivity : AppCompatActivity() {

    // Make ViewModel accessible to fragments via activityViewModels()
    lateinit var depositViewModel: DepositViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize ViewModel
        val repository = DepositRepository(
            DepositDatabase.getDatabase(this).depositDao()
        )
        val factory = DepositViewModelFactory(repository)
        depositViewModel = ViewModelProvider(this, factory).get(DepositViewModel::class.java)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}