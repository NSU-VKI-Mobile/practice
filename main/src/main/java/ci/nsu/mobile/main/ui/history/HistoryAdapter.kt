package ci.nsu.mobile.main.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.mobile.mai.database.DepositCalculation

import ci.nsu.mobile.main.databinding.ItemHistoryBinding
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryAdapter(
    private val onItemClick: (DepositCalculation) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var calculations: List<DepositCalculation> = emptyList()
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    fun submitList(list: List<DepositCalculation>?) {
        calculations = list ?: emptyList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val calculation = calculations[position]
        holder.bind(calculation)
        holder.itemView.setOnClickListener { onItemClick(calculation) }
    }

    override fun getItemCount(): Int = calculations.size

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(calculation: DepositCalculation) {
            binding.dateText.text = dateFormat.format(calculation.calculationDate)
            binding.initialAmountText.text = "Стартовый взнос: ${String.format("%.2f", calculation.initialAmount)} ₽"
            binding.finalAmountText.text = "Итоговая сумма: ${String.format("%.2f", calculation.finalAmount)} ₽"
        }
    }
}