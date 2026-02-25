package ci.nsu.moble.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ci.nsu.moble.main.ui.main.MainFragment
import ColorFragment
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ColorFragment()) // R.id.fragment_container должен быть в activity_main.xml
                .commit()
        }
    }
}