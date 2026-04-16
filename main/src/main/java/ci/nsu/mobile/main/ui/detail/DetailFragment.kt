package ci.nsu.mobile.main.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.databinding.DetailFragmentBinding
import java.text.DecimalFormat

class DetailFragment : Fragment() {

    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: DetailViewModel
    private val df = DecimalFormat("#,##0.00")

    private val calculationId: Long by lazy { arguments?.getLong("calculationId") ?: 0L }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = DepositRepository(
            ci.nsu.mobile.main.data.database.AppDatabase.getDatabase(requireContext()).depositDao()
        )
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(repository) as T
            }
        })[DetailViewModel::class.java]

        viewModel.loadCalculation(calculationId)

        viewModel.calculation.observe(viewLifecycleOwner) { calc ->
            if (calc == null) return@observe
            binding.tvDetailInitialAmount.text = "Стартовый взнос: ${df.format(calc.initialAmount)} ₽"
            binding.tvDetailPeriodMonths.text = "Срок: ${calc.periodMonths} мес."
            binding.tvDetailInterestRate.text = "Ставка: ${calc.interestRate}%"
            val topUpText = calc.monthlyTopUp?.let { df.format(it) + " ₽" } ?: "—"
            binding.tvDetailMonthlyTopUp.text = "Ежемесячное пополнение: $topUpText"
            binding.tvDetailFinalAmount.text = "Итоговая сумма: ${df.format(calc.finalAmount)} ₽"
            binding.tvDetailInterestEarned.text = "Начисленные проценты: ${df.format(calc.interestEarned)} ₽"
        }

        binding.btnBackToHistory.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}