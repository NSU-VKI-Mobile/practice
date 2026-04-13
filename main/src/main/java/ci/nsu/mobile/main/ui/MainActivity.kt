package ci.nsu.mobile.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.data.network.TokenManager
import ci.nsu.mobile.main.ui.adapter.UsersAdapter
import ci.nsu.mobile.main.viewmodel.UsersViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val viewModel: UsersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TokenManager.init(applicationContext)

        val buttonRefresh: Button = findViewById(R.id.buttonRefreshUsers)
        val buttonLogout: Button = findViewById(R.id.buttonLogout)
        val progressBar: ProgressBar = findViewById(R.id.progressUsers)
        val textError: TextView = findViewById(R.id.textUsersError)
        val recyclerUsers: RecyclerView = findViewById(R.id.recyclerUsers)

        val adapter = UsersAdapter()
        recyclerUsers.layoutManager = LinearLayoutManager(this)
        recyclerUsers.adapter = adapter

        viewModel.uiState.observe(this) { state ->
            progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            textError.text = state.error.orEmpty()
            textError.visibility = if (state.error.isNullOrBlank()) View.GONE else View.VISIBLE
            adapter.submitList(state.users)
        }

        viewModel.navigateToLogin.observe(this) { event ->
            if (event.getContentIfNotHandled() == null) return@observe
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        buttonRefresh.setOnClickListener { viewModel.loadUsers() }
        buttonLogout.setOnClickListener { viewModel.logout() }

        viewModel.loadUsers()
    }
}
