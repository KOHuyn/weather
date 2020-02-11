package com.kohuyn.weatherapp.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.kohuyn.weatherapp.data.model.city.City

class AppPrefsHelper constructor(context:Context,prefsName:String,private val gson:Gson):PrefsHelper {
    companion object{
        const val USER = "USER"
        const val CITY = "city"
    }
    private val sharedPreferences:SharedPreferences = context.getSharedPreferences(prefsName,Context.MODE_PRIVATE)

    override fun getUser(): String? = sharedPreferences.getString(USER,"")

    override fun setUser(user: String) {
        sharedPreferences.edit().putString(USER,user).apply()
    }

    override fun getCity(): String? = sharedPreferences.getString(CITY,"")

    override fun setCity(id_city:String) {
       sharedPreferences.edit().putString(CITY,id_city).apply()
    }
}