// Task_3: Вторая Activity. Получает строку через Intent,
// отображает её. TopBar с кнопкой "Назад" возвращает в MainActivity.

package ci.nsu.moble.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ci.nsu.moble.main.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        binding.toolbar.title = "Second Activity"

        val data = intent.getStringExtra(EXTRA_DATA) ?: "No data"
        binding.receivedText.text = data
    }
}
