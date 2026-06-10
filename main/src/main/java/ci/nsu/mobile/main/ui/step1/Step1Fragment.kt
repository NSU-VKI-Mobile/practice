package ci.nsu.mobile.main.ui.step1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.databinding.FragmentStep1Binding
import ci.nsu.mobile.main.data.model.DepositInputData
import androidx.navigation.fragment.findNavController

class Step1Fragment : Fragment() {

    private var _binding: FragmentStep1Binding? = null
    private val binding get() = _binding!!
    private val viewModel: Step1ViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStep1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Восстановление значений при повороте
        viewModel.initialAmount.observe(viewLifecycleOwner) { amount ->
            if (amount != null && binding.editInitialAmount.text.toString().isEmpty()) {
                binding.editInitialAmount.setText(amount.toString())
            }
        }
        viewModel.periodMonths.observe(viewLifecycleOwner) { months ->
            if (months != null && binding.editPeriodMonths.text.toString().isEmpty()) {
                binding.editPeriodMonths.setText(months.toString())
            }
        }

        // Отображение ошибки
        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            binding.textError.text = errorMsg ?: ""
            binding.textError.visibility = if (errorMsg.isNullOrEmpty()) View.GONE else View.VISIBLE
        }

        // Слушатели изменения текста
        binding.editInitialAmount.addTextChangedListener { text ->
            viewModel.updateInitialAmount(text.toString())
        }
        binding.editPeriodMonths.addTextChangedListener { text ->
            viewModel.updatePeriodMonths(text.toString())
        }

        // Кнопка "Далее"
        binding.buttonNext.setOnClickListener {
            if (viewModel.isInputValid()) {
                val depositData = DepositInputData(
                    initialAmount = viewModel.initialAmount.value!!,
                    periodMonths = viewModel.periodMonths.value!!
                )
                val action = Step1FragmentDirections.actionStep1ToStep2(depositData)
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireContext(), "Заполните поля корректно", Toast.LENGTH_SHORT).show()
            }
        }

        // Кнопка "В начало" - возврат на главный экран
        binding.buttonToStart.setOnClickListener {
            view.findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}