package ci.nsu.mobile.main.ui.step2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ci.nsu.mobile.main.DepositData
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.databinding.FragmentStep2Binding

class Step2Fragment : Fragment() {

    private var _binding: FragmentStep2Binding? = null
    private val binding get() = _binding!!

    // Объявляем переменную как var, чтобы можно было изменять
    private var currentDepositData: DepositData? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStep2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получаем данные из аргументов
        currentDepositData = arguments?.getSerializable("depositData") as? DepositData

        currentDepositData?.let { depositData ->
            setupSpinner(depositData.periodMonths)
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnCalculate.setOnClickListener {
            val depositData = currentDepositData
            if (depositData == null) {
                Toast.makeText(requireContext(), "Ошибка: данные не найдены", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (depositData.periodMonths <= 0) {
                Toast.makeText(requireContext(), "Некорректный срок вклада. Вернитесь и укажите срок", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val selectedRate = binding.spinnerInterestRate.selectedItem.toString().replace("%", "").toDouble()
            val monthlyTopUp = if (binding.etMonthlyTopUp.text.toString().isEmpty()) {
                null
            } else {
                try {
                    binding.etMonthlyTopUp.text.toString().toDouble()
                } catch (e: NumberFormatException) {
                    Toast.makeText(requireContext(), "Некорректная сумма пополнения", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            // Создаем новый объект с обновленными данными
            val updatedData = DepositData(
                initialAmount = depositData.initialAmount,
                periodMonths = depositData.periodMonths,
                interestRate = selectedRate,
                monthlyTopUp = monthlyTopUp,
                finalAmount = depositData.finalAmount,
                interestEarned = depositData.interestEarned
            )

            calculateResult(updatedData)

            // Используем Bundle вместо Directions
            val bundle = Bundle()
            bundle.putSerializable("depositData", updatedData)
            findNavController().navigate(R.id.action_step2Fragment_to_resultFragment, bundle)
        }
    }

    private fun setupSpinner(periodMonths: Int) {
        val rates = when {
            periodMonths < 6 -> listOf("15%")
            periodMonths < 12 -> listOf("10%")
            periodMonths >= 12 -> listOf("5%")
            else -> listOf("5%")
        }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, rates)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerInterestRate.adapter = adapter
    }

    private fun calculateResult(data: DepositData) {
        val monthlyRate = data.interestRate / 100 / 12
        var currentAmount = data.initialAmount
        var totalInterest = 0.0

        for (month in 1..data.periodMonths) {
            val interest = currentAmount * monthlyRate
            totalInterest += interest
            currentAmount += interest

            data.monthlyTopUp?.let {
                currentAmount += it
            }
        }

        data.finalAmount = currentAmount
        data.interestEarned = totalInterest
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}