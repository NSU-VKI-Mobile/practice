package ci.nsu.mobile.domain.models

data class QrCodeData(
    val login: String,
    val password: String
) {
    fun toQrString(): String = "$login:$password"

    companion object {
        fun fromQrString(data: String): QrCodeData? {
            val parts = data.split(":")
            if (parts.size == 2) {
                return QrCodeData(login = parts[0], password = parts[1])
            }
            return null
        }
    }
}