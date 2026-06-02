package ci.nsu.mobile.main.viewmodel

import androidx.lifecycle.ViewModel
import android.os.Process

class MainViewModel : ViewModel() {
    fun exitApp() {
        Process.killProcess(Process.myPid())
    }
}