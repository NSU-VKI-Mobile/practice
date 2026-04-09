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
import java.time.Month

class Step2Fragment : Fragment(){
    private var _binding: FragmentStep2Binding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStep2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val depositData = arguments?.getSerializable("depositData") as DepositData
        setupSpinner(depositData.periodMonths)
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnCalcilate.setOnClickListener {
            if (depositData.periodMonths <= -0) {
                Toast.makeText(requireContext(), "Некорректный срок вклада. Вернитесь и укажите срок",
                    Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val selctedRate = binding.spinnerInterestRate.selectedItem.toString().replace("%", "").toDouble()
            val monthlyTopUp = if (binding.etMonthlyTopUp.text.toString().isEmpty()){
                null
            } else {
                try {
                    binding.etMonthlyTopUp.text.toString().toDouble()
                } catch (e: NumberFormatException){
                    Toast.makeText(requireContext(), "Некорректная сумма пополнения", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            depositData.interestRate = selctedRate
            depositData.monthlyTopUp = monthlyTopUp
            calculateResult(depositData)
            val action = Step2FragmentDirections.actionStep2FragmentToResultFragment(depositData)
            findNavController().navigate(action)
        }
    }
    private fun setupSpinner(periodMonth: Int) {
        val rates = when {
            periodMonth < 6 -> listOf("15%")
            periodMonth < 12 -> listOf("10%")
            periodMonth >= 12 -> listOf("5%")
            else -> listOf("5%")
        }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, rates)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerInterestRate.adapter = adapter
    }
    private fun calculateResult(data: DepositData) {
        val monthlyRate = data.interestRate / 100 / 12
        val currentAmount = data.initialAmount
        var totalInsert = 0.0
        for (month in 1..data.periodMonths) {
            val interest = currentAmount * monthlyRate
            totalInsert += interest
            currentAmount += interest

            data.monthlyTopUp?.let {
                currentAmount += it
            }
        }
        data.finalAmount = currentAmount
        data.interestEarned = totalInsert
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}