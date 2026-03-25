package ci.nsu.mobile.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import ci.nsu.mobile.main.ui.main.MainFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Загружаем фрагмент при создании активности
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, MainFragment())
            }
        }
    }
}