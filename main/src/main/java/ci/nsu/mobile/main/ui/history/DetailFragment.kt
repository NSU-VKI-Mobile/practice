package ci.nsu.mobile.main.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.databinding.FragmentDetailBinding
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DepositViewModel by activityViewModels {
        DepositViewModelFactory((requireActivity().application as ci.nsu.mobile.main.App).repository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calculationId = arguments?.getLong("calculationId", -1L) ?: -1L

        if (calculationId == -1L) {
            findNavController().navigateUp()
            return
        }

        viewModel.allCalculations.observe(viewLifecycleOwner) { list ->
            val calculation = list.find { it.id == calculationId }
            if (calculation != null) {
                val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

                binding.tvDetailInitial.text = "Стартовый взнос: ${calculation.initialAmount} ₽"
                binding.tvDetailPeriod.text = "Срок: ${calculation.periodMonths} месяцев"
                binding.tvDetailRate.text = "Ставка: ${calculation.interestRate}%"
                binding.tvDetailMonthly.text = "Пополнение: ${calculation.monthlyTopUp ?: "—"} ₽"
                binding.tvDetailFinal.text = "Итоговая сумма: ${"%.2f".format(calculation.finalAmount)} ₽"
                binding.tvDetailInterest.text = "Начисленные проценты: ${"%.2f".format(calculation.interestEarned)} ₽"
                binding.tvDetailDate.text = "Дата расчёта: ${dateFormat.format(Date(calculation.calculationDate))}"
            }
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}