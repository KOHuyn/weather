package com.kohuyn.weatherapp.data.local.prefs

import com.kohuyn.weatherapp.data.model.city.City

interface PrefsHelper {
    fun getUser():String?

    fun setUser(user:String)

    fun getCity():String?

    fun setCity(id_city:String)

    fun setLoadAds(click:Int)

    fun getLoadAds():Int
}