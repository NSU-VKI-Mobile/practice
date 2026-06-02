package ci.nsu.mobile.main.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

//ХРАНЕНИЕ ТОКЕНА
class TokenManager(context: Context) {
    private val prefs = context.getSharedPreferences("auth_prefs", MODE_PRIVATE)
    //SharedPreferences - хранилище ключ-значение (данные сохраняются даже после закрытия приложения)
    //Имя файла, где будут храниться данные. Файл: auth_prefs.xml
    //MODE_PRIVATE	Режим доступа - только твое приложение может читать этот файл
    companion object { //companion object	Блок, который принадлежит классу, а не объекту.
        private const val TOKEN_KEY = "jwt_token"
    }

    var token: String?
        get() = prefs.getString(TOKEN_KEY, null) //чтение
        set(value) { //запись токена
            if (value == null) {
                prefs.edit().remove(TOKEN_KEY).apply() //apply() - асинхронный (быстрее, не блокирует поток)
                                                     //apply() - сохраняем изменения
                                                    //edit() - редактирование
            } else {
                prefs.edit().putString(TOKEN_KEY, value).apply()
            }
        }

    fun clear() { //clear() - удаляет все данные (выход из аккаунта)
        prefs.edit().clear().apply()
    }
}