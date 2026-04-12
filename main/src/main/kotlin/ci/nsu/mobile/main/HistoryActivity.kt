package ci.nsu.mobile.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ci.nsu.mobile.main.ui.viewmodel.DepositViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryActivity : AppCompatActivity() {
    private lateinit var viewModel: DepositViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        viewModel = ViewModelProvider(this)[DepositViewModel::class.java]

        val listView = findViewById<ListView>(R.id.listViewHistory)
        val format = NumberFormat.getNumberInstance(Locale("ru", "RU"))
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("ru", "RU"))

        val adapter = object : BaseAdapter() {
            private var deposits = listOf<ci.nsu.mobile.main.data.local.DepositCalculation>()

            init {
                viewModel.allDeposits.observe(this@HistoryActivity) { list ->
                    deposits = list
                    notifyDataSetChanged()
                }
            }

            override fun getCount() = deposits.size

            override fun getItem(position: Int) = deposits[position]

            override fun getItemId(position: Int) = deposits[position].id

            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val view = convertView ?: layoutInflater.inflate(android.R.layout.simple_list_item_2, parent, false)
                val deposit = deposits[position]

                view.findViewById<TextView>(android.R.id.text1).text =
                    "Взнос: ${format.format(deposit.initialAmount)} ₽, Итого: ${format.format(deposit.finalAmount)} ₽"
                view.findViewById<TextView>(android.R.id.text2).text =
                    dateFormat.format(Date(deposit.calculationDate))

                return view
            }
        }

        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val deposit = adapter.getItem(position)
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("deposit_id", deposit.id)
            startActivity(intent)
        }
    }
}