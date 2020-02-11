package com.kohuyn.weatherapp.data

import com.google.gson.JsonObject
import com.kohuyn.weatherapp.data.local.prefs.PrefsHelper
import com.kohuyn.weatherapp.data.model.city.City
import com.kohuyn.weatherapp.data.remote.ApiHelper
import io.reactivex.Single

class AppDataManager constructor(private val prefsHelper:PrefsHelper,private val apiHelper: ApiHelper):DataManager{
    override fun getCurrenWeather(lat: Double, long: Double): Single<JsonObject> = apiHelper.getCurrenWeather(lat, long)

    override fun getHourWeather(lat: Double, long: Double): Single<JsonObject> = apiHelper.getHourWeather(lat, long)

    override fun getCurrenWeatherId(id: Int): Single<JsonObject>  = apiHelper.getCurrenWeatherId(id)

    override fun getUser(): String? = prefsHelper.getUser()

    override fun setUser(user: String) = prefsHelper.setUser(user)

    override fun getCity(): String? = prefsHelper.getCity()

    override fun setCity(id_city:String) = prefsHelper.setCity(id_city)

}