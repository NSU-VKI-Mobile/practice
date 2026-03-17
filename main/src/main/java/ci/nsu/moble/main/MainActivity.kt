package ci.nsu.moble.main





import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var selectedColorText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectedColorText = findViewById(R.id.selectedColorText)
    }

    fun onColorClick(view: View) {
        val colorName = (view as TextView).text.toString()
        selectedColorText.text = colorName

        when (colorName) {
            "Red" -> selectedColorText.setTextColor(Color.RED)
            "Orange" -> selectedColorText.setTextColor(Color.rgb(255, 165, 0))
            "Yellow" -> selectedColorText.setTextColor(Color.YELLOW)
            "Green" -> selectedColorText.setTextColor(Color.GREEN)
            "Blue" -> selectedColorText.setTextColor(Color.BLUE)
            "Indigo" -> selectedColorText.setTextColor(Color.rgb(75, 0, 130))
            "Violet" -> selectedColorText.setTextColor(Color.rgb(238, 130, 238))
        }
    }
}