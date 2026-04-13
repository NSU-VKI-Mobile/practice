package ci.nsu.mobile.main.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.data.network.TokenManager
import ci.nsu.mobile.main.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        TokenManager.init(applicationContext)

        val editLogin: EditText = findViewById(R.id.editLogin)
        val editPassword: EditText = findViewById(R.id.editPassword)
        val buttonLogin: Button = findViewById(R.id.buttonLogin)
        val buttonToRegister: Button = findViewById(R.id.buttonToRegister)
        val progress: ProgressBar = findViewById(R.id.progressLogin)
        val textError: TextView = findViewById(R.id.textLoginError)

        editLogin.doAfterTextChanged { viewModel.onLoginChanged(it?.toString().orEmpty()) }
        editPassword.doAfterTextChanged { viewModel.onPasswordChanged(it?.toString().orEmpty()) }

        viewModel.uiState.observe(this) { state ->
            progress.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            textError.text = state.error.orEmpty()
            textError.visibility = if (state.error.isNullOrBlank()) View.GONE else View.VISIBLE
        }

        viewModel.navigateToMain.observe(this) { event ->
            if (event.getContentIfNotHandled() == null) return@observe
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        viewModel.navigateToRegister.observe(this) { event ->
            if (event.getContentIfNotHandled() == null) return@observe
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        buttonLogin.setOnClickListener { viewModel.onLoginClicked() }
        buttonToRegister.setOnClickListener { viewModel.onRegisterClicked() }
    }
}
