package com.duonglh.retrofitapi.data.model.login

import com.google.gson.annotations.SerializedName

data class ResponseLogin(
    val id: Int,
    val email: String,
    var password: String,
    @SerializedName("token_key")
    var token: String
)