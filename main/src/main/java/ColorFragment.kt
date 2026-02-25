import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ci.nsu.moble.main.R
// Модель данных
data class ColorModel(val name: String, val hex: String)

class ColorFragment : Fragment(R.layout.fragment_color) {

    private val colorPalette = listOf(
        ColorModel("Red", "#FF0000"),
        ColorModel("Orange", "#FFA500"),
        ColorModel("Yellow", "#FFFF00"),
        ColorModel("Green", "#00FF00"),
        ColorModel("Blue", "#0000FF"),
        ColorModel("Indigo", "#4B0082"),
        ColorModel("Violet", "#EE82EE")
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val input = view.findViewById<EditText>(R.id.colorInput)
        val button = view.findViewById<Button>(R.id.applyButton)
        val recycler = view.findViewById<RecyclerView>(R.id.colorRecyclerView)

        // Настройка списка
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = ColorAdapter(colorPalette)

        // Логика кнопки
        button.setOnClickListener {
            val query = input.text.toString().trim()
            val foundColor = colorPalette.find { it.name.equals(query, ignoreCase = true) }

            if (foundColor != null) {
                // Если нашли — меняем цвет фона кнопки
                button.backgroundTintList = android.content.res.ColorStateList.valueOf(
                    Color.parseColor(foundColor.hex)
                )
            } else {
                // Если не нашли — пишем в Logcat
                Log.e("ColorSearch", "Пользовательский цвет \"$query\" не найден")
            }
        }
    }
}