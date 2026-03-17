package ci.nsu.moble.main.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ci.nsu.mobile.main.R
import ci.nsu.moble.main.ui.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}