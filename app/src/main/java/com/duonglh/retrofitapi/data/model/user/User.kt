package com.duonglh.retrofitapi.data.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    var name: String,
    @SerializedName("token_key")
    val token: String
)
