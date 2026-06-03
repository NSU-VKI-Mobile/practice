package ci.nsu.mobile.main.ui.history

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import ci.nsu.mobile.main.R
import ci.nsu.mobile.mai.database.DepositCalculation
import java.io.Serializable

class DetailDialogFragment : DialogFragment() {

    private lateinit var calculation: DepositCalculation

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Исправленный способ получения аргументов
        calculation = arguments?.getSerializable(ARG_CALCULATION) as? DepositCalculation
            ?: throw IllegalArgumentException("Missing calculation argument")

        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_detail, null)

        view.findViewById<TextView>(R.id.detailInitialAmount).text =
            "Стартовый взнос: ${String.format("%.2f", calculation.initialAmount)} ₽"
        view.findViewById<TextView>(R.id.detailPeriod).text =
            "Срок: ${calculation.periodMonths} месяцев"
        view.findViewById<TextView>(R.id.detailInterestRate).text =
            "Процентная ставка: ${String.format("%.1f", calculation.interestRate)}%"

        val monthlyTopUpText = if (calculation.monthlyTopUp != null) {
            "${String.format("%.2f", calculation.monthlyTopUp)} ₽"
        } else {
            "Не указано"
        }
        view.findViewById<TextView>(R.id.detailMonthlyTopUp).text =
            "Ежемесячное пополнение: $monthlyTopUpText"

        view.findViewById<TextView>(R.id.detailFinalAmount).text =
            "Итоговая сумма: ${String.format("%.2f", calculation.finalAmount)} ₽"
        view.findViewById<TextView>(R.id.detailInterestEarned).text =
            "Начисленные проценты: ${String.format("%.2f", calculation.interestEarned)} ₽"

        view.findViewById<MaterialButton>(R.id.closeButton).setOnClickListener {
            dismiss()
        }

        return MaterialAlertDialogBuilder(requireContext())
            .setView(view)
            .create()
    }

    companion object {
        private const val ARG_CALCULATION = "calculation"

        fun newInstance(calculation: DepositCalculation): DetailDialogFragment {
            val fragment = DetailDialogFragment()
            val args = Bundle()
            args.putSerializable(ARG_CALCULATION, calculation as Serializable)
            fragment.arguments = args
            return fragment
        }
    }
}