package com.duonglh.retrofitapi.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.duonglh.retrofitapi.data.prefs.Prefs
import com.duonglh.retrofitapi.data.repository.UserRepository

import com.duonglh.retrofitapi.ui.MainViewModel

class MainViewModelFactory(
    private val repository: UserRepository,
    private val prefs: Prefs
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(repository, prefs) as T
}