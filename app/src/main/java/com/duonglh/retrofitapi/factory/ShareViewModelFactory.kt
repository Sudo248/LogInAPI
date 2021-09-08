package com.duonglh.retrofitapi.factory

import androidx.lifecycle.ViewModelProvider
import com.duonglh.retrofitapi.data.prefs.Prefs
import com.duonglh.retrofitapi.data.repository.UserRepository
import com.duonglh.retrofitapi.ui.ShareViewModel

class ShareViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T = ShareViewModel.getInstance(repository) as T
}