package com.kohuyn.weatherapp.data.remote

import com.google.gson.JsonObject
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Single

class AppApiHelper : ApiHelper {

    override fun getCurrenWeather(lat: Double, long: Double): Single<JsonObject> {
        return Rx2AndroidNetworking.get(ApiWebserviceConfig.URL_LAT_LONG)
            .addQueryParameter("lat",lat.toString())
            .addQueryParameter("lon",long.toString())
            .addQueryParameter("appid",ApiWebserviceConfig.APIKEY)
            .addQueryParameter("lang","vi")
            .build()
            .getObjectSingle(JsonObject::class.java)
    }

    override fun getHourWeather(lat: Double, long: Double): Single<JsonObject> {
        return Rx2AndroidNetworking.get(ApiWebserviceConfig.URL_HOUR)
            .addQueryParameter("lat",lat.toString())
            .addQueryParameter("lon",long.toString())
            .addQueryParameter("appid",ApiWebserviceConfig.APIKEY)
            .addQueryParameter("lang","vi")
            .build()
            .getObjectSingle(JsonObject::class.java)
    }
}