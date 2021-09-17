package com.duonglh.retrofitapi.di

import android.content.Context
import com.duonglh.retrofitapi.data.api.RetrofitAPI
import com.duonglh.retrofitapi.data.api.UserService
import com.duonglh.retrofitapi.data.prefs.Prefs
import com.duonglh.retrofitapi.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofitService() = RetrofitAPI.service

    @Singleton
    @Provides
    fun providePref( @ApplicationContext app: Context ) = Prefs(app)

    @Singleton
    @Provides
    fun provideUserRepository(
        apiService: UserService,
        prefs: Prefs
    ) = UserRepository(apiService, prefs)

}