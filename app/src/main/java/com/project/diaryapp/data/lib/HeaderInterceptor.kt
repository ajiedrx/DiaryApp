package com.project.diaryapp.data.lib

import android.content.SharedPreferences
import com.project.diaryapp.Const
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class HeaderInterceptor(
    private val headers: HashMap<String, String>,
    private val sharedPreferences: SharedPreferences,
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = mapHeaders(chain)
        return chain.proceed(request)
    }

    private fun mapAccessToken() {
        if(sharedPreferences.contains(Const.USER_TOKEN)){
            val peekAuthToken = sharedPreferences.getString(Const.USER_TOKEN, "")
            headers["Authorization"] = "Bearer $peekAuthToken"
        }
    }

    private fun mapHeaders(chain: Interceptor.Chain): Request {
        var original = chain.request()
        //Defaul headers
        headers["Content-Type"] = "application/json"
        headers["Accept"] = "application/json"

        val authorizationHeadersMap = original.headers().values("Authorization")
        if (authorizationHeadersMap.any()) {
            original = original.newBuilder().removeHeader("Authorization").build()
            mapAccessToken()
        }

        val requestBuilder = original.newBuilder()
        for ((key, value) in headers) {
            requestBuilder.addHeader(key, value)
        }
        headers.clear()
        return requestBuilder.build()
    }
}
