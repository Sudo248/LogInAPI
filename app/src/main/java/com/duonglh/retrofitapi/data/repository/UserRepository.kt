package com.duonglh.retrofitapi.data.repository

import android.util.Log
import com.duonglh.retrofitapi.data.Result
import com.duonglh.retrofitapi.data.api.UserService
import com.duonglh.retrofitapi.data.model.login.RequestLogin
import com.duonglh.retrofitapi.data.model.login.RequestRegister
import com.duonglh.retrofitapi.data.Error
import com.duonglh.retrofitapi.data.model.user.PostUser
import com.duonglh.retrofitapi.data.prefs.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class UserRepository(
    private val apiService: UserService,
    private val prefs: Prefs
) {

    suspend fun getUser(token: String) = apiService.fetchUser(token)[0]
    suspend fun postUser(user: PostUser) = apiService.postUser(user)
    suspend fun registerAccount(requestRegister: RequestRegister) = apiService.registerUser(requestRegister)

    fun getCurrentToken(): String? {
        val token = prefs.getToken()
        Log.d("Repository","get Token: $token")
        return token
    }

    fun saveTokenToDevice(token: String?){
        Log.d("Repository","Save Token $token")

        prefs.saveToken(token)
    }

    suspend fun checkEmailHasUsed(email: String): Boolean{
        val responses = apiService.fetchLogin(email)
        return responses.isNotEmpty()
    }

    fun getTokenKey(requestLogin: RequestLogin): Flow<Result<Any>> = flow{
        val responses = apiService.fetchLogin(requestLogin.email)
        if(responses.isEmpty()) {
            emit(Result.Error(Error.EMAIl_INVALID))
        }else{
            val responseLogin = responses[0]
            if(responseLogin.password != requestLogin.password.toMD5Hash())
                emit(Result.Error(Error.WRONG_PASSWORD))
            else{
                emit(Result.Success(responseLogin.token))
            }
        }
    }

}

fun String.toMD5Hash(salt: ByteArray? = null): String{
    val digest = MessageDigest.getInstance("MD5")
    if(salt != null) digest.update(salt)
    val  sb = StringBuilder()
    val result = digest.digest(this.toByteArray(Charsets.UTF_8))
    for (i in result){
        sb.append(String.format("%02X",i))
    }
    return sb.toString()
}