package com.duonglh.retrofitapi.data


sealed class Result<out R>{
    data class Success<out T> (val data: T) : Result<T>()
    data class Error(val messageException: String) : Result<Nothing>()
    object Loading : Result<Nothing>()

}
