package com.duonglh.retrofitapi.data.model.user

import com.google.gson.annotations.SerializedName

data class PostUser(
    val name: String,
    @SerializedName("token_key")
    val token: String
)