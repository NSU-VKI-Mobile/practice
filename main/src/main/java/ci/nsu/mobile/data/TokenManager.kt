package ci.nsu.mobile.data

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREFS_NAME = "auth_prefs"
    private const val KEY_TOKEN = "jwt_token"

    private var prefs: SharedPreferences? = null

    // Вызываем один раз в MainActivity.onCreate
    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }

    // Наше свойство для ТЗ, теперь защищенное от скрытых багов
    var token: String?
        get() {
            // Если prefs каким-то чудом null, приложение не промолчит, а честно скажет в чем косяк
            val actualPrefs = requireNotNull(prefs) { "Сначала нужно вызвать TokenManager.init(context)!" }
            return actualPrefs.getString(KEY_TOKEN, null)
        }
        set(value) {
            val actualPrefs = requireNotNull(prefs) { "Сначала нужно вызвать TokenManager.init(context)!" }
            actualPrefs.edit().putString(KEY_TOKEN, value).apply()
        }

    // Очистка при выходе из профиля
    fun clearToken() {
        val actualPrefs = requireNotNull(prefs) { "Сначала нужно вызвать TokenManager.init(context)!" }
        actualPrefs.edit().remove(KEY_TOKEN).apply()
    }
}
