package ci.nsu.mobile.main.utils

import android.widget.EditText
import android.widget.Toast
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout

fun EditText.showError(message: String) {
    this.error = message
    this.requestFocus()
}

fun TextInputLayout.showError(message: String) {
    this.error = message
    this.requestFocus()
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}