package ci.nsu.mobile.main.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.databinding.FragmentResultBinding
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModelFactory

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DepositViewModel by activityViewModels {
        DepositViewModelFactory((requireActivity().application as ci.nsu.mobile.main.App).repository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentCalculation.observe(viewLifecycleOwner) { calc ->
            if (calc != null) {
                binding.tvInitial.text = "Стартовый взнос: ${calc.initialAmount} ₽"
                binding.tvPeriod.text = "Срок: ${calc.periodMonths} месяцев"
                binding.tvRate.text = "Ставка: ${calc.interestRate}%"
                binding.tvMonthlyTopUp.text = "Ежемесячное пополнение: ${calc.monthlyTopUp ?: "—"} ₽"
                binding.tvFinal.text = "Итоговая сумма: ${"%.2f".format(calc.finalAmount)} ₽"
                binding.tvInterest.text = "Начисленные проценты: ${"%.2f".format(calc.interestEarned)} ₽"
            }
        }

        binding.btnSave.setOnClickListener {
            viewModel.saveCurrent()
            Toast.makeText(requireContext(), "Расчёт успешно сохранён в историю", Toast.LENGTH_SHORT).show()
        }

        binding.btnBackToMain.setOnClickListener {
            findNavController().navigate(R.id.action_resultFragment_to_mainFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

