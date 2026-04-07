package ci.nsu.mobile.main.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.mobile.main.data.model.DepositCalculation
import ci.nsu.mobile.main.databinding.ItemHistoryBinding
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(private val onClick: (DepositCalculation) -> Unit) :
    ListAdapter<DepositCalculation, HistoryAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(calculation: DepositCalculation) {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

            binding.tvDate.text = dateFormat.format(Date(calculation.calculationDate))
            binding.tvInitial.text = "Стартовый взнос: ${calculation.initialAmount} ₽"
            binding.tvFinal.text = "Итог: ${"%.2f".format(calculation.finalAmount)} ₽"
            binding.tvPeriod.text = "Срок: ${calculation.periodMonths} мес"

            binding.root.setOnClickListener {
                onClick(calculation)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<DepositCalculation>() {
        override fun areItemsTheSame(oldItem: DepositCalculation, newItem: DepositCalculation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DepositCalculation, newItem: DepositCalculation): Boolean {
            return oldItem == newItem
        }
    }
}