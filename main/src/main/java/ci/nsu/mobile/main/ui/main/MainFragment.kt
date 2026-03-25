package ci.nsu.mobile.main.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import ci.nsu.mobile.main.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    // View элементы
    private lateinit var btnToggleMode: MaterialButton
    private lateinit var etInput: EditText
    private lateinit var etResult: EditText
    private lateinit var tilInput: TextInputLayout
    private lateinit var tilResult: TextInputLayout
    private lateinit var tvModeIndicator: TextView

    private var isUpdating = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupClickListeners()
        setupTextWatchers()
        observeViewModel()
    }

    private fun initViews(view: View) {
        btnToggleMode = view.findViewById(R.id.btnToggleMode)
        etInput = view.findViewById(R.id.etInput)
        etResult = view.findViewById(R.id.etResult)
        tilInput = view.findViewById(R.id.tilInput)
        tilResult = view.findViewById(R.id.tilResult)
        tvModeIndicator = view.findViewById(R.id.tvModeIndicator)

        // Нижнее поле только для чтения
        etResult.isEnabled = false
        etResult.keyListener = null
    }

    private fun setupClickListeners() {
        btnToggleMode.setOnClickListener {
            viewModel.toggleMode()
        }
    }

    private fun setupTextWatchers() {
        etInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (!isUpdating) {
                    isUpdating = true
                    viewModel.onInputChanged(s.toString())
                    isUpdating = false
                }
            }
        })
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                updateUI(state)
            }
        }
    }

    private fun updateUI(state: TemperatureUiState) {
        isUpdating = true

        // Обновляем текстовые поля
        if (etInput.text.toString() != state.inputValue) {
            etInput.setText(state.inputValue)
        }
        if (etResult.text.toString() != state.resultValue) {
            etResult.setText(state.resultValue)
        }

        // Обновляем подсказки
        tilInput.hint = state.inputHint
        tilResult.hint = state.resultHint

        // Обновляем текст на кнопке
        btnToggleMode.text = state.buttonText

        // Обновляем индикатор режима
        tvModeIndicator.text = state.modeText

        // Показываем ошибку валидации
        tilInput.error = if (state.inputValue.isNotBlank() && !state.isInputValid) {
            "Введите корректное число"
        } else {
            null
        }

        // Очищаем ошибку в поле результата (оно всегда валидно, если не пустое)
        tilResult.error = null

        isUpdating = false
    }
}