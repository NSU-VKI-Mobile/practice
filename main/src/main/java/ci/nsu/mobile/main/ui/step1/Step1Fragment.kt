package ci.nsu.mobile.main.ui.step1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ci.nsu.mobile.main.DepositData
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.databinding.FragmentMainBinding
import ci.nsu.mobile.main.databinding.FragmentStep1Binding
import java.text.NumberFormat
import java.time.Month
import java.time.temporal.TemporalAmount

class Step1Fragment : Fragment() {
    private var _binding: FragmentStep1Binding? = null
    private val binding get() = _binding!!
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
        binding.btnBackToMain.setOnClickListener {
            findNavController().navigateUp()

        }
        binding.btnNext.setOnClickListener {
            val initialAmount = binding.etInitialAmount.text.toString()
            val periodMonths = binding.etPeriodMonths.text.toString()
            if (validateInput(initialAmount, periodMonths)) {
                val depositData = DepositData(
                    initialAmount = initialAmount.toDouble(),
                    periodMonths = periodMonths.toInt()
                )
                val action = Step1FragmentDirections.actionStep1FragmentToStep2Fragment(depositData)
                findNavController().navigate(action)
            }
        }
    }

    private fun validateInput(initialAmount: String, periodMonth: String): Boolean {
        if (initialAmount.isEmpty()) {
            Toast.makeText(requireContext(), "Введите стартовый взонс", Toast.LENGTH_SHORT).show()
            return false
        }
        try {
            val amount = initialAmount.toDouble()
            if (amount <= 0) {
                Toast.makeText(
                    requireContext(),
                    "Стартовый взонс должен быть больше 0",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }
        } catch (e: NumberFormatException) {
            Toast.makeText(requireContext(), "Введите корректную сумму", Toast.LENGTH_SHORT).show()
            return false
        }
        if (periodMonth.isEmpty()) {
            Toast.makeText(requireContext(), "Введите срок вклада", Toast.LENGTH_SHORT).show()
            return false
        }
        try {
            val months = periodMonth.toInt()
            if (months <= 0) {
                Toast.makeText(requireContext(), "Срок должен быть больше 0", Toast.LENGTH_SHORT)
                    .show()
                return false
            }
        } catch (e: NumberFormatException) {
            Toast.makeText(requireContext(), "Введите корректный срок", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}