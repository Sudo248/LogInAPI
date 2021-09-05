package com.duonglh.retrofitapi.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duonglh.retrofitapi.data.model.User
import com.duonglh.retrofitapi.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.duonglh.retrofitapi.data.Result;
import kotlinx.coroutines.withContext
import java.lang.Exception

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _statusLogin = MutableLiveData<Result<User>>()
    val statusLogin: LiveData<Result<User>> = _statusLogin

    private val _statusPost = MutableLiveData<Result<User>>()
    val statusPost: LiveData<Result<User>> = _statusPost

    fun logInUser(email: String, password: String) = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main){
            _statusLogin.value = Result.Loading
        }
        try{
            val users = repository.getUser(email)
            withContext(Dispatchers.Main){
                _statusLogin.value = if(users.isEmpty() || users[0].password != password) Result.Error("Email or Password invalid")
                else Result.Success(users[0])
            }

        }catch (exception: Exception){
            Log.e("error API", exception.message!!)
            withContext(Dispatchers.Main){
                _statusLogin.value = Result.Error("Don't Connected")
            }
        }
    }

    fun confirmUser(email: String, password: String, confirmPassword: String){
        // check email is used
        // check is correct password
        if(password.length < 8){
            _statusPost.value = Result.Error("At least 8 characters")
        }
        // check confirmPassword
        else if(password != confirmPassword){
            _statusPost.value = Result.Error("Wrong Password")
        }else{
            postUser(User(ID++, "Some Thing", email, password))
        }
    }

    private fun postUser(user: User) = viewModelScope.launch(Dispatchers.IO) {
        withContext(Dispatchers.Main){
            _statusPost.value = Result.Loading
        }
        try{
            repository.addUser(user)

            withContext(Dispatchers.Main){
                _statusPost.value = Result.Success(user)
            }
        }catch (exception: Exception){
            Log.e("error API", exception.message!!)
//            withContext(Dispatchers.Main){
//                _statusPost.value = Result.Error("Mail is invalid")
//            }
        }
    }

    companion object{
        var ID = 3
    }

}