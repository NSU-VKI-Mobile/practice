package mobile.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.mobile.main.R
import mobile.data.DepositCalculation
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var items: List<DepositCalculation> = emptyList()

    // Функция, чтобы передать новый список в адаптер
    fun submitList(newItems: List<DepositCalculation>) {
        items = newItems
        notifyDataSetChanged() // Обновляем экран
    }

    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvDetails: TextView = view.findViewById(R.id.tvDetails)
        val tvResult: TextView = view.findViewById(R.id.tvResult)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = items[position]

        // Превращаем миллисекунды в красивую дату
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        holder.tvDate.text = "Дата: ${sdf.format(Date(item.calculationDate))}"

        holder.tvDetails.text = "Взнос: ${item.initialAmount} ₽ | Срок: ${item.periodMonths} мес | Ставка: ${item.interestRate}%"
        holder.tvResult.text = "Итог: ${String.format(Locale.getDefault(), "%.2f", item.finalAmount)} ₽"
    }

    override fun getItemCount(): Int = items.size
}