package ci.nsu.mobile.main.ui.history

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.data.database.DepositCalculation
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailDialogFragment : DialogFragment() {

    private val decimalFormat = DecimalFormat("#,##0.00")
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Получаем данные из аргументов
        val calculation = arguments?.getSerializable("calculation") as? DepositCalculation

        if (calculation == null) {
            return MaterialAlertDialogBuilder(requireContext())
                .setTitle("Ошибка")
                .setMessage("Данные не найдены")
                .setPositiveButton("Закрыть", null)
                .create()
        }

        val view = layoutInflater.inflate(R.layout.dialog_detail, null)

        val tvDateValue = view.findViewById<android.widget.TextView>(R.id.tvDateValue)
        val tvInitialAmountValue = view.findViewById<android.widget.TextView>(R.id.tvInitialAmountValue)
        val tvPeriodValue = view.findViewById<android.widget.TextView>(R.id.tvPeriodValue)
        val tvInterestRateValue = view.findViewById<android.widget.TextView>(R.id.tvInterestRateValue)
        val tvMonthlyTopUpValue = view.findViewById<android.widget.TextView>(R.id.tvMonthlyTopUpValue)
        val tvFinalAmountValue = view.findViewById<android.widget.TextView>(R.id.tvFinalAmountValue)
        val tvInterestEarnedValue = view.findViewById<android.widget.TextView>(R.id.tvInterestEarnedValue)

        tvDateValue.text = dateFormat.format(Date(calculation.calculationDate))
        tvInitialAmountValue.text = "${decimalFormat.format(calculation.initialAmount)} ₽"
        tvPeriodValue.text = "${calculation.periodMonths} месяцев"
        tvInterestRateValue.text = "${calculation.interestRate}%"
        tvMonthlyTopUpValue.text = calculation.monthlyTopUp?.let {
            "${decimalFormat.format(it)} ₽"
        } ?: "Не указано"
        tvFinalAmountValue.text = "${decimalFormat.format(calculation.finalAmount)} ₽"
        tvInterestEarnedValue.text = "${decimalFormat.format(calculation.interestEarned)} ₽"

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Детали расчёта")
            .setView(view)
            .setPositiveButton("Закрыть", null)
            .create()
    }
}