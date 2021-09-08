package com.duonglh.retrofitapi.data.api

import com.duonglh.retrofitapi.data.model.login.RequestRegister
import com.duonglh.retrofitapi.data.model.login.ResponseLogin
import com.duonglh.retrofitapi.data.model.user.PostUser
import com.duonglh.retrofitapi.data.model.user.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    @GET("users")
    suspend fun fetchUser(@Query("token_key") token: String): List<User>

    @POST("users")
    suspend fun postUser(@Body user: PostUser)

    @GET("login")
    suspend fun fetchLogin(@Query("email") email: String): List<ResponseLogin>

    @POST("login")
    suspend fun registerUser(@Body requestRegister: RequestRegister)
}