package ci.nsu.mobile.main.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.mobile.main.data.entity.DepositCalculation
import ci.nsu.mobile.main.databinding.ItemHistoryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter(
    private val onItemClick: (Long) -> Unit
) : ListAdapter<DepositCalculation, HistoryAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DepositCalculation) {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            binding.tvDate.text = dateFormat.format(Date(item.calculationDate))
            binding.tvItemInitialAmount.text = "Взнос: ${item.initialAmount} руб."
            binding.tvItemFinalAmount.text = "Итого: ${item.finalAmount} руб."
            binding.root.setOnClickListener { onItemClick(item.id) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<DepositCalculation>() {
        override fun areItemsTheSame(oldItem: DepositCalculation, newItem: DepositCalculation) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: DepositCalculation, newItem: DepositCalculation) =
            oldItem == newItem
    }
}
