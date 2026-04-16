package ci.nsu.mobile.main.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.data.repository.DepositRepository
import ci.nsu.mobile.main.databinding.ResultFragmentBinding
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat

class ResultFragment : Fragment() {

    private var _binding: ResultFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ResultViewModel
    private val df = DecimalFormat("#,##0.00")

    private val initialAmount: Double by lazy { arguments?.getDouble("initialAmount") ?: 0.0 }
    private val periodMonths: Int by lazy { arguments?.getInt("periodMonths") ?: 0 }
    private val interestRate: Double by lazy { arguments?.getDouble("interestRate") ?: 0.0 }
    private val monthlyTopUp: Double? by lazy {
        if (arguments?.containsKey("monthlyTopUp") == true) arguments?.getDouble("monthlyTopUp") else null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ResultFragmentBinding.inflate(inflater, container, false)
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
                return ResultViewModel(repository) as T
            }
        })[ResultViewModel::class.java]

        viewModel.computeAndDisplay(initialAmount, periodMonths, interestRate, monthlyTopUp)

        viewModel.calculationResult.observe(viewLifecycleOwner, Observer { result ->
            if (result == null) return@Observer
            binding.tvResultInitialAmount.text = "Стартовый взнос: ${df.format(result.initialAmount)} ₽"
            binding.tvResultPeriodMonths.text = "Срок: ${result.periodMonths} мес."
            binding.tvResultInterestRate.text = "Ставка: ${result.interestRate}%"
            val topUpText = result.monthlyTopUp?.let { df.format(it) + " ₽" } ?: "—"
            binding.tvResultMonthlyTopUp.text = "Ежемесячное пополнение: $topUpText"
            binding.tvResultFinalAmount.text = "Итоговая сумма: ${df.format(result.finalAmount)} ₽"
            binding.tvResultInterestEarned.text = "Начисленные проценты: ${df.format(result.interestEarned)} ₽"
        })

        binding.btnSave.setOnClickListener {
            viewModel.saveCurrentResult()
        }

        viewModel.saveSuccess.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                Snackbar.make(binding.root, "Расчёт сохранён", Snackbar.LENGTH_SHORT).show()
            }
        })

        binding.btnBackToMainFromResult.setOnClickListener {
            findNavController().popBackStack(R.id.mainFragment, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}