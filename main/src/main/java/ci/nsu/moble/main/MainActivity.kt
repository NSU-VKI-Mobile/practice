package ci.nsu.moble.main

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.marginBottom

private val colorsMap = mapOf(
    "Red" to Color.RED,
    "Magenta" to Color.MAGENTA,
    "Yellow" to Color.YELLOW,
    "Blue" to Color.BLUE,
    "Green" to Color.GREEN,
    "Cyan" to Color.rgb(0, 255, 255),
    "Orange" to Color.rgb(255, 105, 0),
    "Gray" to Color.GRAY
)
class MainActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val colorsList = findViewById<ListView>(R.id.ColorsList)
        val button = findViewById<Button>(R.id.ColorfulButton)
        val inputText = findViewById<EditText>(R.id.ColorText)

        val colorNames = colorsMap.keys.toList()

        val adapter = ColorAdapter(this, android.R.layout.simple_list_item_1, colorNames, colorsMap)
        colorsList.adapter = adapter

        button.setOnClickListener()
        {
            val enteredText = inputText.text.toString().trim()
            val colors = colorNames.find()
            {
                it.equals(enteredText, ignoreCase = true)
            }
            if (colors != null)
            {
                val color = colorsMap[colors] ?: Color.WHITE
                button.setBackgroundColor(color)
            }
            else
            {
                button.setBackgroundColor(Color.WHITE)
                inputText.error = "Color not found"
                Log.d("Warning", "Color not found")
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