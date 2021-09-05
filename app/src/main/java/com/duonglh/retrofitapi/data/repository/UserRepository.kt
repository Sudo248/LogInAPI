package com.duonglh.retrofitapi.data.repository

import com.duonglh.retrofitapi.data.api.UserService
import com.duonglh.retrofitapi.data.model.User

class UserRepository(private val apiService: UserService) {

    suspend fun getUser(email: String) = apiService.fetchUser(email)
    suspend fun addUser(user: User) = apiService.postUser(user)

}