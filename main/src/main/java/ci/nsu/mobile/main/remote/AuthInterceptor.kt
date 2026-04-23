package ci.nsu.mobile.main.remote

//class AuthInterceptor : Interceptor {
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val originalRequest = chain.request()
//
//        val requestBuilder = originalRequest.newBuilder()
//
//        val token = TokenManager.token
//        requestBuilder.addHeader("Content-Type", "application/json")
//        token?.let {
//            requestBuilder.addHeader("Authorization", "Bearer $it")
//        }
//
//        return chain.proceed(requestBuilder.build())
//    }
//}