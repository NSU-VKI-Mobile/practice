package ci.nsu.mobile.main.ui.secondstep

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.databinding.FragmentSecondStepBinding
import ci.nsu.mobile.main.ui.firststep.FirstStepViewModel
import ci.nsu.mobile.main.utils.Validator

class SecondStepFragment : Fragment() {

    private var _binding: FragmentSecondStepBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SecondStepViewModel
    private lateinit var firstStepViewModel: FirstStepViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondStepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация ViewModels
        viewModel = ViewModelProvider(this)[SecondStepViewModel::class.java]
        firstStepViewModel = ViewModelProvider(requireActivity())[FirstStepViewModel::class.java]

        // Получаем данные с первого экрана
        val periodMonths = firstStepViewModel.getPeriodMonths()
        val initialAmount = firstStepViewModel.getInitialAmount()

        // Логирование для проверки
        android.util.Log.d("SecondStepFragment", "=== ПОЛУЧЕННЫЕ ДАННЫЕ ===")
        android.util.Log.d("SecondStepFragment", "Стартовый взнос: $initialAmount")
        android.util.Log.d("SecondStepFragment", "Срок в месяцах: $periodMonths")

        // Проверка: если данные не переданы, показываем ошибку
        if (initialAmount <= 0) {
            Toast.makeText(requireContext(), "Ошибка: не передан стартовый взнос. Вернитесь на первый экран.", Toast.LENGTH_LONG).show()
        }

        if (periodMonths <= 0) {
            Toast.makeText(requireContext(), "Ошибка: не передан срок вклада. Вернитесь на первый экран.", Toast.LENGTH_LONG).show()
        }

        // Расчёт доступной процентной ставки
        val availableRate = if (periodMonths > 0) {
            Validator.calculateInterestRate(periodMonths)
        } else {
            0.0
        }

        android.util.Log.d("SecondStepFragment", "Доступная ставка: $availableRate%")

        // Настройка выпадающего списка для процентной ставки
        val rates = listOf("$availableRate%")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, rates)
        binding.interestRateSpinner.setAdapter(adapter)
        binding.interestRateSpinner.setText("$availableRate%", false)
        binding.interestRateSpinner.isEnabled = false  // Блокируем выбор, так как ставка автоматическая

        // Кнопка "Назад" - возврат к первому этапу
        binding.backButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_secondStepFragment_to_firstStepFragment)
        }

        // Кнопка "Рассчитать"
        binding.calculateButton.setOnClickListener {
            val monthlyTopUp = binding.monthlyTopUpInput.text.toString()
            val topUpResult = Validator.validateMonthlyTopUp(monthlyTopUp)

            if (topUpResult is Validator.Result.Error) {
                showError(topUpResult.message)
            } else {
                val monthlyTopUpValue = (topUpResult as Validator.Result.Success).data

                // Повторно получаем данные (на случай если они обновились)
                val finalInitialAmount = firstStepViewModel.getInitialAmount()
                val finalPeriodMonths = firstStepViewModel.getPeriodMonths()
                val finalInterestRate = Validator.calculateInterestRate(finalPeriodMonths)

                android.util.Log.d("SecondStepFragment", "=== ДАННЫЕ ДЛЯ РАСЧЁТА ===")
                android.util.Log.d("SecondStepFragment", "Стартовый взнос: $finalInitialAmount")
                android.util.Log.d("SecondStepFragment", "Срок: $finalPeriodMonths")
                android.util.Log.d("SecondStepFragment", "Ставка: $finalInterestRate")
                android.util.Log.d("SecondStepFragment", "Пополнение: $monthlyTopUpValue")

                if (finalInitialAmount <= 0) {
                    showError("Ошибка: не указан стартовый взнос")
                    return@setOnClickListener
                }

                if (finalPeriodMonths <= 0) {
                    showError("Ошибка: не указан срок вклада")
                    return@setOnClickListener
                }

                // Выполняем расчёт
                val result = Validator.calculateDeposit(
                    finalInitialAmount,
                    finalPeriodMonths,
                    finalInterestRate,
                    monthlyTopUpValue
                )

                android.util.Log.d("SecondStepFragment", "=== РЕЗУЛЬТАТ РАСЧЁТА ===")
                android.util.Log.d("SecondStepFragment", "Итоговая сумма: ${result.finalAmount}")
                android.util.Log.d("SecondStepFragment", "Начисленные проценты: ${result.interestEarned}")

                // Сохраняем результат в ViewModel
                viewModel.saveResult(
                    finalInitialAmount,
                    finalPeriodMonths,
                    finalInterestRate,
                    monthlyTopUpValue,
                    result.finalAmount,
                    result.interestEarned
                )

                // Переход на экран результата
                view.findNavController().navigate(R.id.action_secondStepFragment_to_resultFragment)
            }
        }
    }

    private fun showError(message: String) {
        binding.errorTextView.text = message
        binding.errorTextView.visibility = View.VISIBLE
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}