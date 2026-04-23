package ci.nsu.mobile.main.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.mobile.main.R
import ci.nsu.mobile.main.data.database.DepositCalculation
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(
    private val onItemClick: (DepositCalculation) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var calculations: List<DepositCalculation> = emptyList()
    private val decimalFormat = DecimalFormat("#,##0.00")
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    fun submitList(list: List<DepositCalculation>) {
        calculations = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view, onItemClick, decimalFormat, dateFormat)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(calculations[position])
    }

    override fun getItemCount(): Int = calculations.size

    class HistoryViewHolder(
        itemView: View,
        private val onItemClick: (DepositCalculation) -> Unit,
        private val decimalFormat: DecimalFormat,
        private val dateFormat: SimpleDateFormat
    ) : RecyclerView.ViewHolder(itemView) {

        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvInitialAmount: TextView = itemView.findViewById(R.id.tvInitialAmount)
        private val tvFinalAmount: TextView = itemView.findViewById(R.id.tvFinalAmount)

        fun bind(calculation: DepositCalculation) {
            tvDate.text = dateFormat.format(Date(calculation.calculationDate))
            tvInitialAmount.text = "${decimalFormat.format(calculation.initialAmount)} ₽"
            tvFinalAmount.text = "${decimalFormat.format(calculation.finalAmount)} ₽"

            itemView.setOnClickListener {
                onItemClick(calculation)
            }
        }
    }
}