package ci.nsu.mobile.main.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.mobile.main.databinding.ItemHistoryBinding
import ci.nsu.mobile.main.model.DepositCalculation
import java.util.Locale

class HistoryAdapter(
    private val onItemClick: (DepositCalculation) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var calculations: List<DepositCalculation> = emptyList()

    fun submitList(list: List<DepositCalculation>) {
        calculations = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HistoryViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(calculations[position])
    }

    override fun getItemCount(): Int = calculations.size

    class HistoryViewHolder(
        private val binding: ItemHistoryBinding,
        private val onItemClick: (DepositCalculation) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(calculation: DepositCalculation) {
            binding.apply {
                tvDate.text = calculation.getFormattedDate()
                tvInitialAmount.text = formatCurrency(calculation.initialAmount)
                tvFinalAmount.text = formatCurrency(calculation.finalAmount)
                tvPeriod.text = "${calculation.periodMonths} мес."
                tvInterestRate.text = String.format("%.1f%%", calculation.interestRate)

                if (calculation.interestEarned > 0) {
                    tvProfitIndicator.visibility = android.view.View.VISIBLE
                    tvProfitIndicator.text = "📈"
                } else if (calculation.interestEarned < 0) {
                    tvProfitIndicator.visibility = android.view.View.VISIBLE
                    tvProfitIndicator.text = "📉"
                } else {
                    tvProfitIndicator.visibility = android.view.View.GONE
                }

                root.setOnClickListener {
                    onItemClick(calculation)
                }
            }
        }

        private fun formatCurrency(amount: Double): String {
            return String.format(Locale.US, "%,.2f ₽", amount)
        }
    }
}