package com.kohuyn.weatherapp.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class AppPrefsHelper constructor(context:Context,prefsName:String,private val gson:Gson):PrefsHelper {
    companion object{
        const val USER = "USER"
    }
    private val sharedPreferences:SharedPreferences = context.getSharedPreferences(prefsName,Context.MODE_PRIVATE)

    override fun getUser(): String? = sharedPreferences.getString(USER,"")

    override fun setUser(user: String) {
        sharedPreferences.edit().putString(USER,user).apply()
    }
}