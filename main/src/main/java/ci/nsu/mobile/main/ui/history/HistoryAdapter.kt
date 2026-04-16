package ci.nsu.mobile.main.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.mobile.main.data.database.DepositCalculation
import ci.nsu.mobile.main.databinding.ItemHistoryBinding
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(
    private val onClick: (DepositCalculation) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var calculations = listOf<DepositCalculation>()
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

    fun submitList(list: List<DepositCalculation>) {
        calculations = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = calculations[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = calculations.size

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(calc: DepositCalculation) {
            binding.tvHistoryDate.text = dateFormat.format(Date(calc.calculationDate))
            binding.tvHistoryAmount.text = "Стартовый взнос: ${calc.initialAmount} ₽"
            binding.tvHistoryFinal.text = "Итоговая сумма: ${calc.finalAmount} ₽"
        }
    }
}