package com.nagycsongor.moviedatabase.Retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitMoviesClient {
    private var retrofit: Retrofit? = null
    private val BASE_URL = "https://api.themoviedb.org/3/"

    val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    val client : OkHttpClient = OkHttpClient.Builder().apply {
        this.addInterceptor(interceptor)

    }.build()

    val retrofitInstance: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }


            return retrofit
        }
}