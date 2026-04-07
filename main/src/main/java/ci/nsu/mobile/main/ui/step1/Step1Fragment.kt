package ci.nsu.mobile.main.ui.step1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.databinding.FragmentStep1Binding
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModelFactory

class Step1Fragment : Fragment() {

    private var _binding: FragmentStep1Binding? = null
    private val binding get() = _binding!!

    private val viewModel: DepositViewModel by activityViewModels {
        DepositViewModelFactory((requireActivity().application as ci.nsu.mobile.main.App).repository)
    }

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

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_step1Fragment_to_mainFragment)
        }

        binding.btnNext.setOnClickListener {
            val initialStr = binding.etInitialAmount.text.toString().trim()
            val monthsStr = binding.etPeriodMonths.text.toString().trim()

            if (initialStr.isEmpty() || monthsStr.isEmpty()) {
                Toast.makeText(requireContext(), "Заполните все обязательные поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val initial = initialStr.toDoubleOrNull() ?: 0.0
            val months = monthsStr.toIntOrNull() ?: 0

            if (initial <= 0 || months <= 0) {
                Toast.makeText(requireContext(), "Введите корректные положительные значения", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.setStep1Data(initial, months)
            findNavController().navigate(R.id.action_step1Fragment_to_step2Fragment)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}