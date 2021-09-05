package com.duonglh.retrofitapi.data.api

import com.duonglh.retrofitapi.data.model.User
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitAPI {
    private const val BASE_URL = "https://my-json-server.typicode.com/Sudo248/Database/"

    val service: UserService = getRetrofit().create(UserService::class.java)

    private fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}