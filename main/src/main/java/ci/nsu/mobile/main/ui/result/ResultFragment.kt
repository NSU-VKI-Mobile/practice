package ci.nsu.mobile.main.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import ci.nsu.mobile.main.R
import ci.nsu.mobile.mai.database.DepositCalculation
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
        val depositData = secondStepViewModel.getDepositData()

        depositData?.let { data ->
            binding.initialAmountText.text = "Стартовый взнос: ${String.format("%.2f", data.initialAmount)} ₽"
            binding.periodText.text = "Срок: ${data.periodMonths} месяцев"
            binding.interestRateText.text = "Процентная ставка: ${String.format("%.1f", data.interestRate)}%"

            val monthlyTopUpText = if (data.monthlyTopUp != null) {
                "${String.format("%.2f", data.monthlyTopUp)} ₽"
            } else {
                "Не указано"
            }
            binding.monthlyTopUpText.text = "Ежемесячное пополнение: $monthlyTopUpText"

            binding.finalAmountText.text = "Итоговая сумма: ${String.format("%.2f", secondStepViewModel.getFinalAmount())} ₽"
            binding.interestEarnedText.text = "Начисленные проценты: ${String.format("%.2f", secondStepViewModel.getInterestEarned())} ₽"
        }
    }

    private fun saveCalculation() {
        val depositData = secondStepViewModel.getDepositData()

        depositData?.let { data ->
            val calculation = DepositCalculation(
                initialAmount = data.initialAmount,
                periodMonths = data.periodMonths,
                interestRate = data.interestRate,
                monthlyTopUp = data.monthlyTopUp,
                finalAmount = secondStepViewModel.getFinalAmount(),
                interestEarned = secondStepViewModel.getInterestEarned(),
                calculationDate = System.currentTimeMillis()
            )

            CoroutineScope(Dispatchers.IO).launch {
                repository.saveCalculation(calculation)

                CoroutineScope(Dispatchers.Main).launch {
                    binding.saveStatusText.text = "✅ Расчёт сохранён!"
                    binding.saveStatusText.visibility = View.VISIBLE
                    binding.saveButton.isEnabled = false

                    binding.saveStatusText.postDelayed({
                        binding.saveStatusText.visibility = View.GONE
                    }, 3000)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}