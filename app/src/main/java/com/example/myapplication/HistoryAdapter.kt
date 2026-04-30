package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemDepositBinding
import com.example.myapplication.entities.DepositEntity
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(private val onItemClick: (DepositEntity) -> Unit) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var items = emptyList<DepositEntity>()

    fun setData(newItems: List<DepositEntity>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemDepositBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = items[position]
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())

        holder.binding.tvHistoryDate.text = sdf.format(Date(item.date))
        holder.binding.tvHistoryInfo.text = "Взнос: ${item.amount} | Итог: ${"%.2f".format(item.total)}"

        holder.itemView.setOnClickListener { onItemClick(item) }
    }

    override fun getItemCount() = items.size

    class HistoryViewHolder(val binding: ItemDepositBinding) : RecyclerView.ViewHolder(binding.root)
}