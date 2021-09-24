package com.duonglh.retrofitapi.data.repository

import android.util.Log
import com.duonglh.retrofitapi.data.Result
import com.duonglh.retrofitapi.data.api.UserService
import com.duonglh.retrofitapi.data.model.login.RequestLogin
import com.duonglh.retrofitapi.data.model.login.RequestRegister
import com.duonglh.retrofitapi.data.Error
import com.duonglh.retrofitapi.data.model.login.ResponseLogin
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
    // get user tren server
    suspend fun getUser(token: String) = apiService.fetchUser(token)[0]
    // post user moi len server
    suspend fun postUser(user: PostUser) = apiService.postUser(user)
    // dang ki 1 tai khoan moi
    suspend fun registerAccount(requestRegister: RequestRegister) = apiService.registerUser(requestRegister)

    // lay token tren thiet bi ra
    fun getCurrentToken(): String? {
        val token = prefs.getToken()
        Log.d("Repository","get Token: $token")
        return token
    }

    // Luu token vao thiet bi
    fun saveTokenToDevice(token: String?){
        Log.d("Repository","Save Token $token")
        prefs.saveToken(token)
    }

    fun checkEmailHasUsed(email: String): Flow<Result<Any>> = flow{
        when(val response = fetchLogin(email)){
            is Result.Error -> {
                // Neu lay email khong thanh cong tuc la email nay van chua duoc dang ki
                if(response.message == Error.EMAIl_INVALID){
                    emit(Result.Success(response.message))
                }else{
                    // truong hop bi loi server
                    emit(response)
                }
            }
            // neu lay email thanh cong thi email nay da co tren server roi
            is Result.Success -> emit(Result.Error(Error.EMAIL_HAS_USED))
        }
    }

    // lay token tren server xuong
    fun getTokenKey(requestLogin: RequestLogin): Flow<Result<Any>> = flow{
        when(val response = fetchLogin(requestLogin.email)){
            is Result.Error -> emit(response)
            is Result.Success -> {
                val responseLogin = response.data as ResponseLogin
                if(responseLogin.password != requestLogin.password.toMD5Hash())
                    emit(Result.Error(Error.WRONG_PASSWORD))
                else{
                    emit(Result.Success(responseLogin.token))
                }
            }
        }
    }

    // lay thong tin account Login
    private suspend fun fetchLogin(email: String): Result<Any>{
        return try {
            val responses = apiService.fetchLogin(email)
            if (responses.isEmpty()) Result.Error(Error.EMAIl_INVALID)
            else Result.Success(responses[0])
        }catch (e: Exception){
            Log.d("User Repository", "Server invalid")
            Result.Error(Error.SERVER_INVALID)
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