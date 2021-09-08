package com.duonglh.retrofitapi.data


sealed class Result<out R>{
    data class Success<out T> (val data: T) : Result<T>()
    data class Error<out T>(val message: T) : Result<T>()
    object Loading : Result<Nothing>()
}

enum class Error{
    WRONG_PASSWORD,
    EMAIl_INVALID,
    EMAIL_HAS_USED,
    WRONG_FORMAT_PASSWORD,
    NOT_FOUND
}
