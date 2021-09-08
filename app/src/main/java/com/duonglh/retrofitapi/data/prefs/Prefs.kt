package com.duonglh.retrofitapi.data.prefs

import android.content.Context
import android.content.SharedPreferences
import java.util.prefs.Preferences

class Prefs(private val context: Context) {

    private val Prefs = "MyPrefs"

    private val prefs: SharedPreferences by lazy{
        context.getSharedPreferences(Prefs, Context.MODE_PRIVATE)
    }

    suspend fun getToken(): String?{
        return prefs.getString("token_key",null)
    }

    suspend fun saveToken(token: String?){
        val editor = prefs.edit()
        editor.putString("token_key", token)
        editor.apply()
    }
}