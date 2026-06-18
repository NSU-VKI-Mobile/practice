package ci.nsu.moble.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ci.nsu.moble.main.ui.main.FirstFragment

class firstActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, FirstFragment.newInstance())
                .commitNow()
        }
    }
}