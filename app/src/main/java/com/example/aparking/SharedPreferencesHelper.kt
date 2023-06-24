package com.example.aparking

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE)

    private val PREFS_NAME = "app_prefs"
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString("TOKEN", token)
        editor.apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("TOKEN", null)
    }

    fun clearToken() {
        val editor = sharedPreferences.edit()
        editor.remove("TOKEN")
        editor.apply()
    }

    fun isUserLogged(): Boolean {
        return sharedPreferences.getBoolean("isLogged", false)
    }

    fun setIsUserLogged(isLogged: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLogged", isLogged)
        editor.apply()
    }

    // telephone
    fun savePhoneNumber(phoneNumber: String) {
        val editor = sharedPreferences.edit()
        editor.putString("PHONE_NUMBER", phoneNumber)
        editor.apply()
    }

    fun getPhoneNumber(): String? {
        return sharedPreferences.getString("PHONE_NUMBER", null)
    }

    // response
    fun saveRequestId(requestId: String) {
        val editor = prefs.edit()
        editor.putString("REQUEST_ID", requestId)
        editor.apply()
    }

    fun getRequestId(): String {
        return prefs.getString("REQUEST_ID", "") ?: ""
    }
}
