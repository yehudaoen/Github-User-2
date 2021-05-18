package com.example.githubuser2

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiBuilder {

    companion object {
        fun api():Endpoint{
            return Retrofit.Builder().baseUrl( "https://api.github.com/").addConverterFactory(GsonConverterFactory.create()).client(
                OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .connectTimeout(10, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).build()).build().create(Endpoint::class.java)
        }
    }

}