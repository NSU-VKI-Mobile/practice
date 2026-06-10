package ci.nsu.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import ci.nsu.mobile.main.MainActivity
import ci.nsu.mobile.main.R
import ci.nsu.ui.common.NetworkResult
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModel()

    // Флаг текущего режима: true - вход, false - регистрация
    private var isLoginMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val etUsername = findViewById<TextInputEditText>(R.id.etUsername)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val tilEmail = findViewById<TextInputLayout>(R.id.tilEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnMainAction = findViewById<Button>(R.id.btnMainAction)
        val btnRegisterMode = findViewById<Button>(R.id.btnRegisterMode)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        // Логика переключения режимов экрана при нажатии на текстовую кнопку
        btnRegisterMode.setOnClickListener {
            isLoginMode = !isLoginMode
            viewModel.resetState() // Сбрасываем старые ошибки во ViewModel

            if (isLoginMode) {
                tvTitle.text = "Авторизация"
                btnMainAction.text = "Войти"
                btnRegisterMode.text = "Нет аккаунта? Зарегистрироваться"
                tilEmail.visibility = View.GONE // Скрываем Email
            } else {
                tvTitle.text = "Регистрация"
                btnMainAction.text = "Создать аккаунт"
                btnRegisterMode.text = "Уже есть аккаунт? Войти"
                tilEmail.visibility = View.VISIBLE // Показываем Email
            }
        }

        // Нажатие на главную кнопку
        btnMainAction.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (isLoginMode) {
                viewModel.login(username, password)
            } else {
                val email = etEmail.text.toString()
                viewModel.register(username, email, password)
            }
        }

        // Подписка на результаты запросов к API
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authState.collect { result ->
                    when (result) {
                        is NetworkResult.Loading -> {
                            progressBar.visibility = View.VISIBLE
                            btnMainAction.isEnabled = false
                        }
                        is NetworkResult.Success -> {
                            progressBar.visibility = View.GONE
                            val message = if (isLoginMode) "Успешный вход!" else "Регистрация успешна!"
                            Toast.makeText(this@AuthActivity, message, Toast.LENGTH_SHORT).show()

                            // Переход в главное приложение
                            startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                            finish()
                        }
                        is NetworkResult.Error -> {
                            progressBar.visibility = View.GONE
                            btnMainAction.isEnabled = true
                            Toast.makeText(this@AuthActivity, result.message, Toast.LENGTH_SHORT).show()
                        }
                        null -> {
                            progressBar.visibility = View.GONE
                            btnMainAction.isEnabled = true
                        }
                    }
                }
            }
        }
    }
}
