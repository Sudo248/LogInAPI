package com.duonglh.retrofitapi.di

import android.content.Context
import com.duonglh.retrofitapi.data.api.RetrofitAPI
import com.duonglh.retrofitapi.data.repository.UserRepository
import com.duonglh.retrofitapi.factory.MainViewModelFactory
import com.duonglh.retrofitapi.ui.MainViewModel

interface ViewModelProviderFactory {
    fun provideMainViewModelFactory(context: Context): MainViewModelFactory
}

object Injector : ViewModelProviderFactory{
    override fun provideMainViewModelFactory(context: Context): MainViewModelFactory {
        val repository = getUserRepository(context)
        return MainViewModelFactory(repository)
    }
    private fun getUserRepository(context: Context): UserRepository{
        val apiService = RetrofitAPI.service
        return UserRepository(apiService)
    }
}