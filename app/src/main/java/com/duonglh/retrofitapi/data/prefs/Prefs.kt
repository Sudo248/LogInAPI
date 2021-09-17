package com.duonglh.retrofitapi.data.prefs

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.util.prefs.Preferences

class Prefs(private val context: Context) {
    private val TAG ="From Pref"
    private val Prefs = "MyPrefs"

    private val prefs: SharedPreferences by lazy{
        Log.d(TAG, "init: pref")
        context.getSharedPreferences(Prefs, Context.MODE_PRIVATE)
    }

    fun getToken(): String?{
        return prefs.getString("token_key",null)
    }

    fun saveToken(token: String?){
        Log.d(TAG, "saveToken: $token")
        val editor = prefs.edit()
        editor.putString("token_key", token)
        editor.apply()
    }
}