package ci.nsu.mobile.main.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import ci.nsu.mobile.mai.database.DepositCalculation
import ci.nsu.mobile.main.R

import ci.nsu.mobile.main.databinding.FragmentResultBinding
import ci.nsu.mobile.main.repository.DepositRepository
import ci.nsu.mobile.main.ui.secondstep.SecondStepViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private lateinit var secondStepViewModel: SecondStepViewModel
    private lateinit var repository: DepositRepository

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

        secondStepViewModel = ViewModelProvider(requireActivity())[SecondStepViewModel::class.java]
        repository = DepositRepository.getInstance(requireContext())

        displayResult()

        binding.saveButton.setOnClickListener {
            saveCalculation()
        }

        binding.backToStartButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_resultFragment_to_mainFragment)
        }
    }

    private fun displayResult() {
        val initialAmount = secondStepViewModel.getInitialAmount()
        val periodMonths = secondStepViewModel.getPeriodMonths()
        val interestRate = secondStepViewModel.getInterestRate()
        val monthlyTopUp = secondStepViewModel.getMonthlyTopUp()
        val finalAmount = secondStepViewModel.getFinalAmount()
        val interestEarned = secondStepViewModel.getInterestEarned()

        android.util.Log.d("ResultFragment", "initialAmount: $initialAmount")
        android.util.Log.d("ResultFragment", "periodMonths: $periodMonths")
        android.util.Log.d("ResultFragment", "interestRate: $interestRate")
        android.util.Log.d("ResultFragment", "finalAmount: $finalAmount")

        if (initialAmount <= 0) {
            binding.initialAmountText.text = "Ошибка: нет данных для расчёта"
            Toast.makeText(requireContext(), "Сначала выполните расчёт на предыдущем экране", Toast.LENGTH_LONG).show()
            return
        }

        binding.initialAmountText.text = "Стартовый взнос: ${String.format("%.2f", initialAmount)} ₽"
        binding.periodText.text = "Срок: $periodMonths месяцев"
        binding.interestRateText.text = "Процентная ставка: ${String.format("%.1f", interestRate)}%"

        val monthlyTopUpText = if (monthlyTopUp != null && monthlyTopUp > 0) {
            "${String.format("%.2f", monthlyTopUp)} ₽"
        } else {
            "Не указано"
        }
        binding.monthlyTopUpText.text = "Ежемесячное пополнение: $monthlyTopUpText"

        binding.finalAmountText.text = "Итоговая сумма: ${String.format("%.2f", finalAmount)} ₽"
        binding.interestEarnedText.text = "Начисленные проценты: ${String.format("%.2f", interestEarned)} ₽"
    }

    private fun saveCalculation() {
        val initialAmount = secondStepViewModel.getInitialAmount()
        val periodMonths = secondStepViewModel.getPeriodMonths()
        val interestRate = secondStepViewModel.getInterestRate()
        val monthlyTopUp = secondStepViewModel.getMonthlyTopUp()
        val finalAmount = secondStepViewModel.getFinalAmount()
        val interestEarned = secondStepViewModel.getInterestEarned()

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