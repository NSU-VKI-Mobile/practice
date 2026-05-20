package ci.nsu.mobile.main.units.data.api

import ci.nsu.mobile.main.units.data.token.TokenManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) {
}