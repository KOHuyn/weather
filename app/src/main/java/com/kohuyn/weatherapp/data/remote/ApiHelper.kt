package com.kohuyn.weatherapp.data.remote

import com.google.gson.JsonObject
import io.reactivex.Single

interface ApiHelper {
    fun getCurrenWeather(lat:Double,long:Double): Single<JsonObject>

    fun getHourWeather(lat: Double,long: Double):Single<JsonObject>

    fun getCurrenWeatherId(id:Int):Single<JsonObject>
}