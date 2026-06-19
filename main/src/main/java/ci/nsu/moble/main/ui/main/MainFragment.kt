package ci.nsu.moble.main.ui.main

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import ci.nsu.moble.main.R
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()

    // Объявляем UI элементы
    private lateinit var textView: TextView
    private lateinit var counterTextView: TextView
    private lateinit var messageTextView: TextView
    private lateinit var editText: EditText
    private lateinit var incrementButton: Button
    private lateinit var updateButton: Button
    private lateinit var loadDataButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация UI элементов
        initViews(view)

        // Настройка наблюдателей
        setupObservers()

        // Настройка слушателей
        setupListeners()

        // Загрузка начальных данных
        viewModel.loadInitialData()
    }

    private fun initViews(view: View) {
        textView = view.findViewById(R.id.textView)
        counterTextView = view.findViewById(R.id.counterTextView)
        messageTextView = view.findViewById(R.id.messageTextView)
        editText = view.findViewById(R.id.editText)
        incrementButton = view.findViewById(R.id.incrementButton)
        updateButton = view.findViewById(R.id.updateButton)
        loadDataButton = view.findViewById(R.id.loadDataButton)
        progressBar = view.findViewById(R.id.progressBar)
    }

    private fun setupObservers() {
        // Наблюдаем за текстом
        viewModel.text.observe(viewLifecycleOwner) { text ->
            textView.text = text
        }

        // Наблюдаем за счетчиком
        viewModel.counter.observe(viewLifecycleOwner) { count ->
            counterTextView.text = "Count: $count"
        }

        // Наблюдаем за состоянием загрузки
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Наблюдаем за UI состоянием (StateFlow)
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                // Обновляем сообщение
                if (state.message.isNotEmpty()) {
                    messageTextView.text = state.message
                }

                // Показываем ошибку
                state.error?.let { error ->
                    Toast.makeText(requireContext(), "Ошибка: $error", Toast.LENGTH_SHORT).show()
                    viewModel.clearError()
                }
            }
        }
    }

    private fun setupListeners() {
        // Кнопка увеличения счетчика
        incrementButton.setOnClickListener {
            viewModel.onButtonClick()
        }

        // Кнопка обновления текста
        updateButton.setOnClickListener {
            val newText = editText.text.toString()
            if (newText.isNotEmpty()) {
                viewModel.updateText(newText)
                editText.text.clear()
            } else {
                Toast.makeText(requireContext(), "Введите текст", Toast.LENGTH_SHORT).show()
            }
        }

        // Кнопка загрузки данных
        loadDataButton.setOnClickListener {
            viewModel.loadInitialData()
        }
    }
}