package ci.nsu.mobile.main.ui.step2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.databinding.FragmentStep2Binding
import ci.nsu.mobile.main.data.model.DepositInputData
import ci.nsu.mobile.main.data.model.DepositResultData

class Step2Fragment : Fragment() {

    private var _binding: FragmentStep2Binding? = null
    private val binding get() = _binding!!
    private val viewModel: Step2ViewModel by viewModels()
    private val args: Step2FragmentArgs by navArgs()

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

        val depositData: DepositInputData = args.depositData
        val periodMonths = depositData.periodMonths

        viewModel.initRates(periodMonths)

        // Настройка спиннера
        viewModel.availableRates.observe(viewLifecycleOwner) { rates ->
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, rates.map { it.first })
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerRate.adapter = adapter

            binding.spinnerRate.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                    viewModel.setSelectedRate(rates[position].second)
                }
                override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
            }
        }

        // Ежемесячное пополнение
        binding.editMonthlyTopUp.addTextChangedListener { text ->
            viewModel.setMonthlyTopUp(text.toString())
        }

        // Кнопка "Назад"
        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // Кнопка "Рассчитать" с обработкой ошибок
        binding.buttonCalculate.setOnClickListener {
            try {
                val monthlyTopUp = viewModel.monthlyTopUp.value ?: 0.0
                val interestRate = viewModel.selectedRate.value ?: 0.0

                val resultData = DepositResultData(
                    initialAmount = depositData.initialAmount,
                    periodMonths = depositData.periodMonths,
                    interestRate = interestRate,
                    monthlyTopUp = if (monthlyTopUp > 0) monthlyTopUp else null
                )
                val action = Step2FragmentDirections.actionStep2ToResult(resultData)
                findNavController().navigate(action)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Ошибка: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}