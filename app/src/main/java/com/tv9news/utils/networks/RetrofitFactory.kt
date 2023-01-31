package com.tv9news.utils.networks

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.tv9news.BuildConfig
import com.tv9news.utils.helpers.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitFactory {
    fun getInstance(context: Context): WebInterface {
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.MINUTES)
            .readTimeout(10, TimeUnit.MINUTES)
            .writeTimeout(10, TimeUnit.MINUTES)
        val logging = HttpLoggingInterceptor()
        logging.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        httpClient.addInterceptor(logging)
        httpClient.addInterceptor(NetworkConnectionInterceptor(context))
        httpClient.addInterceptor { chain ->
            chain.run {
                proceed(
                    request()
                        .newBuilder()
                        .header(Constants.USERID, "0")
                        .header(Constants.APP_ID, "74")
                        .header(Constants.DEVICE_TYPE, "1")
                        .header(Constants.LANGUAGE, "1")
                        .header(Constants.VERSION, "1")
                        .header(Constants.AUTHORIZATION, ApiConstants.AUTHORIZATION_BEARAR)
                        .build()

                )
            }
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        return retrofit.create(WebInterface::class.java)

    }


}