package ci.nsu.mobile.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.mobile.main.data.DepositCalculation
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DepositHistoryAdapter(
    private var items: List<DepositCalculation>,
    private val onItemClick: (DepositCalculation) -> Unit
) : RecyclerView.Adapter<DepositHistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val calculation = items[position]
        val date = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            .format(Date(calculation.calculationDate))

        holder.textView.text = "Дата: $date\nВзнос: ${formatMoney(calculation.initialAmount)}\nИтог: ${formatMoney(calculation.finalAmount)}"
        holder.itemView.setOnClickListener {
            onItemClick(calculation)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newItems: List<DepositCalculation>) {
        items = newItems
        notifyDataSetChanged()
    }

    private fun formatMoney(value: Double): String {
        return String.format(Locale.US, "%.2f", value)
    }
}
