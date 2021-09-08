package com.duonglh.retrofitapi.data.model.login

import com.google.gson.annotations.SerializedName

data class ReponseLogin(
    var email: String,
    var password: String,
    @SerializedName("token_key")
    var token: String
)