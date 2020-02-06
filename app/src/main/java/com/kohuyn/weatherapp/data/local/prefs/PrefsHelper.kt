package com.kohuyn.weatherapp.data.local.prefs

interface PrefsHelper {
    fun getUser():String?

    fun setUser(user:String)
}