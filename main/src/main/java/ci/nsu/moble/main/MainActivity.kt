package ci.nsu.moble.main

import android.R.attr.button
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ci.nsu.moble.main.ui.main.MainFragment
import android.widget.ListView
import android.widget.EditText
import android.widget.Button
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.FrameLayout
import android.widget.Toast
import android.widget.*
import androidx.core.content.ContextCompat
import kotlin.text.clear
import kotlin.toString
class MainActivity : AppCompatActivity() {
    private val colorsMap = mapOf(
        "Red" to Color.RED,
        "Orange" to Color.rgb(237, 128, 26),
        "Yellow" to Color.YELLOW,
        "Green" to Color.GREEN,
        "Blue" to Color.BLUE,
        "Indigo" to Color.rgb(104, 50, 168)
    )
    val colorNames = colorsMap.keys.toList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView = findViewById<ListView>(R.id.list_view)
        val editText = findViewById<EditText>(R.id.editTextText)
        val button = findViewById<Button>(R.id.button)

        val adapter = ColorAdapter(this, android.R.layout.simple_list_item_1, colorNames, colorsMap)
        listView.adapter = adapter

        button.setOnClickListener {
            val inputText = editText.text.toString().trim()
            val colors = colorNames.find()
            {
                it.lowercase().equals(inputText.lowercase())
            }
            if (colors != null)
            {
                val color = colorsMap[colors] ?: Color.WHITE
                button.setBackgroundColor(color)
            }
            else
            {
                button.setBackgroundColor(Color.WHITE)
                Toast.makeText(this, "Color '$inputText' not found", Toast.LENGTH_SHORT).show()
                Log.d ("Neznauy", "Nety '$inputText'")
            }
        }
    }
}
class ColorAdapter(context: Context, val layout: Int, val items: List<String>, private val colorsMap: Map<String, Int>) : ArrayAdapter<String>(context, layout, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)

        val colorName = items[position]
        val color = colorsMap[colorName] ?: Color.WHITE
        view.setBackgroundColor(color)

        return view
    }
}