package ci.nsu.mobile.main

import androidx.core.content.ContextCompat
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.lang.reflect.Field

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextColorName = findViewById<EditText>(R.id.editTextColorName)
        val buttonApplyColor = findViewById<Button>(R.id.buttonApplyColor)
        val colorNames = listOf("red", "orange", "yellow", "green", "blue", "indigo", "violet")

        buttonApplyColor.setOnClickListener {
            val colorName = editTextColorName.text.toString().trim().lowercase()

            if (colorName.isEmpty()) {
                return@setOnClickListener
            }

            if (colorName in colorNames) {
                try {
                    val field: Field = R.color::class.java.getField(colorName)
                    val colorId = field.getInt(null)
                    val color = ContextCompat.getColor(this, colorId)
                    buttonApplyColor.setBackgroundColor(color)
                } catch (e: Exception) {
                    Log.e("MainActivity", "Error setting color: ", e)
                }
            } else {
                Log.d("MainActivity", "Пользовательский цвет \"$colorName\" не найден")
            }
        }
    }
}
