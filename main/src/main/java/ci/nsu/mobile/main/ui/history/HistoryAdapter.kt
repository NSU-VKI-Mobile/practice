package ci.nsu.mobile.main.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.mobile.main.databinding.ItemHistoryBinding
import ci.nsu.mobile.main.data.database.DepositEntity
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryAdapter(
    private val onItemClick: (DepositEntity) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var items = listOf<DepositEntity>()

    fun submitList(list: List<DepositEntity>) {
        items = list
        notifyDataSetChanged()
    }

    fun getItemAt(position: Int): DepositEntity = items[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount() = items.size

    class HistoryViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(entity: DepositEntity) {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            binding.tvDate.text = dateFormat.format(java.util.Date(entity.calculationDate))
            binding.tvInitialAmount.text = "Стартовый взнос: ${entity.initialAmount} ₽"
            binding.tvFinalAmount.text = "Итоговая сумма: ${entity.finalAmount} ₽"
        }
    }
}