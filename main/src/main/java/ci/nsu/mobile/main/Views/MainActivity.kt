package ci.nsu.mobile.main.Views

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.ViewModels.MainActivityViewModel
import ci.nsu.mobile.main.ui.theme.PracticeTheme

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Инициализируем ViewModel (фабрика с SavedStateHandle)
        viewModel = ViewModelProvider(
            this, SavedStateViewModelFactory(application, this)
        )[MainActivityViewModel::class.java]

        setContent {
            PracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val login by viewModel.login.collectAsState()
                    val password by viewModel.password.collectAsState()
                    MainActivityScreen(
                        login = login,
                        onLoginChange = { viewModel.updateLogin(it) },
                        password = password,
                        onPasswordChange = { viewModel.updatePassword(it) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainActivityScreen(
    login: String,
    onLoginChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        OutlinedTextField(
            value = login,
            onValueChange = onLoginChange,
            label = {Text("Логин")},
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = {Text("Пароль")},
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick =
            {

            }) { Text("Войти") }
        Spacer(modifier = Modifier.height(16.dp))
        val annotatedText = buildAnnotatedString {
            append("Нет аккаунта?\t")
            withLink(
                LinkAnnotation.Clickable(
                    tag = "register",  // уникальный идентификатор (может быть любым)
                    linkInteractionListener = {
                        // здесь выполняется переход
                        val intent = Intent(context, RegistrationActivity::class.java)
                        context.startActivity(intent)
                    },
                    styles = TextLinkStyles(
                        style = SpanStyle(
                            color = androidx.compose.ui.graphics.Color.Blue,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                )
            ) {
                append("Зарегистрироваться")
            }
        }

        BasicText(text = annotatedText)
    }
}