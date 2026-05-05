package ci.nsu.mobile.main.core



data class Settings(
    val api: Api = Api()
)

data class Api(
    val host: String = "10.213.59.79",
    val port: Int = 8080,
    val url: String = "http://10.213.59.79:8080"
)

val settings = Settings()