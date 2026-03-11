package ci.nsu.mobile.main.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ci.nsu.mobile.main.R

class DetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvDetailsText = view.findViewById<TextView>(R.id.tvDetailsText)

        // Получаем переданные аргументы
        val data = arguments?.getString("data_key") ?: "Нет данных"

        // Если данные пришли из MainActivity или SecondActivity, показываем это
        if (data.contains("Привет") || data.contains("Данные")) {
            tvDetailsText.text = "📦 Полученные данные:\n\n\"$data\""
        } else {
            tvDetailsText.text = "Детали: $data"
        }
    }
}