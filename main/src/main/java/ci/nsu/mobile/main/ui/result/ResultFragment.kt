package ci.nsu.mobile.main.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import ci.nsu.mobile.mai.database.DepositCalculation
import ci.nsu.mobile.main.R

import ci.nsu.mobile.main.databinding.FragmentResultBinding
import ci.nsu.mobile.main.repository.DepositRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: DepositRepository

    // Переменные для хранения данных
    private var initialAmount: Double = 0.0
    private var periodMonths: Int = 0
    private var interestRate: Double = 0.0
    private var monthlyTopUp: Double? = null
    private var finalAmount: Double = 0.0
    private var interestEarned: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = DepositRepository.getInstance(requireContext())

        // ПОЛУЧАЕМ ДАННЫЕ СО ВТОРОГО ЭКРАНА
        arguments?.let {
            initialAmount = it.getDouble("initialAmount", 0.0)
            periodMonths = it.getInt("periodMonths", 0)
            interestRate = it.getDouble("interestRate", 0.0)
            if (it.containsKey("monthlyTopUp")) {
                monthlyTopUp = it.getDouble("monthlyTopUp")
            }
            finalAmount = it.getDouble("finalAmount", 0.0)
            interestEarned = it.getDouble("interestEarned", 0.0)
        }

        android.util.Log.d("ResultFragment", "Получено: initialAmount=$initialAmount, periodMonths=$periodMonths, finalAmount=$finalAmount")

        // Проверка получения данных
        if (initialAmount <= 0 || finalAmount <= 0) {
            Toast.makeText(requireContext(), "Ошибка: сначала выполните расчёт на предыдущем экране", Toast.LENGTH_LONG).show()
            binding.initialAmountText.text = "Ошибка: нет данных для расчёта"
            return
        }

        displayResult()

        binding.saveButton.setOnClickListener {
            saveCalculation()
        }

        binding.backToStartButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_resultFragment_to_mainFragment)
        }
    }

    private fun displayResult() {
        binding.initialAmountText.text = "Стартовый взнос: ${String.format("%.2f", initialAmount)} ₽"
        binding.periodText.text = "Срок: $periodMonths месяцев"
        binding.interestRateText.text = "Процентная ставка: ${String.format("%.1f", interestRate)}%"

        val monthlyTopUpText = if (monthlyTopUp != null && monthlyTopUp!! > 0) {
            "${String.format("%.2f", monthlyTopUp)} ₽"
        } else {
            "Не указано"
        }
        binding.monthlyTopUpText.text = "Ежемесячное пополнение: $monthlyTopUpText"

        binding.finalAmountText.text = "Итоговая сумма: ${String.format("%.2f", finalAmount)} ₽"
        binding.interestEarnedText.text = "Начисленные проценты: ${String.format("%.2f", interestEarned)} ₽"
    }

    private fun saveCalculation() {
        if (initialAmount <= 0) {
            Toast.makeText(requireContext(), "Нет данных для сохранения", Toast.LENGTH_SHORT).show()
            return
        }

        val calculation = DepositCalculation(
            initialAmount = initialAmount,
            periodMonths = periodMonths,
            interestRate = interestRate,
            monthlyTopUp = monthlyTopUp,
            finalAmount = finalAmount,
            interestEarned = interestEarned,
            calculationDate = System.currentTimeMillis()
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                repository.saveCalculation(calculation)

                CoroutineScope(Dispatchers.Main).launch {
                    binding.saveStatusText.text = "✅ Расчёт сохранён!"
                    binding.saveStatusText.visibility = View.VISIBLE
                    binding.saveButton.isEnabled = false
                    Toast.makeText(requireContext(), "Расчёт сохранён!", Toast.LENGTH_SHORT).show()

                    binding.saveStatusText.postDelayed({
                        binding.saveStatusText.visibility = View.GONE
                    }, 3000)
                }
            } catch (e: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(requireContext(), "Ошибка сохранения: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}