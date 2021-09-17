package com.duonglh.retrofitapi.ui

import android.util.Log
import androidx.lifecycle.LiveData

import androidx.lifecycle.*
import com.duonglh.retrofitapi.data.Result
import com.duonglh.retrofitapi.data.model.login.RequestLogin
import com.duonglh.retrofitapi.data.model.login.RequestRegister
import com.duonglh.retrofitapi.data.model.user.User
import com.duonglh.retrofitapi.data.repository.UserRepository
import com.duonglh.retrofitapi.data.repository.toMD5Hash
import com.duonglh.retrofitapi.data.Error
import com.duonglh.retrofitapi.data.model.user.PostUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception
import java.security.SecureRandom
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor (private val repository: UserRepository) : ViewModel() {
    private val TAG = "ShareViewModel"
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user
    private var isOpenApp: Boolean = true

    fun login(email: String, password: String): LiveData<Result<Any>> = liveData(IO){
        emit((Result.Loading))
        repository.getTokenKey(RequestLogin(email, password)).collect {
            when(it){
                is Result.Error -> emit(it)
                is Result.Success -> {
                    val token = it.data
                    try {
                        val user = repository.getUser(token as String)
                        _user.postValue(user)
                        emit(Result.Success(user))
                        repository.saveTokenToDevice(token)
                        Log.d(TAG, "emit User ${_user.value}")
                    }catch (e: Exception){
                        Log.e(TAG, "Error loginUser")
                        emit(Result.Error(Error.NOT_FOUND))
                    }
                }
                else ->{
                    Log.e(TAG, "Error loginUser")
                }
            }
        }
    }

    fun register(name: String, email: String, password: String, confirmPassword: String): LiveData<Result<Any>>
    = liveData(IO) {
        emit(Result.Loading)
        when {
            password.length < 6 -> emit(Result.Error(Error.WRONG_FORMAT_PASSWORD))
            password != confirmPassword -> emit(Result.Error(Error.WRONG_PASSWORD))
            repository.checkEmailHasUsed(email) -> emit(Result.Error(Error.EMAIl_INVALID))
            else -> {
                val hashPassword = password.toMD5Hash()
                val token = genToken(email+password)
                val account = RequestRegister(email, hashPassword, token)
                val postUser = PostUser(name, token)

                repository.registerAccount(account)
                repository.postUser(postUser)
                val user = repository.getUser(token)
                _user.postValue(user)
                emit(Result.Success(user))
                repository.saveTokenToDevice(token)
            }
        }
    }

    fun loginWithToken(): LiveData<Result<Any>> = liveData(IO){
        emit(Result.Loading)
        Log.d(TAG, "is open app: $isOpenApp")
        if(isOpenApp){
            isOpenApp = false
            val currentToken = repository.getCurrentToken()
            if (currentToken != null){
                try {
                    Log.d(TAG,"current Token: $currentToken")
                    val user = repository.getUser(currentToken)
                    _user.postValue(user)
                    emit(Result.Success(user))
                    Log.d(TAG, "emit User@ ${_user.value}")
                }catch (e: Exception){
                    emit(Result.Error(Error.NOT_FOUND))
                }
            }else{
                emit(Result.Error("No Token"))
            }
        }else{
            emit(Result.Error("No Open App"))
        }
    }

    private fun getSalt(): ByteArray{
        val sr = SecureRandom.getInstance("SHA1PRNG")
        val salt = ByteArray(16)
        sr.nextBytes(salt)
        return salt
    }

    private fun genToken(strToken: String): String{
        return strToken.toMD5Hash(getSalt())
    }

    fun clearToken(){
        CoroutineScope(IO).launch {
            repository.saveTokenToDevice(null)
            Log.d(TAG,"cleared Token")
        }
    }
}