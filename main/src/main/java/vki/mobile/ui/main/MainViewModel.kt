package vki.mobile.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _text = MutableLiveData("Привет!")
    private var _flag = false
    val text: LiveData<String> = _text

    fun onButtonClicked() {
        _flag = !_flag
        _text.value = if (_flag) "Hello World" else "Привет!"
    }
}