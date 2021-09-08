package com.duonglh.retrofitapi.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitAPI {
//    "https://my-json-server.typicode.com/Sudo248/Database/"
    private const val BASE_URL = "http://192.168.16.103:3000/"

    val service: UserService = getRetrofit().create(UserService::class.java)

    private fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}