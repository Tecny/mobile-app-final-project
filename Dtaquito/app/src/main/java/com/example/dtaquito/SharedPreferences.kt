package com.example.dtaquito

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString("token", token)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString("token", null)
    }

    fun getUserId(): Int {
        return prefs.getInt("id", -1) // -1 or any default value if not found
    }
    fun saveUserId(id: Int) {
        val editor = prefs.edit()
        editor.putInt("id", id)
        editor.apply()
    }
}