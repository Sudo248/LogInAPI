package com.duonglh.retrofitapi.data.api

import com.duonglh.retrofitapi.data.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    @GET("users")
    suspend fun fetchUser(@Query("email") email: String): List<User>

    @POST("users")
    suspend fun postUser(@Body user: User)
}