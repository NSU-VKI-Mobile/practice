package ci.nsu.mobile.main.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ci.nsu.mobile.main.databinding.FragmentDetailBinding
import ci.nsu.mobile.main.data.database.DepositEntity
import java.text.DecimalFormat

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val df = DecimalFormat("#.##")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val entity = arguments?.getSerializable("entity") as? DepositEntity
        if (entity != null) {
            binding.tvInitialAmount.text = "Стартовый взнос: ${df.format(entity.initialAmount)} ₽"
            binding.tvPeriodMonths.text = "Срок: ${entity.periodMonths} мес."
            binding.tvInterestRate.text = "Процентная ставка: ${entity.interestRate}%"
            binding.tvMonthlyTopUp.text = if (entity.monthlyTopUp != null && entity.monthlyTopUp > 0)
                "Ежемесячное пополнение: ${df.format(entity.monthlyTopUp)} ₽"
            else
                "Ежемесячное пополнение: не указано"
            binding.tvFinalAmount.text = "Итоговая сумма: ${df.format(entity.finalAmount)} ₽"
            binding.tvInterestEarned.text = "Начисленные проценты: ${df.format(entity.interestEarned)} ₽"
        }

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}