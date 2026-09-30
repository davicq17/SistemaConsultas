package com.example.iubconsultas.data.remote

import com.example.iubconsultas.data.local.SessionManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
        private const val SERVER_ADB = "http://localhost:8080/"
        private const val SERVER_EMULATOR = "http://10.0.2.2:8080/"
        private const val SERVER_WIFI = "http://tuIP:8080/"

        private const val BASE_URL = SERVER_WIFI

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val token = SessionManager.token
            val request = if (token != null) {
                chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            } else {
                chain.request()
            }
            val response = chain.proceed(request)

            if (token != null && (response.code == 401 || response.code == 403)) {
                SessionManager.expireSession()
            }

            response
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
