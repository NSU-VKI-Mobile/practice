package ci.nsu.mobile.main.ui.secondstep

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.databinding.FragmentSecondStepBinding
import ci.nsu.mobile.main.utils.Validator

class SecondStepFragment : Fragment() {

    private var _binding: FragmentSecondStepBinding? = null
    private val binding get() = _binding!!

    // Переменные для хранения данных с первого экрана
    private var initialAmount: Double = 0.0
    private var periodMonths: Int = 0

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

        // ПОЛУЧАЕМ ДАННЫЕ С ПЕРВОГО ЭКРАНА
        arguments?.let {
            initialAmount = it.getDouble("initialAmount", 0.0)
            periodMonths = it.getInt("periodMonths", 0)
        }

        android.util.Log.d("SecondStepFragment", "Получено: initialAmount=$initialAmount, periodMonths=$periodMonths")

        // Проверка получения данных
        if (initialAmount <= 0 || periodMonths <= 0) {
            Toast.makeText(requireContext(), "Ошибка: не переданы данные. Вернитесь на первый экран.", Toast.LENGTH_LONG).show()
            return
        }

        // Расчёт процентной ставки
        val availableRate = Validator.calculateInterestRate(periodMonths)

        // Настройка выпадающего списка
        val rates = listOf("$availableRate%")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, rates)
        binding.interestRateSpinner.setAdapter(adapter)
        binding.interestRateSpinner.setText("$availableRate%", false)
        binding.interestRateSpinner.isEnabled = false

        binding.backButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_secondStepFragment_to_firstStepFragment)
        }

        binding.calculateButton.setOnClickListener {
            val monthlyTopUp = binding.monthlyTopUpInput.text.toString()
            val topUpResult = Validator.validateMonthlyTopUp(monthlyTopUp)

            if (topUpResult is Validator.Result.Error) {
                showError(topUpResult.message)
            } else {
                val monthlyTopUpValue = (topUpResult as Validator.Result.Success).data
                val interestRate = Validator.calculateInterestRate(periodMonths)

                // Выполняем расчёт
                val result = Validator.calculateDeposit(
                    initialAmount,
                    periodMonths,
                    interestRate,
                    monthlyTopUpValue
                )

                android.util.Log.d("SecondStepFragment", "Результат: finalAmount=${result.finalAmount}, interestEarned=${result.interestEarned}")

                // ПЕРЕДАЁМ ДАННЫЕ НА ЭКРАН РЕЗУЛЬТАТА
                val bundle = Bundle().apply {
                    putDouble("initialAmount", initialAmount)
                    putInt("periodMonths", periodMonths)
                    putDouble("interestRate", interestRate)
                    if (monthlyTopUpValue != null) {
                        putDouble("monthlyTopUp", monthlyTopUpValue)
                    }
                    putDouble("finalAmount", result.finalAmount)
                    putDouble("interestEarned", result.interestEarned)
                }

                view.findNavController().navigate(R.id.action_secondStepFragment_to_resultFragment, bundle)
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