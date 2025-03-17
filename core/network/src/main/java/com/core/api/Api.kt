package com.core.api

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Api {

    private const val TAG = "OkHttp"

    //日志拦截器
    private const val defaultReadTimeout = 35L
    private const val defaultConnectTimeout = 35L
    private const val defaultWriteTimeout = 35L

    var readTimeout = defaultReadTimeout
    var connectTimeout = defaultConnectTimeout
    var writeTimeout = defaultWriteTimeout


    private val logInterceptor = HttpLoggingInterceptor { message -> Log.d(TAG, message) }
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private fun getOkHttpClient(): OkHttpClient {
        return Builder().retryOnConnectionFailure(true)
            .readTimeout(readTimeout, TimeUnit.SECONDS)          // 读取超时时间
            .connectTimeout(connectTimeout, TimeUnit.SECONDS)    // 连接超时时间
            .writeTimeout(writeTimeout, TimeUnit.SECONDS)        // 写超时
            .addInterceptor(logInterceptor)
//            .addInterceptor(ReceivedCookiesInterceptor())
//            .addInterceptor(TokenInterceptor())
            .build()
    }

    val retrofitBuilder: Retrofit.Builder
        get() = Retrofit
            .Builder()
            .client(getOkHttpClient())
//            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))

    inline fun <reified T> create(
        retrofitBuilder: Retrofit.Builder = Api.retrofitBuilder, url: String
    ): T {
        return retrofitBuilder.baseUrl(url.replace(" ", "")).build().create(T::class.java)
    }


}