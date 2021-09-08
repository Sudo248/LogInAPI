package com.duonglh.retrofitapi.di

import android.content.Context
import android.util.Log
import com.duonglh.retrofitapi.data.api.RetrofitAPI
import com.duonglh.retrofitapi.data.prefs.Prefs
import com.duonglh.retrofitapi.data.repository.UserRepository
import com.duonglh.retrofitapi.factory.ShareViewModelFactory
import com.duonglh.retrofitapi.ui.ShareViewModel

interface ViewModelProviderFactory {
    fun provideShareViewModelFactory(context: Context): ShareViewModelFactory
}

object Injector : ViewModelProviderFactory{
    override fun provideShareViewModelFactory(context: Context): ShareViewModelFactory {
        if (instanceShareViewModelFactory == null){
            val prefs = getPrefs(context)
            val repository = getUserRepository(prefs)
            instanceShareViewModelFactory = ShareViewModelFactory(repository)
        }
        return instanceShareViewModelFactory!!
    }

    private var instanceShareViewModelFactory: ShareViewModelFactory? = null

    private fun getUserRepository(prefs: Prefs): UserRepository{
        val apiService = RetrofitAPI.service
        return UserRepository(apiService, prefs)
    }

    private fun getPrefs(context: Context) = Prefs(context)
}