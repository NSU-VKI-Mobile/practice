package ci.nsu.mobile.main.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.viewmodel.RegisterViewModel

class RegisterActivity : AppCompatActivity() {
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val firstName: EditText = findViewById(R.id.editFirstName)
        val lastName: EditText = findViewById(R.id.editLastName)
        val middleName: EditText = findViewById(R.id.editMiddleName)
        val birthDate: EditText = findViewById(R.id.editBirthDate)
        val gender: EditText = findViewById(R.id.editGender)
        val login: EditText = findViewById(R.id.editRegisterLogin)
        val password: EditText = findViewById(R.id.editRegisterPassword)
        val email: EditText = findViewById(R.id.editEmail)
        val phone: EditText = findViewById(R.id.editPhone)
        val groupSpinner: Spinner = findViewById(R.id.spinnerGroups)
        val buttonRegister: Button = findViewById(R.id.buttonRegister)
        val progress: ProgressBar = findViewById(R.id.progressRegister)
        val message: TextView = findViewById(R.id.textRegisterMessage)

        fun syncInputs() {
            viewModel.onTextChanged(
                firstName = firstName.text.toString(),
                lastName = lastName.text.toString(),
                middleName = middleName.text.toString(),
                birthDate = birthDate.text.toString(),
                gender = gender.text.toString(),
                login = login.text.toString(),
                password = password.text.toString(),
                email = email.text.toString(),
                phone = phone.text.toString()
            )
        }

        val watchers = listOf(
            firstName, lastName, middleName, birthDate, gender, login, password, email, phone
        )
        watchers.forEach { edit -> edit.doAfterTextChanged { syncInputs() } }

        viewModel.uiState.observe(this) { state ->
            progress.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            message.text = state.message.orEmpty()
            message.visibility = if (state.message.isNullOrBlank()) View.GONE else View.VISIBLE

            val groups = state.groups
            val labels = groups.map { "${it.id ?: "-"} - ${it.name ?: "Без названия"}" }
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                if (labels.isEmpty()) listOf("Группы не загружены") else labels
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            groupSpinner.adapter = adapter
            groupSpinner.isEnabled = groups.isNotEmpty()

            val selectedId = state.selectedGroupId
            if (selectedId != null && groups.isNotEmpty()) {
                val selectedIndex = groups.indexOfFirst { it.id == selectedId }
                if (selectedIndex >= 0) {
                    groupSpinner.setSelection(selectedIndex)
                }
            }
        }

        groupSpinner.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                val groupId = (viewModel.uiState.value?.groups ?: emptyList()).getOrNull(position)?.id
                viewModel.onGroupSelected(groupId)
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) = Unit
        })

        viewModel.registrationCompleted.observe(this) { event ->
            if (event.getContentIfNotHandled() == null) return@observe
            Toast.makeText(this, "Регистрация успешна", Toast.LENGTH_SHORT).show()
            finish()
        }

        buttonRegister.setOnClickListener {
            syncInputs()
            viewModel.onRegisterClicked()
        }

        viewModel.loadGroups()
    }
}
