package ci.nsu.mobile.main.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ci.nsu.mobile.main.data.entity.DepositCalculation
import ci.nsu.mobile.main.databinding.FragmentDetailsBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.calculation.observe(viewLifecycleOwner) { calculation ->
            calculation?.let { displayDetails(it) }
        }

        binding.btnDetailsBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun displayDetails(item: DepositCalculation) {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        binding.tvDetailDate.text = "Дата: ${dateFormat.format(Date(item.calculationDate))}"
        binding.tvDetailInitialAmount.text = "Стартовый взнос: ${item.initialAmount} руб."
        binding.tvDetailPeriodMonths.text = "Срок: ${item.periodMonths} мес."
        binding.tvDetailInterestRate.text = "Ставка: ${item.interestRate}%"
        val topUp = if (item.monthlyTopUp == 0.0) "Не указано" else "${item.monthlyTopUp} руб./мес."
        binding.tvDetailMonthlyTopUp.text = "Пополнение: $topUp"
        binding.tvDetailFinalAmount.text = "Итоговая сумма: ${item.finalAmount} руб."
        binding.tvDetailInterestEarned.text = "Начислено процентов: ${item.interestEarned} руб."
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
